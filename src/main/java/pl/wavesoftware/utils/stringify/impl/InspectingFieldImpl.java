package pl.wavesoftware.utils.stringify.impl;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.DisplayNull;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-30
 */
@RequiredArgsConstructor
final class InspectingFieldImpl implements InspectingField {
  private final InspectionPoint inspectionPoint;
  private final InspectFieldPredicate predicate;

  @Override
  public boolean shouldInspect() {
    return technically() && predicate.shouldInspect(inspectionPoint);
  }

  private boolean technically() {
    Field field = inspectionPoint.getField();
    int mods = field.getModifiers();
    return !Modifier.isStatic(mods)
      && !field.isEnumConstant()
      && !field.isSynthetic();
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
