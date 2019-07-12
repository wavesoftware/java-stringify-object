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
import pl.wavesoftware.utils.stringify.impl.beans.BeansModule;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class DefaultConfiguration implements Configuration {
  private static final BeanFactory DEFAULT_BEAN_FACTORY =
    BeansModule.INSTANCE.defaultBeanFactory();

  private Mode mode = Mode.DEFAULT_MODE;
  private BeanFactory beanFactory = DEFAULT_BEAN_FACTORY;

  @Override
  public Configuration mode(Mode mode) {
    this.mode = mode;
    return this;
  }

  @Override
  public Configuration beanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
    return this;
  }

  DefaultConfiguration dup() {
    DefaultConfiguration dup = new DefaultConfiguration();
    dup.beanFactory = beanFactory;
    dup.mode = mode;
    return dup;
  }

  Mode getMode() {
    return mode;
  }

  BeanFactory getBeanFactory() {
    return beanFactory;
  }
}
