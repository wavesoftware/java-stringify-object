package pl.wavesoftware.utils.stringify.impl;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class IterableInspector implements ObjectInspector {
  @Override
  public boolean consentTo(Object candidate, State state) {
    return candidate instanceof Iterable;
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    return inspectIterable((Iterable<?>) object, alternative);
  }

  private static CharSequence inspectIterable(Iterable<?> iterable,
                                              Function<Object, CharSequence> alternative) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Object elem : iterable) {
      sb.append(alternative.apply(elem));
      sb.append(",");
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
}
