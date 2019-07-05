package pl.wavesoftware.utils.stringify.impl;

final class InspectionUtils {
  private InspectionUtils() {
    // nothing
  }

  static String safeInspect(Object object) {
    if (object instanceof Inspectable) {
      return ((Inspectable) object).inspect();
    }
    return object.getClass().getName() + '@' + Integer.toHexString(object.hashCode());
  }
}
