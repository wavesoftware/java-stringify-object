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

/**
 * Represents a basic object inspector that can handle specific type of object, which
 * is determined by call to {@link #consentTo(Object, InspectionContext)}
 * method.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public interface ObjectInspector {
  /**
   * Determines if this inspector is suitable for a given object
   *
   * @param candidate a candidate to inspect
   * @param context   a context of inspection
   * @return true, if this object inspector can inspect candidate object
   */
  boolean consentTo(Object candidate, InspectionContext context);

  /**
   * Will inspect a given object to character sequence
   *
   * @param object  a object to inspect
   * @param context a context of inspection
   * @return a character sequence representation of given object
   */
  CharSequence inspect(Object object, InspectionContext context);
}
