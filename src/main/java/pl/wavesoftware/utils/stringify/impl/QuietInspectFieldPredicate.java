package pl.wavesoftware.utils.stringify.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.configuration.Inspect;
import pl.wavesoftware.utils.stringify.configuration.Predicate;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class QuietInspectFieldPredicate implements InspectFieldPredicate {
  private final BeanFactory beanFactory;

  @Override
  public boolean shouldInspect(Field field) {
    Inspect inspect = field.getAnnotation(Inspect.class);
    if (inspect != null) {
      return shouldInspect(field, inspect);
    } else {
      return false;
    }
  }

  private boolean shouldInspect(Field field, Inspect inspect) {
    Class<? extends Predicate<Field>> predicateClass = inspect.conditionally();
    if (predicateClass == AlwaysTruePredicate.class) {
      return true;
    } else {
      Predicate<Field> predicate = beanFactory.create(predicateClass);
      return predicate.test(field);
    }
  }
}
