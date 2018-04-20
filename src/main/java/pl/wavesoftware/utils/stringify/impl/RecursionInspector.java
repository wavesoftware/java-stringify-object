package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class RecursionInspector implements ObjectInspector {
  @Override
  public boolean consentTo(Object candidate, State state) {
    return state.wasInspected(candidate);
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    return "(↻)";
  }
}
