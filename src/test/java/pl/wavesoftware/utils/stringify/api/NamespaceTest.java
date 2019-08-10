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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
class NamespaceTest {

  @Test
  void equalsToItself() {
    // given
    Namespace ns = Namespace.GLOBAL;

    // then
    assertThat(ns).isEqualTo(Namespace.GLOBAL);
  }

  @Test
  void doNotEqualNull() {
    // then
    assertThat(Namespace.GLOBAL).isNotEqualTo(null);
  }

  @Test
  void doNotEqualOtherType() {
    // then
    assertThat(Namespace.GLOBAL).isNotEqualTo("test");
  }

  @Test
  void equalsByParts() {
    // when
    Namespace ns1 = Namespace.create("alice", true, "bob");
    Namespace ns2 = Namespace.create("alice", true, "bob");

    // then
    assertThat(ns1).isEqualTo(ns2);
  }
}
