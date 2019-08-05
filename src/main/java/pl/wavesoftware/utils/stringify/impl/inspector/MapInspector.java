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
import pl.wavesoftware.utils.stringify.spi.theme.MapStyle;

import java.util.Map;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class MapInspector implements ObjectInspector {

  @Override
  public boolean consentTo(InspectionPoint point, StringifierContext context) {
    return Map.class.isAssignableFrom(point.getType().get());
  }

  @Override
  public CharSequence inspect(InspectionPoint point, StringifierContext context) {
    Map<?, ?> map = (Map<?, ?>) point.getValue().get();
    MapStyle style = context.theme().map();
    StringBuilder sb = new StringBuilder();
    sb.append(style.begin(point));
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object key = entry.getKey();
      InspectionPoint keyPoint =
        InspectorModule.INSTANCE.objectInspectionPoint(key, point::getContext);
      Object value = entry.getValue();
      InspectionPoint valuePoint =
        InspectorModule.INSTANCE.objectInspectionPoint(value, point::getContext);
      sb.append(context.rootInspector().apply(keyPoint));
      sb.append(style.entryEquals(point));
      sb.append(context.rootInspector().apply(valuePoint));
      sb.append(style.separator(point));
    }
    if (!map.isEmpty()) {
      for (int i=0; i < style.separator(point).length(); i++) {
        sb.deleteCharAt(sb.length() - 1);
      }
    }
    sb.append(style.end(point));
    return sb.toString();
  }
}
