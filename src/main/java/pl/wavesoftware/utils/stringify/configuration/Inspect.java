package pl.wavesoftware.utils.stringify.configuration;

import pl.wavesoftware.utils.stringify.lang.Predicate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If {@link Mode} is set to {@link Mode#QUIET} (by default), this annotation
 * marks a field to be inspected to String representation of parent object.
 * <p>
 * If {@link Mode} is set to {@link Mode#PROMISCUOUS} this annotation has no function.
 *
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inspect {
  /**
   * If given this predicate will be used to conditionally check if annotated field should
   * be inspected. This can lead to implementing conditional logic of field inspection.
   *
   * @return a class of predicate to be used to determine if field should be inspected
   */
  Class<? extends Predicate<InspectionPoint>> conditionally() default AlwaysTruePredicate.class;
}
