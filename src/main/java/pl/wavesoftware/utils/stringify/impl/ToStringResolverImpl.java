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
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.api.Namespace;
import pl.wavesoftware.utils.stringify.api.Store;
import pl.wavesoftware.utils.stringify.impl.beans.BeanFactoryCache;
import pl.wavesoftware.utils.stringify.impl.beans.BeansModule;
import pl.wavesoftware.utils.stringify.impl.inspector.StringifierContext;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;
import pl.wavesoftware.utils.stringify.spi.theme.Theme;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
final class ToStringResolverImpl implements ToStringResolver, Configuration {

  private final DefaultConfiguration configuration;
  private final InspectorBasedToStringResolver delegateResolver;

  /**
   * A default constructor
   *
   * @param inspectionPoint a inspection point to resolve
   * @param configuration   a configuration
   */
  ToStringResolverImpl(InspectionPoint inspectionPoint, DefaultConfiguration configuration) {
    this(
      inspectionPoint,
      configuration,
      new DefaultStringifierContext(configuration::getTheme, inspectionPoint),
      BeansModule.INSTANCE.cachedBeanFactory(
        configuration::getBeanFactory, inspectionPoint
      ),
      new InspectingFieldFactory(configuration::getMode)
    );
  }

  ToStringResolverImpl(
    InspectionPoint inspectionPoint,
    DefaultConfiguration configuration,
    StringifierContext stringifierContext,
    BeanFactoryCache beanFactoryCache,
    InspectingFieldFactory inspectingFieldFactory
  ) {
    this.configuration = configuration;
    this.delegateResolver = new InspectorBasedToStringResolver(
      configuration, inspectionPoint, stringifierContext,
      beanFactoryCache, inspectingFieldFactory
    );
  }

  private ToStringResolverImpl(
    InspectionPoint inspectionPoint,
    DefaultConfiguration configuration,
    DefaultStringifierContext inspectionContext,
    BeanFactoryCache beanFactoryCache,
    InspectingFieldFactory inspectingFieldFactory
  ) {
    this(
      inspectionPoint, configuration, (StringifierContext) inspectionContext,
      beanFactoryCache, inspectingFieldFactory
    );
    inspectionContext.rootInpector(delegateResolver::inspectObject);
  }

  @Override
  public CharSequence resolve() {
    return delegateResolver.resolve();
  }

  @Override
  public Configuration mode(Mode mode) {
    delegateResolver.clear();
    return configuration.mode(mode);
  }

  @Override
  public Configuration beanFactory(BeanFactory beanFactory) {
    delegateResolver.clear();
    return configuration.beanFactory(beanFactory);
  }

  @Override
  public Configuration theme(Theme theme) {
    delegateResolver.clear();
    return configuration.theme(theme);
  }

  @Override
  public Configuration store(Namespace namespace, Consumer<Store> storeConsumer) {
    delegateResolver.clear();
    return configuration.store(namespace, storeConsumer);
  }
}
