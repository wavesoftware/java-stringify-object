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

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.api.DisplayNull;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-30
 */
@RequiredArgsConstructor
final class InspectingFieldImpl implements InspectingField {
  private final InspectionPoint inspectionPoint;
  private final InspectFieldPredicate predicate;

  @Override
  public boolean shouldInspect() {
    return technically() && predicate.shouldInspect(inspectionPoint);
  }

  private boolean technically() {
    Field field = inspectionPoint.getField();
    int mods = field.getModifiers();
    return !Modifier.isStatic(mods)
      && !field.isEnumConstant()
      && !field.isSynthetic();
  }

  @Override
  public boolean showNull() {
    DisplayNull displayNull = inspectionPoint.getField()
      .getAnnotation(DisplayNull.class);
    if (displayNull != null) {
      return displayNull.value();
    } else {
      return DisplayNull.BY_DEFAULT;
    }
  }
}
