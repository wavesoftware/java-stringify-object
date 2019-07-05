package pl.wavesoftware.utils.stringify.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import javax.annotation.Nullable;

import static pl.wavesoftware.eid.utils.EidPreconditions.checkState;
import static pl.wavesoftware.utils.stringify.impl.InspectionUtils.safeInspect;

final class FallbackBootFactory implements BeanFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(FallbackBootFactory.class);
  private static final BeanFactory DEFAULT = new DefaultBeanFactory();
  private final BeanFactory beanFactory;

  FallbackBootFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public <T> T create(Class<T> contractClass) {
    try {
      @Nullable T bean = beanFactory.create(contractClass);
      checkState(bean != null, "20190705:114553");
      return bean;
    } catch (RuntimeException ex) {
      LOGGER.error(
        MessageFormatter.format(
          "Configured BeanFactory {} thrown an exception while trying to " +
            "resolve {}. Fallback to default BeanFactory.",
          safeInspect(beanFactory), contractClass
        ).getMessage(), ex
      );
      return DEFAULT.create(contractClass);
    }
  }
}
