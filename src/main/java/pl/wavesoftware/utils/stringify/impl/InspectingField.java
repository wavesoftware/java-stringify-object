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

import pl.wavesoftware.utils.stringify.spi.Masker;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
interface InspectingField {
  /**
   * Gets a name of a field
   *
   * @return a name
   */
  String getName();

  /**
   * Get field representation of inspecting field
   * @return a field
   */
  Field getFieldReflection();

  /**
   * Get object that contains this inspecting field
   *
   * @return an object
   */
  Object getContainingObject();

  /**
   * True if should inspect given field
   *
   * @param inspectionPoint a inspection point to test
   * @return true, if should inspect
   */
  boolean shouldInspect(FieldInspectionPoint inspectionPoint);

  /**
   * Show null if true
   *
   * @return true - null will be shown
   */
  boolean showNull();

  /**
   * Provides a masker for a field if it is configured. Should validate do a type of a
   * field and type of a master match.
   *
   * @param <T> a type of the masker
   * @return a masker
   */
  <T> Optional<Masker<T>> masker();

}
