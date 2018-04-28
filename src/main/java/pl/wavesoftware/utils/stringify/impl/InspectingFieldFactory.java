package pl.wavesoftware.utils.stringify.impl;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.DisplayNull;
import pl.wavesoftware.utils.stringify.configuration.Mode;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@RequiredArgsConstructor
final class InspectingFieldFactory {
  private final Mode mode;

  InspectingField create(Field field,
                         BeanFactory beanFactory) {
    return new InspectingFieldImpl(field, createPredicate(beanFactory));
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
    private final Field field;
    private final InspectFieldPredicate predicate;

    @Override
    public boolean shouldInspect() {
      return technically() && predicate.shouldInspect(field);
    }

    private boolean technically() {
      return !field.isEnumConstant() && !field.isSynthetic();
    }

    @Override
    public boolean showNull() {
      DisplayNull displayNull = field.getAnnotation(DisplayNull.class);
      if (displayNull != null) {
        return displayNull.value();
      } else {
        return DisplayNull.BY_DEFAULT;
      }
    }
  }
}
