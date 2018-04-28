package pl.wavesoftware.utils.stringify.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class JPALazyCheckerFacade implements JPALazyChecker {
  private static final JPALazyChecker NOOP_CHECKER = new JPALazyChecker() {
    @Override
    public boolean isLazy(Object candidate) {
      return false;
    }
  };

  private JPALazyChecker lazyChecker;

  @Override
  public boolean isLazy(Object candidate) {
    JPALazyChecker checker = getImpl();
    return checker.isLazy(candidate);
  }

  private JPALazyChecker getImpl() {
    if (lazyChecker == null) {
      lazyChecker = resolveImpl();
    }
    return lazyChecker;
  }

  private static JPALazyChecker resolveImpl() {
    if (HibernateLazyChecker.isSuitable()) {
      return new HibernateLazyChecker();
    }
    return NOOP_CHECKER;
  }
}
