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

import org.junit.jupiter.api.Test;
import pl.wavesoftware.utils.stringify.api.InspectionContext;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.api.Namespace;
import pl.wavesoftware.utils.stringify.api.Store;
import pl.wavesoftware.utils.stringify.impl.inspector.InspectorModule;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 30.04.18
 */
class FieldInspectionPointImplTest {

  @Test
  void testGetValueSupplier() throws NoSuchFieldException {
    // given
    Sample sample = new Sample();
    sample.ala = "ma";
    Field alaField = Sample.class.getDeclaredField("ala");
    InspectionContext ctx = InspectorModule.INSTANCE
      .inspectionContext(this::constantStore);
    InspectingFieldFactory inspectingFieldFactory =
      new InspectingFieldFactory(() -> Mode.DEFAULT_MODE);
    InspectingField inspectingField = inspectingFieldFactory.create(
      alaField, sample, this::beanFactory
    );
    InspectionPoint inspectionPoint = new FieldInspectionPointImpl(inspectingField, ctx);

    // when
    Object value = inspectionPoint.getValue().get();

    // then
    assertThat(value).isEqualTo("ma");
  }

  private <T> T beanFactory(Class<T> contract) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private Store constantStore(Namespace namespace) {
    return new StoreImpl();
  }

  private static final class Sample {
    private String ala;
  }
}
