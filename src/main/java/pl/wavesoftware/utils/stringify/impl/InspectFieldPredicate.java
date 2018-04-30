package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
interface InspectFieldPredicate {
  boolean shouldInspect(InspectionPoint inspectionPoint);
}
