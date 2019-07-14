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

package pl.wavesoftware.utils.stringify.impl.theme;

import pl.wavesoftware.utils.stringify.spi.theme.ComplexObjectStyle;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class DefaultComplexObjectStyle implements ComplexObjectStyle {
  @Override
  public CharSequence begin() {
    return "<";
  }

  @Override
  public CharSequence name(Supplier<Class<?>> type, Supplier<Integer> hashCode) {
    return type.get().getSimpleName();
  }

  @Override
  public CharSequence end() {
    return ">";
  }

  @Override
  public CharSequence nameSeparator() {
    return " ";
  }

  @Override
  public CharSequence propertyEquals() {
    return "=";
  }

  @Override
  public CharSequence propertySeparator() {
    return ", ";
  }
}
