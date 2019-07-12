package pl.wavesoftware.utils.stringify;

import pl.wavesoftware.utils.stringify.api.Configuration;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.spi.Configurator;

public final class TestingConfigurator implements Configurator {
  @Override
  public void configure(Configuration configuration) {
    configuration.mode(Mode.DEFAULT_MODE);
    configuration.beanFactory(new TestBeanFactory());
  }
}
