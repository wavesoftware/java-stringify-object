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

import pl.wavesoftware.utils.stringify.impl.inspector.InspectionContext;
import pl.wavesoftware.utils.stringify.impl.inspector.RootInpector;
import pl.wavesoftware.utils.stringify.spi.theme.Theme;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class DefaultInspectionContext implements InspectionContext {
  private static final Object CONTAIN = new Object();

  private final Map<Object, Object> resolved = new IdentityHashMap<>();
  private final Theme theme;

  private RootInpector rootInpector;

  DefaultInspectionContext(Theme theme) {
    this.theme = theme;
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
    return theme;
  }

  public void rootInpector(RootInpector rootInpector) {
    this.rootInpector = rootInpector;
  }
}
