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

import pl.wavesoftware.utils.stringify.spi.BeanFactory;
import pl.wavesoftware.utils.stringify.spi.Configurator;
import pl.wavesoftware.utils.stringify.spi.theme.Theme;

/**
 * A interface that represents a configuration options for this library
 * <p>
 * To configure this library you either use:
 * <ul>
 *     <li>declarative way - using Java's {@link java.util.ServiceLoader ServiceLoader}
 *     mechanism,</li>
 *     <li>or programmatic way.</li>
 * </ul>
 *
 * <p>
 * On how to use declarative way consult Javadoc of {@link Configurator}.
 * <p>
 * To use programmatic way, just invoke configuration methods on instance of
 * {@link pl.wavesoftware.utils.stringify.Stringify Stringify} object.
 * <p>
 * {@link BeanFactory} interface can be used to create instances of classes, declared
 * in annotations used to define inspection rules.
 *
 * @see Configurator
 * @see BeanFactory
 * @see pl.wavesoftware.utils.stringify.Stringify Stringify
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface Configuration {
  /**
   * Configures a mode in which resolver operates
   *
   * @param mode a mode to set
   * @return a self reference
   */
  Configuration mode(Mode mode);

  /**
   * Configures a predicate factory to be used to load implementation of predicates used
   *
   * @param beanFactory a predicate factory
   * @return a self reference
   */
  Configuration beanFactory(BeanFactory beanFactory);

  /**
   * Configures a user provided theme
   *
   * @param theme a theme provided by user
   * @return a self reference
   */
  Configuration theme(Theme theme);
}
