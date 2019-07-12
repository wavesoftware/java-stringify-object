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

import pl.wavesoftware.eid.utils.UnsafeSupplier;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class MethodLocatorImpl implements MethodLocator {
  @Override
  public <T, R> MethodInfo<T> locate(
    ClassInfo<R> classInfo,
    String methodName,
    Class<?>... parametersTypes
  ) {
    return new MethodInfoImpl<>(() ->
      tryToExecute(new UnsafeSupplier<Method>() {
        @Override
        @Nonnull
        public Method get() throws NoSuchMethodException {
          return classInfo.get().getDeclaredMethod(methodName, parametersTypes);
        }
      }, "20180418:231029")
    );
  }
}
