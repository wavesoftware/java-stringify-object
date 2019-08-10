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

/**
 * Locates a method withing a given class info
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface MethodLocator {
  /**
   * Return a method info if described method exists, throws runtime exception otherwise.
   *
   * @param classInfo       a class info
   * @param methodName      a method name
   * @param parametersTypes parameter types for a method
   * @param <T>             method return type
   * @param <R>             class info type
   * @return a method info
   */
  <T, R> MethodInfo<T> locate(ClassInfo<R> classInfo, String methodName, Class<?>... parametersTypes);
}
