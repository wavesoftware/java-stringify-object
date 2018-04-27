package pl.wavesoftware.utils.stringify.configuration;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public final class AlwaysTruePredicate implements Predicate<Field> {
  @Override
  public boolean test(Field field) {
    return true;
  }
}
