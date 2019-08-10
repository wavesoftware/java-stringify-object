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

package pl.wavesoftware.utils.stringify.impl.inspector;

import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.spi.theme.IterableStyle;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class IterableInspector implements ObjectInspector {
  @Override
  public boolean consentTo(InspectionPoint point, StringifierContext context) {
    return Iterable.class.isAssignableFrom(point.getType().get());
  }

  @Override
  public CharSequence inspect(InspectionPoint point, StringifierContext context) {
    return inspectIterable((Iterable<?>) point.getValue().get(), point, context);
  }

  private static CharSequence inspectIterable(
    Iterable<?> iterable,
    InspectionPoint point,
    StringifierContext context
  ) {
    IterableStyle style = context.theme().iterable();
    StringBuilder sb = new StringBuilder();
    sb.append(style.begin(point));
    for (Object elem : iterable) {
      InspectionPoint subPoint =
        InspectorModule.INSTANCE.objectInspectionPoint(elem, point::getContext);
      sb.append(context.rootInspector().apply(subPoint));
      sb.append(style.separator(point));
    }
    if (sb.length() > style.begin(point).length()) {
      for (int i=0; i < style.separator(point).length(); i++) {
        sb.deleteCharAt(sb.length() - 1);
      }
    }
    sb.append(style.end(point));
    return sb.toString();
  }
}
