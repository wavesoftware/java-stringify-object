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
import pl.wavesoftware.eid.utils.UnsafeSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class MethodInfoImpl<T> implements MethodInfo<T> {

  private final Supplier<Method> methodSupplier;

  @Override
  @SuppressWarnings("unchecked")
  public T invoke(@Nullable Object target, Object... parameters) {
    return tryToExecute(new UnsafeSupplier<T>() {
        @Override
        @Nonnull
        public T get() throws InvocationTargetException, IllegalAccessException {
          return (T) methodSupplier.get().invoke(target, parameters);
        }
      },
      "20180418:231314"
    );
  }
}
