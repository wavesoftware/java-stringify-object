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

import pl.wavesoftware.eid.utils.UnsafeSupplier;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;


/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
final class HibernateLazyChecker implements JpaLazyChecker {
  private static final ClassLocator HIBERNATE_LOCATOR =
    new ClassLocator("org.hibernate.Hibernate");
  private static Method isInitializedMethod;

  static boolean isSuitable() {
    return HIBERNATE_LOCATOR.isAvailable();
  }

  @Override
  public boolean isLazy(final Object candidate) {
    Class<?> cls = HIBERNATE_LOCATOR.get();
    final Method method = getIsInitializedMethod(cls);
    return !tryToExecute(
      new UnsafeSupplier<Boolean>() {
        @Override
        @Nonnull
        public Boolean get() throws Exception {
          return (Boolean) method.invoke(null, candidate);
        }
      },
      "20180418:231314"
    );
  }

  private static Method getIsInitializedMethod(final Class<?> hibernateClass) {
    if (isInitializedMethod == null) {
      isInitializedMethod = tryToExecute(
        new UnsafeSupplier<Method>() {
          @Override
          @Nonnull
          public Method get() throws Exception {
            return hibernateClass.getDeclaredMethod("isInitialized", Object.class);
          }
        },
        "20180418:231029"
      );
    }
    return isInitializedMethod;
  }
}
