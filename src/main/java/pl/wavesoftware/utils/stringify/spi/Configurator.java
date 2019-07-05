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

import pl.wavesoftware.utils.stringify.api.Configuration;

import java.util.ServiceLoader;

/**
 * A {@code Configurator} interface is intended to be implemented in user code, and
 * assigned to <a href="https://www.baeldung.com/java-spi">Service Loader</a> mechanism.
 * <p>
 * To do that, create on your classpath, a file:
 *
 * <pre>
 * /META-INF/services/pl.wavesoftware.utils.stringify.spi.Configurator
 * </pre>
 *
 * In that file, place a fully qualified class name of your class that implements
 * {@code Configurator} interface. It should be called first time you use an Stringify
 * to inspect an object:
 *
 * <pre>
 * # classpath:/META-INF/services/pl.wavesoftware.utils.stringify.spi.Configurator
 * org.acmecorp.StringifyConfigurator
 * </pre>
 *
 * Then implement that class in your code:
 *
 * <pre>
 * package org.acmecorp;
 *
 * import pl.wavesoftware.utils.stringify.api.Configuration;
 * import pl.wavesoftware.utils.stringify.spi.Configurator;
 *
 * public final class StringifyConfigurator implements Configurator {
 *
 *   &#064;Override
 *   public void configure(Configuration configuration) {
 *     configuration.beanFactory(new SpringBeanFactory());
 *   }
 * }
 * </pre>
 *
 * with example Spring based BeanFactory:
 *
 * <pre>
 * package org.acmecorp;
 *
 * import org.springframework.context.event.ContextRefreshedEvent;
 * import org.springframework.context.ApplicationContext;
 * import org.springframework.context.annotation.Configuration;
 *
 * import pl.wavesoftware.utils.stringify.spi.BeanFactory;
 * import pl.wavesoftware.utils.stringify.spi.BootingAware;
 *
 * &#064;Configuration
 * class SpringBeanFactory implements BeanFactory, BootingAware {
 *   private static ApplicationContext context;
 *
 *   &#064;EventListener(ContextRefreshedEvent.class)
 *   void onRefresh(ContextRefreshedEvent event) {
 *     SpringBeanFactory.context = event.getApplicationContext();
 *   }
 *
 *   &#064;Override
 *   public &lt;T&gt; T create(Class&lt;T&gt; contractClass) {
 *     return SpringBeanFactory.context.getBean(contractClass);
 *   }
 *
 *   &#064;Override
 *   public boolean isReady() {
 *     return SpringBeanFactory.context != null;
 *   }
 * }
 * </pre>
 *
 * @see ServiceLoader
 * @see Configuration
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public interface Configurator {
  /**
   * Configures a library with various configuration options
   *
   * @param configuration a configuration
   */
  void configure(Configuration configuration);
}
