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

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
final class DefaultInspectionContext implements InspectionContext {
  private static final Object CONTAIN = new Object();

  private final Map<Object, Object> resolved;

  DefaultInspectionContext() {
    this(new IdentityHashMap<>());
  }

  private DefaultInspectionContext(Map<Object, Object> resolved) {
    this.resolved = resolved;
  }

  @Override
  public boolean wasInspected(Object object) {
    return resolved.containsKey(object);
  }

  @Override
  public void markIsInspected(Object object) {
    resolved.put(object, CONTAIN);
  }
}
