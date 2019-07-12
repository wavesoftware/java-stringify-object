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

package pl.wavesoftware.utils.stringify.impl.reflect;

import pl.wavesoftware.eid.exceptions.EidIllegalStateException;

import java.util.Optional;

/**
 * Represents a information about a given class
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 * @param <T> a type of a class
 */
public interface ClassInfo<T> {
  /**
   * Provides a optional class reference. Reference is {@link Optional#empty()} if target
   * class isn't found.
   *
   * @return an optional class reference
   */
  Optional<Class<T>> maybeClass();

  default Class<T> get() {
    return maybeClass()
      .orElseThrow(() -> new EidIllegalStateException("20190712:012032"));
  }

  default boolean isAvailable() {
    return maybeClass().isPresent();
  }
}
