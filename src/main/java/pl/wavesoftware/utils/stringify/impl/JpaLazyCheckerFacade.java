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
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class JpaLazyCheckerFacade implements JpaLazyChecker {
  private static final JpaLazyChecker NOOP_CHECKER = candidate -> false;

  private JpaLazyChecker lazyChecker;

  @Override
  public boolean isLazy(Object candidate) {
    JpaLazyChecker checker = getImpl();
    return checker.isLazy(candidate);
  }

  private JpaLazyChecker getImpl() {
    if (lazyChecker == null) {
      lazyChecker = resolveImpl();
    }
    return lazyChecker;
  }

  private static JpaLazyChecker resolveImpl() {
    if (HibernateLazyChecker.isSuitable()) {
      return new HibernateLazyChecker();
    }
    return NOOP_CHECKER;
  }
}
