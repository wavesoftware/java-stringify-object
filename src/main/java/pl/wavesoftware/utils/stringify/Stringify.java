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

package pl.wavesoftware.utils.stringify;

import pl.wavesoftware.utils.stringify.api.Configuration;
import pl.wavesoftware.utils.stringify.api.Inspect;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.impl.DefaultStringify;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

/**
 * A utility to safely inspect any Java Object as String representation. It's best to be
 * used with domain model (also with JPA entities) with intention to dump those entities
 * as text to log files.
 * <p>
 * It runs in two modes: {@code PROMISCUOUS} (by default) and {@code QUIET}. In
 * {@code PROMISCUOUS} mode every defined field is automatically inspected, unless the
 * field is annotated with {@code @DoNotInspect} annotation. In {@code QUIET} mode only
 * fields annotated with @Inspect will gets inspected.
 * <p>
 * This library has proper support for object graph cycles, and JPA (Hibernate)
 * lazy loaded elements.
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public interface Stringify extends Configuration, CharSequence {

  /**
   * Creates string representation of given object using {@link Inspect} on given fields.
   *
   * @return a string representation of given object
   */
  CharSequence stringify();

  /**
   * Creates a stringify from a given object
   *
   * @param object an object to inspect
   * @return a stringify
   */
  static Stringify of(Object object) {
    return new DefaultStringify(object);
  }

  @Override
  Stringify mode(Mode mode);

  @Override
  Stringify beanFactory(BeanFactory beanFactory);
}
