package pl.wavesoftware.utils.stringify.spi.theme;

final class CustomTheme implements Theme {
  private static CharSequence name(Object target) {
    return target.getClass().getSimpleName()
      + "#"
      + Integer.toUnsignedString(System.identityHashCode(target), 36);
  }

  @Override
  public ComplexObjectStyle complexObject() {
    return new ComplexObjectStyle() {
      @Override
      public CharSequence begin() {
        return "(";
      }

      @Override
      public CharSequence name(Object target) {
        return CustomTheme.name(target);
      }

      @Override
      public CharSequence end() {
        return ")";
      }

      @Override
      public CharSequence propertySeparator() {
        return ", ";
      }
    };
  }

  @Override
  public MapStyle map() {
    return new MapStyle() {
      @Override
      public CharSequence entryEquals() {
        return " => ";
      }
    };
  }

  @Override
  public JpaLazyStyle jpaLazy() {
    return new JpaLazyStyle() {
      @Override
      public CharSequence representation(Object target) {
        return "❄️";
      }
    };
  }

  @Override
  public RecursionStyle recursion() {
    return new RecursionStyle() {
      @Override
      public CharSequence representation(Object target) {
        return "(↻️️ " + CustomTheme.name(target) + ")";
      }
    };
  }
}
