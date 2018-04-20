package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class JPALazyInspector implements ObjectInspector {
  private static final JPALazyChecker LAZY_CHECKER = new JPALazyCheckerFacade();

  @Override
  public boolean consentTo(Object candidate, State state) {
    return LAZY_CHECKER.isLazy(candidate);
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    return "⁂Lazy";
  }
}
