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

import pl.wavesoftware.utils.stringify.api.Configuration;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import java.util.function.Function;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
final class ToStringResolverImpl implements ToStringResolver, Configuration {

  private final DefaultConfiguration configuration;
  private final Inspector inspector;

  /**
   * A default constructor
   *
   * @param target        a target object to resolve
   * @param configuration a configuration
   */
  ToStringResolverImpl(Object target, DefaultConfiguration configuration) {
    this(
      target,
      configuration,
      new DefaultInspectionContext(),
      new BeanFactoryCache(() ->
        new FallbackBootFactory(
          new BootAwareBootFactory(configuration.getBeanFactory(), target)
        )
      ),
      new InspectingFieldFactory(configuration::getMode)
    );
  }

  ToStringResolverImpl(
    Object target,
    DefaultConfiguration configuration,
    InspectionContext inspectionContext,
    BeanFactoryCache beanFactoryCache,
    InspectingFieldFactory inspectingFieldFactory
  ) {
    this.configuration = configuration;
    this.inspector = new Inspector(
      configuration, target, inspectionContext, new ObjectInspectorImpl(),
      beanFactoryCache, inspectingFieldFactory
    );
  }

  @Override
  public CharSequence resolve() {
    return inspector.resolve();
  }

  @Override
  public Configuration mode(Mode mode) {
    inspector.clear();
    return configuration.mode(mode);
  }

  @Override
  public Configuration beanFactory(BeanFactory beanFactory) {
    inspector.clear();
    return configuration.beanFactory(beanFactory);
  }

  private final class ObjectInspectorImpl implements Function<Object, CharSequence> {

    @Override
    public CharSequence apply(Object object) {
      return inspector.inspectObject(object);
    }

  }

}
