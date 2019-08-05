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
import pl.wavesoftware.utils.stringify.impl.inspector.StringifierContext;
import pl.wavesoftware.utils.stringify.impl.inspector.InspectorModule;
import pl.wavesoftware.utils.stringify.impl.inspector.ObjectInspector;
import pl.wavesoftware.utils.stringify.spi.theme.ComplexObjectStyle;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;
import static pl.wavesoftware.eid.utils.EidPreconditions.checkNotNull;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class InspectorBasedToStringResolver implements ToStringResolver {
  private static final Iterable<ObjectInspector> OBJECT_INSPECTORS =
    InspectorModule.INSTANCE.inspectors();

  private final DefaultConfiguration configuration;
  private final InspectionPoint point;
  private final StringifierContext stringifierContext;
  private final BeanFactoryCache beanFactoryCache;
  private final InspectingFieldFactory inspectingFieldFactory;

  InspectorBasedToStringResolver(
    DefaultConfiguration configuration,
    InspectionPoint point,
    StringifierContext stringifierContext,
    BeanFactoryCache beanFactoryCache,
    InspectingFieldFactory inspectingFieldFactory
  ) {
    this.configuration = configuration;
    this.point = point;
    this.stringifierContext = stringifierContext;
    this.beanFactoryCache = beanFactoryCache;
    this.inspectingFieldFactory = inspectingFieldFactory;
  }

  @Override
  public CharSequence resolve() {
    stringifierContext.markAsInspected(point.getValue().get());
    ComplexObjectStyle style = stringifierContext.theme().complexObject();
    StringBuilder sb = new StringBuilder();
    sb.append(style.begin(point));
    sb.append(style.name(point));
    CharSequence props = propertiesForToString();
    if (props.length() != 0) {
      sb.append(style.nameSeparator(point));
      sb.append(props);
    }
    sb.append(style.end(point));
    return sb;
  }

  private CharSequence propertiesForToString() {
    Map<String, CharSequence> props;
    props = inspectTargetAsClass(point.getType().get());
    ComplexObjectStyle style = stringifierContext.theme().complexObject();
    StringBuilder sb = new StringBuilder();
    CharSequence propertySeparator = style.propertySeparator(point);
    for (Map.Entry<String, CharSequence> entry : props.entrySet()) {
      String fieldName = entry.getKey();
      CharSequence fieldStringValue = entry.getValue();
      sb.append(fieldName);
      sb.append(style.propertyEquals(point));
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
      InspectingField inspectingField = inspectingFieldFactory
        .create(field, point.getValue().get(), beanFactoryCache);
      FieldInspectionPoint inspectionPoint = createInspectionPoint(inspectingField);
      if (inspectingField.shouldInspect(inspectionPoint)) {
        inspectAnnotatedField(properties, inspectionPoint, inspectingField);
      }
    }
  }

  private FieldInspectionPoint createInspectionPoint(InspectingField field) {
    return new FieldInspectionPointImpl(
      field, stringifierContext.inspectionContext()
    );
  }

  private void inspectAnnotatedField(
    final Map<String, CharSequence> properties,
    final InspectionPoint inspectionPoint,
    final InspectingField inspectingField
  ) {
    tryToExecute(() -> {
      @Nullable Object value = inspectionPoint.getValue().get();
      Optional<CharSequence> maybeMasked = inspectingField.masker()
        .map(masker -> masker.mask(value))
        .map(masked -> checkNotNull(masked, "20190724:231722"));
      @Nullable CharSequence inspected = maybeMasked.orElseGet(() ->
        value != null ? inspectObject(inspectionPoint) : null
      );
      if (inspected != null || inspectingField.showNull()) {
        properties.put(inspectingField.getName(), inspected);
      }
    }, "20130422:154938");
  }

  CharSequence inspectObject(InspectionPoint point) {
    for (ObjectInspector inspector : OBJECT_INSPECTORS) {
      if (inspector.consentTo(point, stringifierContext)) {
        return inspector.inspect(point, stringifierContext);
      }
    }
    ToStringResolverImpl sub = new ToStringResolverImpl(
      point,
      configuration,
      stringifierContext,
      beanFactoryCache,
      inspectingFieldFactory
    );
    return sub.resolve();
  }

  void clear() {
    beanFactoryCache.clear();
  }
}
