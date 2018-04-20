package pl.wavesoftware.utils.stringify;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.utils.stringify.annotation.Inspect;
import pl.wavesoftware.utils.stringify.impl.ToStringResolver;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@RequiredArgsConstructor
public final class ObjectStringifier {
  private final Object target;
  /**
   * Creates string representation of given object using {@link Inspect} on given fields.
   *
   * @return a string representation of given object
   */
  public CharSequence stringify() {
    return new ToStringResolver(target).resolve();
  }

  @Override
  public String toString() {
    return stringify().toString();
  }
}
