package pl.wavesoftware.utils.stringify;

import lombok.Setter;
import pl.wavesoftware.utils.stringify.configuration.DisplayNull;
import pl.wavesoftware.utils.stringify.configuration.DoNotInspect;
import pl.wavesoftware.utils.stringify.configuration.Inspect;

import java.util.List;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
@Setter
class Person {
  private volatile int id;
  @DisplayNull
  private transient Person parent;
  private List<Person> childs;
  private Account account;
  @Inspect(conditionally = IsInDevelopment.class)
  private String password;
  @DoNotInspect
  private String ignored;
}
