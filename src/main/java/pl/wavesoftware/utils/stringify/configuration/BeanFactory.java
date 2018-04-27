package pl.wavesoftware.utils.stringify.configuration;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
public interface BeanFactory {
  <T> T create(Class<T> contractClass);
}
