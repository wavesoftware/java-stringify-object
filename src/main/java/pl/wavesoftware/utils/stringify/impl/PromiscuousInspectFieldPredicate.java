package pl.wavesoftware.utils.stringify.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.configuration.DoNotInspect;
import pl.wavesoftware.utils.stringify.configuration.Inspect;
import pl.wavesoftware.utils.stringify.configuration.Predicate;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class PromiscuousInspectFieldPredicate implements InspectFieldPredicate {
  private final BeanFactory beanFactory;

  @Override
  public boolean shouldInspect(Field field) {
    DoNotInspect doNotInspect = field.getAnnotation(DoNotInspect.class);
    if (doNotInspect != null) {
      return shouldInspect(field, doNotInspect);
    } else {
      Inspect inspect = field.getAnnotation(Inspect.class);
      if (inspect != null && inspect.conditionally() != AlwaysTruePredicate.class) {
        Predicate<Field> predicate = beanFactory.create(inspect.conditionally());
        return predicate.test(field);
      } else {
        return true;
      }
    }
  }

  private boolean shouldInspect(Field field, DoNotInspect doNotInspect) {
    Class<? extends Predicate<Field>> predicateClass = doNotInspect.conditionally();
    if (predicateClass == AlwaysTruePredicate.class) {
      return false;
    } else {
      Predicate<Field> predicate = beanFactory.create(predicateClass);
      return !predicate.test(field);
    }
  }
}
