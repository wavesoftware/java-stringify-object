package pl.wavesoftware.utils.stringify.impl;

import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@NoArgsConstructor
final class JPALazyCheckerFacade implements JPALazyChecker {
  private JPALazyChecker lazyChecker;

  @Override
  public boolean isLazy(Object candidate) {
    JPALazyChecker checker = getImpl();
    return checker.isLazy(candidate);
  }

  private JPALazyChecker getImpl() {
    return Optional.ofNullable(lazyChecker)
      .orElseGet(JPALazyCheckerFacade::resolveImpl);
  }

  private static JPALazyChecker resolveImpl() {
    if (HibernateLazyChecker.isSuitable()) {
      return new HibernateLazyChecker();
    }
    return candidate -> false;
  }
}
