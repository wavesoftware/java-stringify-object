package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public interface ToStringResolverFactory {
  ToStringResolver create(Object target);
}
