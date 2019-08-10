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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class Lazy<T> implements Supplier<T>, Serializable {
  private static final long serialVersionUID = 20190709223414L;

  /**
   * @see <a href="http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html">
   *     Doublde checked locking</a>
   */
  private transient volatile Supplier<? extends T> supplier;

  /**
   * Will behave as a volatile in reality, because a supplier volatile read will update
   * all fields.
   *
   * @see <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#volatile">
   *     What does volatile do?</a>
   */
  private T value;

  // should not be called directly
  private Lazy(Supplier<? extends T> supplier) {
    this.supplier = supplier;
  }

  /**
   * Creates a {@code Lazy} that requests its value from a given {@code Supplier}.
   * The supplier is asked only once, the value is memoized.
   *
   * @param <T>      type of the lazy value
   * @param supplier A supplier
   * @return A new instance of Lazy
   */
  @SuppressWarnings("unchecked")
  static <T> Lazy<T> of(Supplier<? extends T> supplier) {
    Objects.requireNonNull(supplier, "supplier is null");
    if (supplier instanceof Lazy) {
      return (Lazy<T>) supplier;
    } else {
      return new Lazy<>(supplier);
    }
  }

  /**
   * Evaluates this lazy value and caches it, when called the first time.
   * On subsequent calls, returns the cached value.
   *
   * @return the lazy evaluated value
   */
  @Override
  public T get() {
    return (supplier == null) ? value : computeValue();
  }

  private synchronized T computeValue() {
    final Supplier<? extends T> s = supplier;
    if (s != null) {
      value = s.get();
      supplier = null;
    }
    return value;
  }

  /**
   * Checks, if this lazy value is evaluated.
   * <p>
   * Note: A value is internally evaluated (once) by calling {@link #get()}.
   *
   * @return true, if the value is evaluated, false otherwise.
   * @throws UnsupportedOperationException if this value is undefined
   */
  private boolean isEvaluated() {
    return supplier == null;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj == this) || (obj instanceof Lazy && Objects.equals(((Lazy<?>) obj).get(), get()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(get());
  }

  @Override
  public String toString() {
    return "Lazy(" + (!isEvaluated() ? "?" : value) + ")";
  }

  /**
   * Ensures that the value is evaluated before serialization.
   *
   * @param stream An object serialization stream.
   * @throws java.io.IOException If an error occurs writing to the stream.
   */
  private void writeObject(ObjectOutputStream stream) throws IOException {
    // evaluates the lazy value if it isn't evaluated yet!
    get();
    stream.defaultWriteObject();
  }
}
