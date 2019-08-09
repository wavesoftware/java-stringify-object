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

import pl.wavesoftware.utils.stringify.api.InspectionContext;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.impl.inspector.RootInpector;
import pl.wavesoftware.utils.stringify.impl.inspector.StringifierContext;
import pl.wavesoftware.utils.stringify.spi.theme.Theme;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class DefaultStringifierContext implements StringifierContext {
  private static final Object CONTAIN = new Object();

  private final Map<Object, Object> resolved = new IdentityHashMap<>();
  private final Supplier<Theme> theme;
  private final InspectionContext inspectionContext;

  private RootInpector rootInpector;

  DefaultStringifierContext(Supplier<Theme> theme, InspectionPoint inspectionPoint) {
    this.theme = theme;
    this.inspectionContext = inspectionPoint.getContext();
  }

  @Override
  public boolean wasInspected(Object object) {
    return resolved.containsKey(object);
  }

  @Override
  public void markAsInspected(Object object) {
    resolved.put(object, CONTAIN);
  }

  @Override
  public RootInpector rootInspector() {
    return rootInpector;
  }

  @Override
  public Theme theme() {
    return theme.get();
  }

  @Override
  public InspectionContext inspectionContext() {
    return inspectionContext;
  }

  void rootInpector(RootInpector rootInpector) {
    this.rootInpector = rootInpector;
  }
}
