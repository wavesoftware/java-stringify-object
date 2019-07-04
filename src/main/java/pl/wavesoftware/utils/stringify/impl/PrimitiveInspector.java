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

import java.util.Date;
import java.util.function.Function;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class PrimitiveInspector implements ObjectInspector {
  private static final ClassLocator TEMPORAL_CLASS_LOCATOR =
    new ClassLocator("java.time.temporal.Temporal");


  @Override
  public boolean consentTo(Object candidate,
                           InspectionContext inspectionContext) {
    return isPrimitive(candidate);
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    return object.toString();
  }

  private static boolean isPrimitive(Object object) {
    return object instanceof Number
      || object instanceof Boolean
      || object instanceof Enum
      || isDatelike(object);
  }

  private static boolean isDatelike(Object object) {
    return object instanceof Date
      || isInstanceOfTemporal(object);
  }

  private static boolean isInstanceOfTemporal(Object candidate) {
    if (TEMPORAL_CLASS_LOCATOR.isAvailable()) {
      Class<?> temporalCls = TEMPORAL_CLASS_LOCATOR.get();
      return temporalCls.isInstance(candidate);
    }
    return false;
  }
}
