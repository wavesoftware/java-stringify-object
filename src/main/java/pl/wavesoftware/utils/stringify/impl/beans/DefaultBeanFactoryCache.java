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

package pl.wavesoftware.utils.stringify.impl.beans;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
@RequiredArgsConstructor
final class DefaultBeanFactoryCache implements BeanFactoryCache {
  private final Map<Class<?>, Object> created = new ConcurrentHashMap<>();
  private final Supplier<BeanFactory> delegate;

  @Override
  public <T> T create(final Class<T> contractClass) {
    if (isCached(contractClass)) {
      return getFromCache(contractClass);
    }
    T predicate = delegate.get().create(contractClass);
    put(contractClass, predicate);
    return predicate;
  }

  @Override
  public void clear() {
    created.clear();
  }

  @SuppressWarnings("unchecked")
  private <T> T getFromCache(final Class<T> contractClass) {
    Object bean = created.get(contractClass);
    return (T) bean;
  }

  private <T> void put(Class<T> contractClass, T bean) {
    created.put(contractClass, bean);
  }

  private boolean isCached(Class<?> contractClass) {
    return created.containsKey(contractClass);
  }
}
