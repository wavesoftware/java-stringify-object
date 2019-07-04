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

import java.util.Map;
import java.util.function.Function;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class MapInspector implements ObjectInspector {

  @Override
  public boolean consentTo(Object candidate,
                           InspectionContext inspectionContext) {
    return candidate instanceof Map;
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    Map<?, ?> map = (Map<?, ?>) object;
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();
      sb.append(alternative.apply(key));
      sb.append(": ");
      sb.append(alternative.apply(value));
      sb.append(", ");
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
}
