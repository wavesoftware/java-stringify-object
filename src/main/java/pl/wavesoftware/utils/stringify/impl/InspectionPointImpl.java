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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.eid.utils.UnsafeSupplier;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.function.Supplier;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 30.04.18
 */
@Getter
@RequiredArgsConstructor
final class InspectionPointImpl implements InspectionPoint {
  private final Field field;
  private final Object containingObject;

  @Override
  public Supplier<Object> getValueSupplier() {
    return () -> {
      try (final FieldAccessiblier accessiblier = new FieldAccessiblier(getField())) {
        return tryToExecute(new UnsafeSupplier<Object>() {
          @Override
          @Nonnull
          public Object get() throws IllegalAccessException {
            return accessiblier
              .getField()
              .get(getContainingObject());
          }
        }, "20180430:113514");
      }
    };
  }

  private static final class FieldAccessiblier implements AutoCloseable {
    @Getter
    private final Field field;
    private final boolean accessible;

    private FieldAccessiblier(Field field) {
      this.field = field;
      this.accessible = ensureAccessible(field);
    }

    @Override
    public void close() {
      if (!accessible) {
        field.setAccessible(false);
      }
    }

    private static boolean ensureAccessible(Field field) {
      boolean ret = field.isAccessible();
      if (!ret) {
        field.setAccessible(true);
      }
      return ret;
    }
  }
}
