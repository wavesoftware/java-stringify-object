/*
 * Copyright 2018-2019 Wave Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.impl.beans.BeanFactoryCache;
import pl.wavesoftware.utils.stringify.impl.inspector.InspectionContext;
import pl.wavesoftware.utils.stringify.impl.inspector.InspectorModule;
import pl.wavesoftware.utils.stringify.impl.inspector.ObjectInspector;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class InspectorBasedToStringResolver implements ToStringResolver {
  private static final Iterable<ObjectInspector> OBJECT_INSPECTORS =
    InspectorModule.INSTANCE.inspectors();

  private final DefaultConfiguration configuration;
  private final Object target;
  private final InspectionContext inspectionContext;
  private final Function<Object, CharSequence> alternative;
  private final BeanFactoryCache beanFactoryCache;
  private final InspectingFieldFactory inspectingFieldFactory;

  InspectorBasedToStringResolver(
    DefaultConfiguration configuration,
    Object target,
    InspectionContext inspectionContext,
    Function<Object, CharSequence> alternative,
    BeanFactoryCache beanFactoryCache,
    InspectingFieldFactory inspectingFieldFactory
  ) {
    this.configuration = configuration;
    this.target = target;
    this.inspectionContext = inspectionContext;
    this.alternative = alternative;
    this.beanFactoryCache = beanFactoryCache;
    this.inspectingFieldFactory = inspectingFieldFactory;
  }

  @Override
  public CharSequence resolve() {
    inspectionContext.markIsInspected(target);
    StringBuilder sb = new StringBuilder();
    sb.append('<');
    sb.append(target.getClass().getSimpleName());
    CharSequence props = propertiesForToString();
    if (props.length() != 0) {
      sb.append(' ');
      sb.append(props);
    }
    sb.append('>');
    return sb;
  }

  private CharSequence propertiesForToString() {
    Map<String, CharSequence> props;
    props = inspectTargetAsClass(target.getClass());
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, CharSequence> entry : props.entrySet()) {
      String fieldName = entry.getKey();
      CharSequence fieldStringValue = entry.getValue();
      sb.append(fieldName);
      sb.append("=");
      sb.append(fieldStringValue);
      sb.append(", ");
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb;
  }

  private Map<String, CharSequence> inspectTargetAsClass(Class<?> type) {
    Class<?> supertype = type.getSuperclass();
    Map<String, CharSequence> props;
    if (supertype == null || supertype.equals(Object.class)) {
      props = new LinkedHashMap<>();
    } else {
      props = inspectTargetAsClass(supertype);
    }
    inspectFields(type.getDeclaredFields(), props);
    return props;
  }

  private void inspectFields(Field[] fields,
                             Map<String, CharSequence> properties) {
    for (Field field : fields) {
      InspectionPoint inspectionPoint = createInspectionPoint(field);
      InspectingField inspectingField = inspectingFieldFactory.create(inspectionPoint, beanFactoryCache);
      if (inspectingField.shouldInspect()) {
        inspectAnnotatedField(properties, field, inspectingField);
      }
    }
  }

  private InspectionPoint createInspectionPoint(Field field) {
    return new InspectionPointImpl(field, target);
  }

  private void inspectAnnotatedField(final Map<String, CharSequence> properties,
                                     final Field field,
                                     final InspectingField inspectingField) {
    tryToExecute(() -> {
      ensureAccessible(field);
      Object value = field.get(target);
      if (value == null) {
        if (inspectingField.showNull()) {
          properties.put(field.getName(), null);
        }
      } else {
        properties.put(field.getName(), inspectObject(value));
      }
    }, "20130422:154938");
  }

  private static void ensureAccessible(Field field) {
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }
  }

  CharSequence inspectObject(Object object) {
    for (ObjectInspector inspector : OBJECT_INSPECTORS) {
      if (inspector.consentTo(object, inspectionContext)) {
        return inspector.inspect(object, alternative);
      }
    }
    ToStringResolverImpl sub = new ToStringResolverImpl(
      object,
      configuration,
      inspectionContext,
      beanFactoryCache,
      inspectingFieldFactory
    );
    return sub.resolve();
  }

  void clear() {
    beanFactoryCache.clear();
  }
}
