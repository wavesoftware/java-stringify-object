package pl.wavesoftware.utils.stringify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.wavesoftware.utils.stringify.api.Inspect;

@Getter
@AllArgsConstructor
final class Acme {
  @Inspect(conditionally = UnlessContainsSecret.class)
  private String acme;
}
