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
 * Creates instances of classes based on a class. This is usually implemented by
 * some DI container like Spring.
 * <p>
 * Implementations can also implement optional interface {@link BootingAware} hinting
 * library about readiness of this {@code BeanFactory}.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public interface BeanFactory {
  /**
   * Creates an instance of a class
   *
   * @param contractClass a contract class to be created
   * @param <T> a type of a class
   * @return an instance of a contract class
   */
  <T> T create(Class<T> contractClass);
}
