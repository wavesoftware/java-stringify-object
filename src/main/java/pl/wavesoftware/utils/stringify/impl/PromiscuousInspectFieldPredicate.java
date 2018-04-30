package pl.wavesoftware.utils.stringify.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;
import pl.wavesoftware.utils.stringify.configuration.DoNotInspect;
import pl.wavesoftware.utils.stringify.configuration.Inspect;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;
import pl.wavesoftware.utils.stringify.lang.Predicate;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class PromiscuousInspectFieldPredicate implements InspectFieldPredicate {
  private final BeanFactory beanFactory;

  @Override
  public boolean shouldInspect(InspectionPoint inspectionPoint) {
    DoNotInspect doNotInspect = inspectionPoint.getField()
      .getAnnotation(DoNotInspect.class);
    if (doNotInspect != null) {
      return shouldInspect(inspectionPoint, doNotInspect);
    } else {
      Inspect inspect = inspectionPoint.getField()
        .getAnnotation(Inspect.class);
      if (inspect != null && inspect.conditionally() != AlwaysTruePredicate.class) {
        Predicate<InspectionPoint> predicate = beanFactory.create(inspect.conditionally());
        return predicate.test(inspectionPoint);
      } else {
        return true;
      }
    }
  }

  private boolean shouldInspect(InspectionPoint inspectionPoint, DoNotInspect doNotInspect) {
    Class<? extends Predicate<InspectionPoint>> predicateClass = doNotInspect.conditionally();
    if (predicateClass == AlwaysTruePredicate.class) {
      return false;
    } else {
      Predicate<InspectionPoint> predicate = beanFactory.create(predicateClass);
      return !predicate.test(inspectionPoint);
    }
  }
}
