package pl.wavesoftware.utils.stringify;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.configuration.Inspect;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Getter
@RequiredArgsConstructor
final class User {
  private final String login;
  @Inspect(conditionally = IsInDevelopment.class)
  private final String password;
}
