package pl.wavesoftware.utils.stringify.impl;

import org.junit.Test;
import pl.wavesoftware.utils.stringify.configuration.InspectionPoint;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 30.04.18
 */
public class InspectionPointImplTest {

  @Test
  public void testGetValueSupplier() throws NoSuchFieldException {
    // given
    Sample sample = new Sample();
    sample.ala = "ma";
    Field alaField = Sample.class.getDeclaredField("ala");
    alaField.setAccessible(true);
    InspectionPoint inspectionPoint = new InspectionPointImpl(alaField, sample);

    // when
    Object value = inspectionPoint.getValueSupplier().get();

    // then
    assertThat(value).isEqualTo("ma");
  }

  private static final class Sample {
    private String ala;
  }
}
