package pl.wavesoftware.utils.stringify;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wavesoftware.utils.stringify.configuration.Inspect;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
final class Earth extends Planet {

  private static final long serialVersionUID = 20180430201544L;

  @Inspect
  private Moon moon;
  @Inspect
  private int dayOfYear;
  @Inspect
  private char type;


  Earth() {
    super(true, "Earth");
  }
}
