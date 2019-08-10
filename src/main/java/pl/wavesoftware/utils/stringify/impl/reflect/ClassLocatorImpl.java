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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ClassLocatorImpl implements ClassLocator {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassLocatorImpl.class);

  private final ClassLoader classLoader;

  @Override
  @SuppressWarnings({"squid:S1166", "squid:S2658", "unchecked"})
  public <T> ClassInfo<T> locate(String fqcn) {
    try {
      Class<T> targetClass = (Class<T>) Class.forName(fqcn, false, classLoader);
      return new ClassInfoImpl<>(targetClass);
    } catch (ClassNotFoundException ex) {
      LOGGER.trace("Silenced exception", ex);
      return ClassInfoImpl.empty();
    }
  }
}
