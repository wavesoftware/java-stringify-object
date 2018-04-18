package pl.wavesoftware.utils.stringify;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
final class Moon extends Planet {
  @Inspect
  private Phase phase;
  @Inspect
  private Map<Year, List<String>> visits = new LinkedHashMap<>();

  Moon(Phase phase) {
    super(null, "Moon");
    this.phase = phase;
  }
}
