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

package pl.wavesoftware.utils.stringify.impl.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import pl.wavesoftware.utils.stringify.impl.lang.LangModule;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import javax.annotation.Nullable;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkState;

final class FallbackBootFactory implements BeanFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(FallbackBootFactory.class);
  private static final BeanFactory DEFAULT = new DefaultBeanFactory();
  private final BeanFactory beanFactory;

  FallbackBootFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public <T> T create(Class<T> contractClass) {
    try {
      @Nullable T bean = beanFactory.create(contractClass);
      checkState(bean != null, "20190705:114553");
      return bean;
    } catch (RuntimeException ex) {
      LOGGER.error(
        MessageFormatter.format(
          "Configured BeanFactory {} thrown an exception while trying to " +
            "resolve {}. Fallback to default BeanFactory.",
          LangModule.INSTANCE.safeInspector().inspect(beanFactory), contractClass
        ).getMessage(), ex
      );
      return DEFAULT.create(contractClass);
    }
  }
}
