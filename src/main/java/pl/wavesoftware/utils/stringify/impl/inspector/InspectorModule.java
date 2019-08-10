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
import pl.wavesoftware.utils.stringify.api.Namespace;
import pl.wavesoftware.utils.stringify.api.Store;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2.0.0
 */
public enum InspectorModule {
  INSTANCE;

  public Iterable<ObjectInspector> inspectors() {
    return Arrays.asList(
      new CharSequenceInspector(),
      new PrimitiveInspector(),
      new CharacterInspector(),
      new JpaLazyInspector(),
      new MapInspector(),
      new IterableInspector(),
      new RecursionInspector()
    );
  }

  public InspectionPoint objectInspectionPoint(
    Object target, Supplier<InspectionContext> contextSupplier
  ) {
    return new ObjectInspectionPoint(target, contextSupplier);
  }

  public InspectionContext inspectionContext(Function<Namespace, Store> storeResolver) {
    return new InspectionContextImpl(storeResolver);
  }
}
