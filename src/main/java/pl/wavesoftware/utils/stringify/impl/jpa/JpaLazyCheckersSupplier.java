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
import pl.wavesoftware.utils.stringify.impl.lang.LangModule;
import pl.wavesoftware.utils.stringify.spi.JpaLazyChecker;

import java.util.Collections;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class JpaLazyCheckersSupplier implements Supplier<JpaLazyCheckers> {
  private static final Supplier<Set<JpaLazyChecker>> IMPLS = LangModule.INSTANCE.lazy(
    JpaLazyCheckersSupplier::computeImplementations
  );

  private final JpaLazyChecker fallback;

  @Override
  public JpaLazyCheckers get() {
    return new JpaLazyCheckersImpl(IMPLS, fallback);
  }

  private static Set<JpaLazyChecker> computeImplementations() {
    Set<JpaLazyChecker> impls = new HashSet<>();
    impls.add(new HibernateLazyChecker());
    ServiceLoader<JpaLazyChecker> serviceLoader = ServiceLoader.load(JpaLazyChecker.class);
    for (JpaLazyChecker checker : serviceLoader) {
      impls.add(checker);
    }
    return Collections.unmodifiableSet(impls);
  }
}
