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

import pl.wavesoftware.utils.stringify.spi.theme.IterableStyle;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class IterableInspector implements ObjectInspector {
  @Override
  public boolean consentTo(Object candidate, InspectionContext context) {
    return candidate instanceof Iterable;
  }

  @Override
  public CharSequence inspect(Object object, InspectionContext context) {
    return inspectIterable((Iterable<?>) object, context);
  }

  private static CharSequence inspectIterable(
    Iterable<?> iterable, InspectionContext context
  ) {
    IterableStyle style = context.theme().iterable();
    StringBuilder sb = new StringBuilder();
    sb.append(style.begin());
    for (Object elem : iterable) {
      sb.append(context.rootInspector().apply(elem));
      sb.append(style.separator());
    }
    if (sb.length() > style.begin().length()) {
      sb.deleteCharAt(sb.length() - style.separator().length());
    }
    sb.append(style.end());
    return sb.toString();
  }
}
