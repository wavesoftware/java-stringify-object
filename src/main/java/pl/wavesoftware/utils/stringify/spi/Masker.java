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

package pl.wavesoftware.utils.stringify.spi;

import javax.annotation.Nullable;

/**
 * A masker should mask out a field to a safe representation.
 *
 * <p>
 * For example given a phone
 * masker and a phone value <code>"01 555-345-242"</code> a masked result could be
 * <code>"********242"</code>
 *
 * @param <T> a type of this masker
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface Masker<T> {

  /**
   * A type this masker supports. Value will be casted to this type, so this must match
   * a field type.
   *
   * @return a type of field this masker supports
   */
  Class<T> type();

  /**
   * Mask a field value to safe representation. A value can be {@code null}.
   *
   * @param target a target field to be masked
   * @return masked value
   */
  CharSequence mask(@Nullable T target);
}
