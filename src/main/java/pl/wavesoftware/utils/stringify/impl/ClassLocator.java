package pl.wavesoftware.utils.stringify.impl;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.eid.exceptions.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;

import java.util.Optional;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@RequiredArgsConstructor
final class ClassLocator {

  private final String fqcn;
  private Class<?> targetClass;
  private boolean located;

  boolean isAvialable() {
    return getTargetClass()
      .isPresent();
  }

  Class<?> get() {
    return getTargetClass()
      .orElseThrow(() -> new EidIllegalStateException(new Eid("20180418:230411")));
  }

  @SuppressWarnings({"squid:S1166", "squid:S2658"})
  private Optional<Class<?>> getTargetClass() {
    if (!located) {
      ClassLoader cl = Thread
        .currentThread()
        .getContextClassLoader();
      try {
        targetClass = Class.forName(fqcn, false, cl);
      } catch (ClassNotFoundException e) {
        // do nothing
      }
      located = true;
    }
    return Optional.ofNullable(targetClass);
  }
}
