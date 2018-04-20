package pl.wavesoftware.utils.stringify.impl;

import pl.wavesoftware.eid.utils.EidPreconditions;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

import static pl.wavesoftware.eid.utils.EidPreconditions.tryToExecute;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
final class HibernateLazyChecker implements JPALazyChecker {
  private static final ClassLocator HIBERNATE_LOCATOR =
    new ClassLocator("org.hibernate.Hibernate");
  private static Method isInitializedMethod;

  static boolean isSuitable() {
    return HIBERNATE_LOCATOR.isAvailable();
  }

  @Override
  public boolean isLazy(final Object candidate) {
    Class<?> cls = HIBERNATE_LOCATOR.get();
    final Method method = getIsInitializedMethod(cls);
    return !tryToExecute(
      new EidPreconditions.UnsafeSupplier<Boolean>() {
        @Override
        @Nonnull
        public Boolean get() throws Exception {
          return Boolean.class.cast(method.invoke(null, candidate));
        }
      },
      "20180418:231314"
    );
  }

  private static Method getIsInitializedMethod(final Class<?> hibernateClass) {
    if (isInitializedMethod == null) {
      isInitializedMethod = tryToExecute(
        new EidPreconditions.UnsafeSupplier<Method>() {
          @Override
          @Nonnull
          public Method get() throws Exception {
            return hibernateClass.getDeclaredMethod("isInitialized", Object.class);
          }
        },
        "20180418:231029"
      );
    }
    return isInitializedMethod;
  }
}
