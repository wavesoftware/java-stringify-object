package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
interface ObjectInspector {
  boolean consentTo(Object candidate,
                    State state);
  CharSequence inspect(Object object,
                       Function<Object, CharSequence> alternative);
}
