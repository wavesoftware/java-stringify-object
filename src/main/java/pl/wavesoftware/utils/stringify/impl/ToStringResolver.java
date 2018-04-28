package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.utils.stringify.configuration.Mode;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public interface ToStringResolver {
  /**
   * Configures a mode in which resolver operates
   *
   * @param mode a mode to set
   * @return a self reference
   */
  ToStringResolver withMode(Mode mode);

  /**
   * Configures a predicate factory to be used to load implementation of predicates used
   *
   * @param beanFactory a predicate factory
   * @return a self reference
   */
  ToStringResolver withBeanFactory(BeanFactory beanFactory);

  /**
   * Resolves a {@link String} representation of given object.
   *
   * @return String representation
   */
  CharSequence resolve();
}
