package pl.wavesoftware.utils.stringify.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.eid.utils.EidPreconditions;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;
import pl.wavesoftware.utils.stringify.lang.Supplier;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

import static pl.wavesoftware.eid.utils.EidPreconditions.tryToExecute;

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
    return new Supplier<Object>() {
      @Override
      public Object get() {
        try (final FieldAccessiblier accessiblier = new FieldAccessiblier(getField())) {
          return tryToExecute(new EidPreconditions.UnsafeSupplier<Object>() {
            @Override
            @Nonnull
            public Object get() throws IllegalAccessException {
              return accessiblier
                .getField()
                .get(getContainingObject());
            }
          }, "20180430:113514");
        }
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
