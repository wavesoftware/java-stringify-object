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

package pl.wavesoftware.utils.stringify.impl.jpa;

import pl.wavesoftware.utils.stringify.impl.lang.LangModule;
import pl.wavesoftware.utils.stringify.impl.reflect.ClassInfo;
import pl.wavesoftware.utils.stringify.impl.reflect.MethodInfo;
import pl.wavesoftware.utils.stringify.impl.reflect.ReflectModule;
import pl.wavesoftware.utils.stringify.spi.JpaLazyChecker;

import java.util.function.Supplier;


/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
final class HibernateLazyChecker implements JpaLazyChecker {
  private static final ClassInfo<?> HIBERNATE = ReflectModule.INSTANCE
    .classLocator().locate("org.hibernate.Hibernate");
  private static Supplier<MethodInfo<Boolean>> isInitializedMethod =
    LangModule.INSTANCE.lazy(() ->
      ReflectModule.INSTANCE.methodLocator()
        .locate(HIBERNATE, "isInitialized", Object.class)
    );

  @Override
  public boolean isSuitable(Object candidate) {
    return HIBERNATE.isAvailable();
  }

  @Override
  public boolean isLazy(Object candidate) {
    return !isInitializedMethod.get().invoke(null, candidate);
  }

}
