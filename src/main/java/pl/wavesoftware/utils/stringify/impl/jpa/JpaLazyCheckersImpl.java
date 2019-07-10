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
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.spi.JpaLazyChecker;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class JpaLazyCheckersImpl implements JpaLazyCheckers {
  private final Supplier<Set<JpaLazyChecker>> checkersSupplier;
  private final JpaLazyChecker fallback;

  @Override
  public boolean isLazy(Object candidate) {
    Set<JpaLazyChecker> checkers = checkersSupplier.get();
    return checkers.stream()
      .filter(checker -> checker.isSuitable(candidate))
      .map(checker -> checker.isLazy(candidate))
      .reduce((a, b) ->  a || b)
      .orElseGet(() -> fallback.isLazy(candidate));
  }
}
