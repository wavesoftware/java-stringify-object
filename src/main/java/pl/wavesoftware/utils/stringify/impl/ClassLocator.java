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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkNotNull;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ClassLocator {

  private final String fqcn;
  private Class<?> targetClass;
  private boolean located;

  boolean isAvailable() {
    return getTargetClass() != null;
  }

  Class<?> get() {
    return checkNotNull(getTargetClass(), "20180418:230411");
  }

  @SuppressWarnings({"squid:S1166", "squid:S2658"})
  @Nullable
  private Class<?> getTargetClass() {
    if (!located) {
      ClassLoader cl = Thread
        .currentThread()
        .getContextClassLoader();
      try {
        targetClass = Class.forName(fqcn, false, cl);
      } catch (ClassNotFoundException e) {
        // do nothing
      }
      located = true;
    }
    return targetClass;
  }
}
