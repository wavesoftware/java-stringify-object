package pl.wavesoftware.utils.stringify;

import lombok.Data;
import pl.wavesoftware.utils.stringify.configuration.DisplayNull;
import pl.wavesoftware.utils.stringify.configuration.DoNotInspect;
import pl.wavesoftware.utils.stringify.configuration.Inspect;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Data
abstract class Planet {
  @Inspect
  private String name;
  @Inspect
  @DisplayNull
  private Boolean rocky;
  @Inspect
  private PlanetSystem planetSystem;
  @DoNotInspect
  private String ignored;

  Planet(Boolean rocky, String name) {
    this.rocky = rocky;
    this.name = name;
  }
}
