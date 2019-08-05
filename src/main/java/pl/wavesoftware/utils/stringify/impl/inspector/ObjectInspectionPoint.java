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

package pl.wavesoftware.utils.stringify.impl.inspector;

import pl.wavesoftware.utils.stringify.api.InspectionContext;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.impl.lang.LangModule;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
final class ObjectInspectionPoint implements InspectionPoint {
  private final Object target;
  private final Supplier<InspectionContext> contextSupplier;

  ObjectInspectionPoint(Object target, Supplier<InspectionContext> contextSupplier) {
    this.target = target;
    this.contextSupplier = LangModule.INSTANCE.lazy(contextSupplier);
  }

  @Override
  public Supplier<Object> getValue() {
    return () -> target;
  }

  @Override
  public Supplier<Class<?>> getType() {
    return target::getClass;
  }

  @Override
  public InspectionContext getContext() {
    return contextSupplier.get();
  }
}
