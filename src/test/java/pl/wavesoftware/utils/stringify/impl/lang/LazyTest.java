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

package pl.wavesoftware.utils.stringify.impl.lang;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
class LazyTest {

  @Test
  void of() {
    // given
    Supplier<Integer> supplier = () -> 42;
    Lazy<Integer> lazy1 = Lazy.of(supplier);
    Lazy<Integer> lazy2 = Lazy.of(lazy1);

    // when
    String toString1 = lazy1.toString();
    String toString2 = lazy2.toString();
    Integer result = lazy2.get();
    String toString3 = lazy1.toString();
    String toString4 = lazy2.toString();

    // then
    assertThat(result).isEqualTo(42);
    assertThat(toString1).isEqualTo("Lazy(?)");
    assertThat(toString2).isEqualTo("Lazy(?)");
    assertThat(toString3).isEqualTo("Lazy(42)");
    assertThat(toString4).isEqualTo("Lazy(42)");
  }

  @Test
  void equals1() {
    // given
    Supplier<Integer> supplier = () -> 42;
    Lazy<Integer> lazy1 = Lazy.of(supplier);
    Lazy<Integer> lazy2 = Lazy.of(lazy1);

    // when
    boolean result = lazy1.equals(lazy2);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void hashCode1() {
    // given
    Supplier<Integer> supplier = () -> 42;
    Lazy<Integer> lazy1 = Lazy.of(supplier);
    Lazy<Integer> lazy2 = Lazy.of(lazy1);

    // when
    int result1 = lazy1.hashCode();
    int result2 = lazy2.hashCode();

    // then
    assertThat(result1).isEqualTo(result2);
  }

  @Test
  void serialize() throws IOException {
    // given
    Supplier<Integer> supplier = () -> 42;
    Lazy<Integer> lazy = Lazy.of(supplier);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      // when
      oos.writeObject(lazy);
      byte[] bytes = baos.toByteArray();
      String result = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

      // then
      assertThat(result).isEqualTo(
        "rO0ABXNyAC5wbC53YXZlc29mdHdhcmUudXRpbHMuc3RyaW5naWZ5LmltcGwubGFuZy5MYXp5AAA" +
          "SXQQND_YDAAFMAAV2YWx1ZXQAEkxqYXZhL2xhbmcvT2JqZWN0O3hwc3IAEWphdmEubGFuZy5Jbn" +
          "RlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4c" +
          "AAAACp4"
      );
    }
  }

}
