package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
interface JPALazyChecker {
  /**
   * Checks if given object is lazy
   *
   * @param candidate a candidate to be checked
   * @return true, if object is lazy
   */
  boolean isLazy(Object candidate);
}
