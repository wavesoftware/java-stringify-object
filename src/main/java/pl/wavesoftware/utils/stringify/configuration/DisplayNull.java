package pl.wavesoftware.utils.stringify.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to specify if given field should be displayed even
 * if it holds a null value.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DisplayNull {

  boolean BY_DEFAULT = false;

  /**
   * Do show null value on field annotated with this annotation
   *
   * @return true if should display
   */
  boolean value() default true;
}
