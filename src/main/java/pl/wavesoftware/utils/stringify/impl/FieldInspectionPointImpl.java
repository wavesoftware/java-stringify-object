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
import pl.wavesoftware.eid.utils.UnsafeSupplier;
import pl.wavesoftware.utils.stringify.api.InspectionContext;
import pl.wavesoftware.utils.stringify.impl.lang.LangModule;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.function.Supplier;

import static pl.wavesoftware.eid.utils.EidExecutions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 30.04.18
 */
final class FieldInspectionPointImpl implements FieldInspectionPoint {
  private final InspectingField field;
  private final InspectionContext context;
  private final Supplier<Object> value = LangModule.INSTANCE.lazy(this::value);
  private final Supplier<Class<?>> type;

  FieldInspectionPointImpl(InspectingField field, InspectionContext context) {
    this.field = field;
    type = LangModule.INSTANCE.lazy(() -> field.getFieldReflection().getType());
    this.context = context;
  }

  @Override
  public InspectingField getField() {
    return field;
  }

  @Override
  public Supplier<Object> getValue() {
    return value;
  }

  @Override
  public Supplier<Class<?>> getType() {
    return type;
  }

  @Override
  public InspectionContext getContext() {
    return context;
  }

  private Object value() {
    try (final FieldAccessiblier accessiblier = new FieldAccessiblier(field.getFieldReflection())) {
      return tryToExecute(new UnsafeSupplier<Object>() {
        @Override
        @Nonnull
        public Object get() throws IllegalAccessException {
          return accessiblier
            .getField()
            .get(field.getContainingObject());
        }
      }, "20180430:113514");
    }
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
