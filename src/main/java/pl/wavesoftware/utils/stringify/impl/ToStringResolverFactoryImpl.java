package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public final class ToStringResolverFactoryImpl implements ToStringResolverFactory {
  @Override
  public ToStringResolver create(Object target) {
    return new ToStringResolverImpl(target);
  }
}
