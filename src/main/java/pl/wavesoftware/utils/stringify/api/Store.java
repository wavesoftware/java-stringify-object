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

import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface Store extends Iterable<Entry<Object, Object>> {
  /**
   * Puts a value on a key
   *
   * @param key   a key
   * @param value a value
   */
  void put(Object key, Object value);

  /**
   * Gets a value set for a given key
   *
   * @param key          a key
   * @param <T>          a type of value
   * @param requiredType a type of value
   * @return a value, or absent
   */
  <T> Optional<T> get(Object key, Class<T> requiredType);
}
