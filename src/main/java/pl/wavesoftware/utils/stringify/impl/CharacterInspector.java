package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class CharacterInspector implements ObjectInspector {

  @Override
  public boolean consentTo(Object candidate, State state) {
    return candidate instanceof Character;
  }

  @Override
  public CharSequence inspect(Object object, Function<Object, CharSequence> alternative) {
    return "'" + object.toString() + "'";
  }
}
