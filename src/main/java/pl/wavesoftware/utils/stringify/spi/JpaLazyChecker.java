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

package pl.wavesoftware.utils.stringify.spi;

/**
 * Checks a field, if it isn't loaded from persistence. If checker returns {@code true},
 * Stringify shouldn't inspect that field, but will display a lazy indicating
 * representation, for ex.: {@code "⁂Lazy"}.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public interface JpaLazyChecker {
  /**
   * Checks if given object is lazy
   *
   * @param candidate a candidate to be checked
   * @return true, if object is lazy
   */
  boolean isLazy(Object candidate);

  /**
   * Returns {@code true}, if this checker is suitable to check given candidate.
   *
   * @param candidate a candidate object to be checked.
   * @return true, if this checker can be used to determine if candidate is lazy.
   */
  default boolean isSuitable(Object candidate) {
    return true;
  }
}
