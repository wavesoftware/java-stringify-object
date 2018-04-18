package pl.wavesoftware.utils.stringify.impl;

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
    return HIBERNATE_LOCATOR.isAvialable();
  }

  @Override
  public boolean isLazy(Object candidate) {
    Class<?> cls = HIBERNATE_LOCATOR.get();
    Method method = getIsInitializedMethod(cls);
    return !tryToExecute(
      () -> Boolean.class.cast(method.invoke(null, candidate)),
      "20180418:231314"
    );
  }

  private static Method getIsInitializedMethod(Class<?> hibernateClass) {
    if (isInitializedMethod == null) {
      isInitializedMethod = tryToExecute(
        () -> hibernateClass.getDeclaredMethod("isInitialized", Object.class),
        "20180418:231029"
      );
    }
    return isInitializedMethod;
  }
}
