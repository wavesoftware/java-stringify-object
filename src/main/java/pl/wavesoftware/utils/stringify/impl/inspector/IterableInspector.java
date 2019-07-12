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

import java.util.function.Function;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class IterableInspector implements ObjectInspector {
  @Override
  public boolean consentTo(Object candidate, InspectionContext inspectionContext) {
    return candidate instanceof Iterable;
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    return inspectIterable((Iterable<?>) object, alternative);
  }

  private static CharSequence inspectIterable(Iterable<?> iterable,
                                              Function<Object, CharSequence> alternative) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Object elem : iterable) {
      sb.append(alternative.apply(elem));
      sb.append(",");
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
}
