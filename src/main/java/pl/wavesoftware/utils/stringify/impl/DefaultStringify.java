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

package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.utils.stringify.Stringify;
import pl.wavesoftware.utils.stringify.api.Inspect;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

/**
 * A default implementation of {@link Stringify}.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public final class DefaultStringify implements Stringify {

  private static final DefaultConfiguration CONFIGURATION =
    DefaultConfigurationLoader.load();

  private final ToStringResolverImpl resolver;

  /**
   * A default constructor for Stringify.
   *
   * @param target a target object to inspect
   */
  public DefaultStringify(Object target) {
    resolver = new ToStringResolverImpl(target, CONFIGURATION.dup());
  }

  /**
   * Creates string representation of given object using {@link Inspect} on given fields.
   *
   * @return a string representation of given object
   */
  @Override
  public CharSequence stringify() {
    return resolver.resolve();
  }

  @Override
  public String toString() {
    return stringify().toString();
  }

  @Override
  public int length() {
    return stringify().length();
  }

  @Override
  public char charAt(int index) {
    return stringify().charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return stringify().subSequence(start, end);
  }

  @Override
  public Stringify mode(Mode mode) {
    resolver.mode(mode);
    return this;
  }

  @Override
  public Stringify beanFactory(BeanFactory beanFactory) {
    resolver.beanFactory(beanFactory);
    return this;
  }
}
