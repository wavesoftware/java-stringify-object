package pl.wavesoftware.utils.stringify;

import pl.wavesoftware.utils.stringify.spi.BeanFactory;
import pl.wavesoftware.utils.stringify.spi.BootingAware;

final class TestBeanFactory implements BeanFactory, BootingAware {

  static boolean ready = false;

  @Override
  public <T> T create(Class<T> contractClass) {
    throw new UnsupportedOperationException(
      "💩 This exception is expected to be thrown in JUnit"
    );
  }

  @Override
  public boolean isReady() {
    return ready;
  }
}
