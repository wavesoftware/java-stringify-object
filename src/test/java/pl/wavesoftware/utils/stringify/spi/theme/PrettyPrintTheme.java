package pl.wavesoftware.utils.stringify.spi.theme;

import pl.wavesoftware.utils.stringify.api.InspectionPoint;

final class PrettyPrintTheme implements Theme {

  private static final String INDENT = "  ";

  private static CharSequence name(Object target) {
    return target.getClass().getSimpleName()
      + "#"
      + Integer.toUnsignedString(System.identityHashCode(target), 36);
  }

  @Override
  public ComplexObjectStyle complexObject() {
    return new ComplexObjectStyle() {
      @Override
      public CharSequence begin(InspectionPoint point) {
        point.getContext().indentationControl().increment();
        return "(";
      }

      @Override
      public CharSequence name(InspectionPoint point) {
        return PrettyPrintTheme.name(point.getValue().get())
          + "\n" +
          point.getContext().indentationControl().indent(INDENT);
      }

      @Override
      public CharSequence end(InspectionPoint point) {
        point.getContext().indentationControl().decrement();
        return "\n" + point.getContext().indentationControl().indent(INDENT) + ")";
      }

      @Override
      public CharSequence propertySeparator(InspectionPoint point) {
        return ",\n" + point.getContext().indentationControl().indent(INDENT);
      }

      @Override
      public CharSequence nameSeparator(InspectionPoint point) {
        return "";
      }

      @Override
      public CharSequence propertyEquals(InspectionPoint point) {
        return " = ";
      }
    };
  }

  @Override
  public MapStyle map() {
    return new MapStyle() {
      @Override
      public CharSequence begin(InspectionPoint point) {
        point.getContext().indentationControl().increment();
        return "{\n" +
          point.getContext().indentationControl().indent(INDENT);
      }

      @Override
      public CharSequence separator(InspectionPoint point) {
        return ",\n" + point.getContext().indentationControl().indent(INDENT);
      }

      @Override
      public CharSequence entryEquals(InspectionPoint point) {
        return " => ";
      }

      @Override
      public CharSequence end(InspectionPoint point) {
        point.getContext().indentationControl().decrement();
        return "\n" + point.getContext().indentationControl().indent(INDENT) + "}";
      }
    };
  }

  @Override
  public JpaLazyStyle jpaLazy() {
    return new JpaLazyStyle() {
      @Override
      public CharSequence representation(InspectionPoint point) {
        return "❄️";
      }
    };
  }

  @Override
  public RecursionStyle recursion() {
    return new RecursionStyle() {
      @Override
      public CharSequence representation(InspectionPoint point) {
        return "(↻️️ " + PrettyPrintTheme.name(point.getValue().get()) + ")";
      }
    };
  }

  @Override
  public IterableStyle iterable() {
    return new IterableStyle() {
      @Override
      public CharSequence separator(InspectionPoint point) {
        return ", ";
      }
    };
  }
}
