package pl.wavesoftware.utils.stringify.impl;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkNotNull;

/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
@RequiredArgsConstructor
final class ClassLocator {

  private final String fqcn;
  private Class<?> targetClass;
  private boolean located;

  boolean isAvailable() {
    return getTargetClass() != null;
  }

  Class<?> get() {
    return checkNotNull(getTargetClass(), "20180418:230411");
  }

  @SuppressWarnings({"squid:S1166", "squid:S2658"})
  @Nullable
  private Class<?> getTargetClass() {
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
    return targetClass;
  }
}
