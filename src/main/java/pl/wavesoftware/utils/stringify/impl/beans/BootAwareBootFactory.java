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
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.impl.lang.Inspectable;
import pl.wavesoftware.utils.stringify.impl.lang.LangModule;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;
import pl.wavesoftware.utils.stringify.spi.BootingAware;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class BootAwareBootFactory implements BeanFactory, Inspectable {
  private static final Logger LOGGER =
    LoggerFactory.getLogger(BootAwareBootFactory.class);
  private final Supplier<BeanFactory> delegateSupplier;
  private final InspectionPoint inspectionPoint;

  BootAwareBootFactory(
    Supplier<BeanFactory> delegateSupplier,
    InspectionPoint inspectionPoint
  ) {
    this.delegateSupplier = delegateSupplier;
    this.inspectionPoint = inspectionPoint;
  }

  @Override
  public <T> T create(Class<T> contractClass) {
    return getBeanFactory(contractClass).create(contractClass);
  }

  private BeanFactory getBeanFactory(Class<?> contractClass) {
    BeanFactory delegate = delegateSupplier.get();
    if (delegate instanceof BootingAware) {
      BootingAware bootingAware = (BootingAware) delegate;
      if (!bootingAware.isReady() && LOGGER.isWarnEnabled()) {
        LOGGER.error(
          "Given BeanFactory {} is reporting that it's not ready, but Stringify" +
            " has been already invoked for {} while trying to resolve {}. This is " +
            "usually some race condition issues. Using default BeanFactory as a " +
            "fallback for this call.",
          safeInspect(delegate),
          safeInspect(inspectionPoint.getValue().get()),
          contractClass
        );
        return BeansModule.INSTANCE.defaultBeanFactory();
      }
    }
    return delegate;
  }

  @Override
  public String inspect() {
    return safeInspect(delegateSupplier);
  }

  private static String safeInspect(Object object) {
    return LangModule.INSTANCE.safeInspector().inspect(object);
  }
}
