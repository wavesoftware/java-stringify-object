package pl.wavesoftware.utils.stringify.configuration;

import pl.wavesoftware.utils.stringify.lang.Predicate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When running in {@link Mode#PROMISCUOUS} this annotation can be used to exclude a
 * field from inspection.
 *
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoNotInspect {
  /**
   * If given this predicate will be used to conditionally check if annotated field should not
   * be inspected. This can lead to implementing conditional logic of field inspection.
   *
   * @return a class of predicate to be used to determine if field should not be inspected
   */
  Class<? extends Predicate<InspectionPoint>> conditionally() default AlwaysTruePredicate.class;
}
