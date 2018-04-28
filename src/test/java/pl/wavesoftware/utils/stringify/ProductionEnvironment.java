package pl.wavesoftware.utils.stringify;

import pl.wavesoftware.utils.stringify.configuration.Predicate;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public final class ProductionEnvironment implements Predicate<Field> {
  private static boolean production = true;

  static void setProduction(boolean setting) {
    production = setting;
  }

  @Override
  public boolean test(Field field) {
    return production;
  }
}
