package pl.wavesoftware.utils.stringify.impl;

import java.util.Map;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class MapInspector implements ObjectInspector {

  @Override
  public boolean consentTo(Object candidate,
                           State state) {
    return candidate instanceof Map;
  }

  @Override
  public CharSequence inspect(Object object,
                              Function<Object, CharSequence> alternative) {
    Map<?, ?> map = (Map<?, ?>) object;
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();
      sb.append(alternative.apply(key));
      sb.append(": ");
      sb.append(alternative.apply(value));
      sb.append(", ");
    }
    if (sb.length() > 1) {
      sb.deleteCharAt(sb.length() - 1);
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb.toString();
  }
}
