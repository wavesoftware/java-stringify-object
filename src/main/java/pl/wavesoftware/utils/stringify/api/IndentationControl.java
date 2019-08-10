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

package pl.wavesoftware.utils.stringify.api;

/**
 * An indentation of output that can be used by
 * {@link pl.wavesoftware.utils.stringify.spi.theme.Theme Theme} elements to prepare
 * pretty print.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface IndentationControl {
  /**
   * Current level of indentation.
   *
   * @return an indentation level
   */
  int current();

  /**
   * Increments current level of indentation.
   */
  void increment();

  /**
   * Decrements current level of indentation.
   */
  void decrement();

  /**
   * Calculates a indent text based on a indent value and current level
   *
   * @param indent a text representation of indent
   * @return a complete indent
   */
  default CharSequence indent(CharSequence indent) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < current(); i++) {
      builder.append(indent);
    }
    return builder;
  }
}
