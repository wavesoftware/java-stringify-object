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

import pl.wavesoftware.eid.utils.UnsafeSupplier;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import javax.annotation.Nonnull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
final class ReflectionBeanFactory implements BeanFactory {
  @Override
  public <T> T create(Class<T> contractClass) {
    return tryToExecute(new UnsafeSupplier<T>() {
      @Override
      @Nonnull
      public T get() throws IllegalAccessException, InstantiationException,
        InvocationTargetException, NoSuchMethodException {
        Constructor<T> constructor = contractClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
      }
    }, "20180427:171402");
  }
}
