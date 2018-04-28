package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.eid.utils.EidPreconditions;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static pl.wavesoftware.eid.utils.EidPreconditions.tryToExecute;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 27.04.18
 */
final class ReflectionBeanFactory implements BeanFactory {
  private final Map<Class<?>, Object> created =
    new HashMap<>();

  @Override
  public <T> T create(final Class<T> contractClass) {
    if (isCached(contractClass)) {
      return getFromCache(contractClass);
    }
    T predicate = instantinate(contractClass);
    put(contractClass, predicate);
    return predicate;
  }

  @SuppressWarnings("unchecked")
  private <T> T getFromCache(final Class<T> contractClass) {
    Object bean = created.get(contractClass);
    return (T) bean;
  }

  private <T> void put(Class<T> contractClass, T bean) {
    created.put(contractClass, bean);
  }

  private boolean isCached(Class<?> contractClass) {
    return created.containsKey(contractClass);
  }

  private <T> T instantinate(final Class<T> contractClass) {
    return tryToExecute(new EidPreconditions.UnsafeSupplier<T>() {
      @Override
      @Nonnull
      public T get() throws IllegalAccessException, InstantiationException {
        return contractClass.newInstance();
      }
    }, "20180427:171402");
  }
}
