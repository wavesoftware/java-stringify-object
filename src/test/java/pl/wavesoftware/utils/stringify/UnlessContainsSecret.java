package pl.wavesoftware.utils.stringify;

import pl.wavesoftware.utils.stringify.api.InspectionPoint;

import java.util.function.Predicate;

final class UnlessContainsSecret implements Predicate<InspectionPoint> {
  @Override
  public boolean test(InspectionPoint inspectionPoint) {
    Object value = inspectionPoint.getValue().get();
    return !value.toString().contains("secret");
  }
}
