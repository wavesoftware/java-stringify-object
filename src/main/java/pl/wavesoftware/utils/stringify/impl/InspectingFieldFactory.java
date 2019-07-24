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
import pl.wavesoftware.utils.stringify.spi.BeanFactory;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.api.Mode;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor
final class InspectingFieldFactory {
  private final Supplier<Mode> mode;

  InspectingField create(InspectionPoint inspectionPoint,
                         BeanFactory beanFactory) {
    return new InspectingFieldImpl(
      inspectionPoint, createPredicate(beanFactory), beanFactory
    );
  }

  private InspectFieldPredicate createPredicate(BeanFactory beanFactory) {
    if (mode.get() == Mode.PROMISCUOUS) {
      return new PromiscuousInspectFieldPredicate(beanFactory);
    } else {
      return new QuietInspectFieldPredicate(beanFactory);
    }
  }

}
