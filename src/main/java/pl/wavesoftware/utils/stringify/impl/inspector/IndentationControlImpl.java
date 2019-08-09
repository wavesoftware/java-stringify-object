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

package pl.wavesoftware.utils.stringify.impl.inspector;

import pl.wavesoftware.utils.stringify.api.IndentationControl;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkState;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class IndentationControlImpl implements IndentationControl {
  private int indent = 0;
  @Override
  public int current() {
    return indent;
  }

  @Override
  public void increment() {
    indent++;
  }

  @Override
  public void decrement() {
    checkState(indent > 0, "20190805:230357");
    indent--;
  }
}
