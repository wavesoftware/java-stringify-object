package pl.wavesoftware.utils.stringify;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.DoNotInspect;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Getter
@RequiredArgsConstructor
final class SimpleUser {
  private final String login;
  @DoNotInspect(conditionally = ProductionEnvironment.class)
  private final String password;
  @DoNotInspect(conditionally = ProductionEnvironment.class)
  private final String phoneNumber;
}
