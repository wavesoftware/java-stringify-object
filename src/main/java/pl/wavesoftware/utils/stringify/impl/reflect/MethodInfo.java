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

import javax.annotation.Nullable;

/**
 * Represents a method that can be invoked
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 * @param <T> a type of method returns
 */
public interface MethodInfo<T> {
  /**
   * Invokes a method with parameters given.
   *
   * @param target     a target object, if {@code null} method will be called statically.
   * @param parameters a parameters for a method
   * @return a method return value
   */
  T invoke(@Nullable Object target, Object... parameters);
}
