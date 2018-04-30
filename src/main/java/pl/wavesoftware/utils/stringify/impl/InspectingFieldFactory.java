package pl.wavesoftware.utils.stringify.impl;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;
import pl.wavesoftware.utils.stringify.configuration.DisplayNull;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;
import pl.wavesoftware.utils.stringify.configuration.Mode;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor
final class InspectingFieldFactory {
  private final Mode mode;

  InspectingField create(InspectionPoint inspectionPoint,
                         BeanFactory beanFactory) {
    return new InspectingFieldImpl(inspectionPoint, createPredicate(beanFactory));
  }

  private InspectFieldPredicate createPredicate(BeanFactory beanFactory) {
    if (mode == Mode.PROMISCUOUS) {
      return new PromiscuousInspectFieldPredicate(beanFactory);
    } else {
      return new QuietInspectFieldPredicate(beanFactory);
    }
  }

  @RequiredArgsConstructor
  private class InspectingFieldImpl implements InspectingField {
    private final InspectionPoint inspectionPoint;
    private final InspectFieldPredicate predicate;

    @Override
    public boolean shouldInspect() {
      return technically() && predicate.shouldInspect(inspectionPoint);
    }

    private boolean technically() {
      return !inspectionPoint.getField().isEnumConstant()
        && !inspectionPoint.getField().isSynthetic();
    }

    @Override
    public boolean showNull() {
      DisplayNull displayNull = inspectionPoint.getField()
        .getAnnotation(DisplayNull.class);
      if (displayNull != null) {
        return displayNull.value();
      } else {
        return DisplayNull.BY_DEFAULT;
      }
    }
  }
}
