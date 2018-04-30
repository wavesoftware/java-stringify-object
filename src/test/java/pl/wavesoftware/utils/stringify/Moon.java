package pl.wavesoftware.utils.stringify;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.wavesoftware.utils.stringify.configuration.Inspect;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Data
@ToString(exclude = "nullinside")
@EqualsAndHashCode(callSuper = true)
final class Moon extends Planet {

  private static final long serialVersionUID = 20180430201602L;

  @Inspect
  private Phase phase;
  @Inspect
  private Map<String, List<String>> visits = new LinkedHashMap<>();
  @Inspect
  private Object nullinside;

  Moon(Phase phase) {
    super(null, "Moon");
    this.phase = phase;
  }
}
