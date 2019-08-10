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

package pl.wavesoftware.utils.stringify.api;

import java.util.function.Supplier;

/**
 * Represents a inspection point in some object, that is a field with value.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public interface InspectionPoint {
  /**
   * Get a field value supplier
   *
   * @return a supplier of a field value
   */
  Supplier<Object> getValue();

  /**
   * Get a type of a field
   * @return a type of a field
   */
  Supplier<Class<?>> getType();

  /**
   * Gets a context object associated with this inspection point
   *
   * @return a context object
   */
  InspectionContext getContext();
}
