package pl.wavesoftware.utils.stringify;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;
import pl.wavesoftware.utils.stringify.configuration.Inspect;
import pl.wavesoftware.utils.stringify.configuration.Mode;
import pl.wavesoftware.utils.stringify.impl.ToStringResolver;
import pl.wavesoftware.utils.stringify.impl.ToStringResolverFactory;
import pl.wavesoftware.utils.stringify.impl.ToStringResolverFactoryImpl;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@Setter
@RequiredArgsConstructor
public final class ObjectStringifier {
  private static final ToStringResolverFactory RESOLVER_FACTORY =
    new ToStringResolverFactoryImpl();
  private final Object target;
  private Mode mode = Mode.DEFAULT_MODE;
  private BeanFactory beanFactory;

  /**
   * Creates string representation of given object using {@link Inspect} on given fields.
   *
   * @return a string representation of given object
   */
  public CharSequence stringify() {
    ToStringResolver resolver = RESOLVER_FACTORY
      .create(target)
      .withMode(mode);
    if (beanFactory != null) {
      resolver = resolver.withBeanFactory(beanFactory);
    }
    return resolver.resolve();
  }

  @Override
  public String toString() {
    return stringify().toString();
  }
}
