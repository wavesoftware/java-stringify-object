package pl.wavesoftware.utils.stringify;

import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;
import pl.wavesoftware.utils.stringify.lang.Predicate;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public final class ProductionEnvironment implements Predicate<InspectionPoint> {
  private static boolean production = true;

  static void setProduction(boolean setting) {
    production = setting;
  }

  @Override
  public boolean test(InspectionPoint inspectionPoint) {
    return production;
  }
}
