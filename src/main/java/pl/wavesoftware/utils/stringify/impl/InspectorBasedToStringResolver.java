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
import pl.wavesoftware.utils.stringify.spi.theme.ComplexObjectStyle;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

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
  private final BeanFactoryCache beanFactoryCache;
  private final InspectingFieldFactory inspectingFieldFactory;

  InspectorBasedToStringResolver(
    DefaultConfiguration configuration,
    Object target,
    InspectionContext inspectionContext,
    BeanFactoryCache beanFactoryCache,
    InspectingFieldFactory inspectingFieldFactory
  ) {
    this.configuration = configuration;
    this.target = target;
    this.inspectionContext = inspectionContext;
    this.beanFactoryCache = beanFactoryCache;
    this.inspectingFieldFactory = inspectingFieldFactory;
  }

  @Override
  public CharSequence resolve() {
    inspectionContext.markAsInspected(target);
    ComplexObjectStyle style = inspectionContext.theme().complexObject();
    StringBuilder sb = new StringBuilder();
    sb.append(style.begin());
    sb.append(style.name(target));
    CharSequence props = propertiesForToString();
    if (props.length() != 0) {
      sb.append(style.nameSeparator());
      sb.append(props);
    }
    sb.append(style.end());
    return sb;
  }

  private CharSequence propertiesForToString() {
    Map<String, CharSequence> props;
    props = inspectTargetAsClass(target.getClass());
    ComplexObjectStyle style = inspectionContext.theme().complexObject();
    StringBuilder sb = new StringBuilder();
    CharSequence propertySeparator = style.propertySeparator();
    for (Map.Entry<String, CharSequence> entry : props.entrySet()) {
      String fieldName = entry.getKey();
      CharSequence fieldStringValue = entry.getValue();
      sb.append(fieldName);
      sb.append(style.propertyEquals());
      sb.append(fieldStringValue);
      sb.append(propertySeparator);
    }
    if (!props.isEmpty()) {
      for (int i = 0; i < propertySeparator.length(); i++) {
        sb.deleteCharAt(sb.length() - 1);
      }
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

  private void inspectFields(
    Field[] fields, Map<String, CharSequence> properties
  ) {
    for (Field field : fields) {
      InspectionPoint inspectionPoint = createInspectionPoint(field);
      InspectingField inspectingField = inspectingFieldFactory
        .create(inspectionPoint, beanFactoryCache);
      if (inspectingField.shouldInspect()) {
        inspectAnnotatedField(properties, field, inspectingField);
      }
    }
  }

  private InspectionPoint createInspectionPoint(Field field) {
    return new InspectionPointImpl(field, target);
  }

  private void inspectAnnotatedField(
    final Map<String, CharSequence> properties,
    final Field field,
    final InspectingField inspectingField
  ) {
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
        return inspector.inspect(object, inspectionContext);
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
