package pl.wavesoftware.utils.stringify;

import lombok.Data;
import pl.wavesoftware.utils.stringify.annotation.Inspect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-19
 */
@Data
final class PlanetSystem {
  @Inspect
  private List<Planet> planets = new ArrayList<>();
}
