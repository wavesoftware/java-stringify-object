package pl.wavesoftware.utils.stringify.configuration;

import pl.wavesoftware.utils.stringify.lang.Predicate;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public final class AlwaysTruePredicate implements Predicate<InspectionPoint> {
  @Override
  public boolean test(InspectionPoint inspectionPoint) {
    return true;
  }
}
