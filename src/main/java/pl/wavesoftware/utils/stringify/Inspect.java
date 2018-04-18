package pl.wavesoftware.utils.stringify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field to be inspected to String representation of parent object.
 *
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inspect {
  /**
   * Should show if field contain a NULL value?
   */
  boolean showNull() default false;
}
