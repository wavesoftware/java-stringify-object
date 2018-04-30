package pl.wavesoftware.utils.stringify.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;
import pl.wavesoftware.utils.stringify.configuration.Inspect;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;
import pl.wavesoftware.utils.stringify.lang.Predicate;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class QuietInspectFieldPredicate implements InspectFieldPredicate {
  private final BeanFactory beanFactory;

  @Override
  public boolean shouldInspect(InspectionPoint inspectionPoint) {
    Inspect inspect = inspectionPoint.getField()
      .getAnnotation(Inspect.class);
    if (inspect != null) {
      return shouldInspect(inspectionPoint, inspect);
    } else {
      return false;
    }
  }

  private boolean shouldInspect(InspectionPoint inspectionPoint, Inspect inspect) {
    Class<? extends Predicate<InspectionPoint>> predicateClass = inspect.conditionally();
    if (predicateClass == AlwaysTruePredicate.class) {
      return true;
    } else {
      Predicate<InspectionPoint> predicate = beanFactory.create(predicateClass);
      return predicate.test(inspectionPoint);
    }
  }
}
