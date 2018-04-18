package pl.wavesoftware.utils.stringify;

import lombok.Data;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Data
abstract class Planet {
  @Inspect
  private String name;
  @Inspect(showNull = true)
  private Boolean rocky;
  @Inspect
  private PlanetSystem planetSystem;

  Planet(Boolean rocky, String name) {
    this.rocky = rocky;
    this.name = name;
  }
}
