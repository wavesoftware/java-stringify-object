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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.api.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.api.DoNotInspect;
import pl.wavesoftware.utils.stringify.api.Inspect;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import java.util.function.Predicate;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class PromiscuousInspectFieldPredicate implements InspectFieldPredicate {
  private final BeanFactory beanFactory;

  @Override
  public boolean shouldInspect(InspectionPoint inspectionPoint) {
    DoNotInspect doNotInspect = inspectionPoint.getField()
      .getAnnotation(DoNotInspect.class);
    if (doNotInspect != null) {
      return shouldInspect(inspectionPoint, doNotInspect);
    } else {
      Inspect inspect = inspectionPoint.getField()
        .getAnnotation(Inspect.class);
      if (inspect != null && inspect.conditionally() != AlwaysTruePredicate.class) {
        Predicate<InspectionPoint> predicate = beanFactory.create(inspect.conditionally());
        return predicate.test(inspectionPoint);
      } else {
        return true;
      }
    }
  }

  private boolean shouldInspect(InspectionPoint inspectionPoint, DoNotInspect doNotInspect) {
    Class<? extends Predicate<InspectionPoint>> predicateClass = doNotInspect.conditionally();
    if (predicateClass == AlwaysTruePredicate.class) {
      return false;
    } else {
      Predicate<InspectionPoint> predicate = beanFactory.create(predicateClass);
      return !predicate.test(inspectionPoint);
    }
  }
}
