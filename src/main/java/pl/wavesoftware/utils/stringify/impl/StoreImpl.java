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

import pl.wavesoftware.utils.stringify.api.Store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class StoreImpl implements Store {
  private final Map<Object, Object> map = new HashMap<>();

  StoreImpl() {
    // nothing
  }

  StoreImpl(Store store) {
    for (Entry<Object, Object> entry : store) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void put(Object key, Object value) {
    map.put(key, value);
  }

  @Override
  public <T> Optional<T> get(Object key, Class<T> requiredType) {
    T obj = requiredType.cast(map.get(key));
    return Optional.ofNullable(obj);
  }

  @Override
  public Iterator<Entry<Object, Object>> iterator() {
    return map.entrySet().iterator();
  }
}
