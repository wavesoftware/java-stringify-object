package pl.wavesoftware.utils.stringify.configuration;

import pl.wavesoftware.utils.stringify.lang.Supplier;

import java.lang.reflect.Field;

/**
 * This interface represents a inspection point in some object.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 30.04.18
 */
public interface InspectionPoint {
  /**
   * Get field representation of inspection point
   * @return a field
   */
  Field getField();

  /**
   * Get object that contains this inspection point
   * @return an object
   */
  Object getContainingObject();

  /**
   * Get a field value supplier
   *
   * @return a supplier of a field value
   */
  Supplier<Object> getValueSupplier();
}
