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

package pl.wavesoftware.utils.stringify.impl.jpa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wavesoftware.utils.stringify.impl.lang.LangModule;
import pl.wavesoftware.utils.stringify.spi.JpaLazyChecker;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class JpaLazyCheckerFacade implements JpaLazyChecker {
  private static final JpaLazyChecker NOOP_CHECKER = candidate -> false;
  private static final Supplier<JpaLazyCheckers> CHECKERS =
    LangModule.INSTANCE.lazy(new JpaLazyCheckersSupplier(NOOP_CHECKER));

  @Override
  public boolean isLazy(Object candidate) {
    return CHECKERS.get().isLazy(candidate);
  }
}
