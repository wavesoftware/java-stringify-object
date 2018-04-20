package pl.wavesoftware.utils.stringify.impl;

import java.util.Date;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class PrimitiveInspector implements ObjectInspector {
  private static final ClassLocator TEMPORAL_CLASS_LOCATOR =
    new ClassLocator("java.time.temporal.Temporal");


  @Override
  public boolean consentTo(Object candidate,
                           State state) {
    return isPrimitive(candidate);
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    return object.toString();
  }

  private static boolean isPrimitive(Object object) {
    return object instanceof Number
      || object instanceof Boolean
      || object instanceof Enum
      || isDatelike(object);
  }

  private static boolean isDatelike(Object object) {
    return object instanceof Date
      || isInstanceOfTemporal(object);
  }

  private static boolean isInstanceOfTemporal(Object candidate) {
    if (TEMPORAL_CLASS_LOCATOR.isAvailable()) {
      Class<?> temporalCls = TEMPORAL_CLASS_LOCATOR.get();
      return temporalCls.isInstance(candidate);
    }
    return false;
  }
}
