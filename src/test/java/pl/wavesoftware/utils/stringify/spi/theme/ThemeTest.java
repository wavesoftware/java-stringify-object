package pl.wavesoftware.utils.stringify.spi.theme;


import org.junit.jupiter.api.Test;
import pl.wavesoftware.utils.stringify.Earth;
import pl.wavesoftware.utils.stringify.Stringify;
import pl.wavesoftware.utils.stringify.TestRepository;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeTest {
  private final TestRepository testRepository = new TestRepository();

  @Test
  void customTheme() {
    // given
    Earth planet = (Earth) testRepository.createTestPlanet();
    Stringify stringifier = Stringify.of(planet);
    stringifier.theme(new CustomTheme());
    CharSequence earthHash = hash(planet);
    CharSequence planetSystemHash = hash(planet.getPlanetSystem());
    CharSequence moonHash = hash(planet.getMoon());

    // when
    String result = stringifier.stringify().toString();

    // then
    String expected = format(
      "(Earth#{0} name=\"Earth\", rocky=true, " +
        "planetSystem=(PlanetSystem#{1} planets=❄️), " +
        "moon=(Moon#{2} name=\"Moon\", rocky=null, " +
        "planetSystem=(↻️️ PlanetSystem#{1}), phase=FULL_MOON, " +
        "visits={" +
        "\"1969\" => [\"Apollo 11\",\"Apollo 12\"], " +
        "\"1971\" => [\"Apollo 14\",\"Apollo 15\"], " +
        "\"1972\" => [\"Apollo 16\",\"Apollo 17\"]" +
        "}), dayOfYear=14, type='A')",
      earthHash, planetSystemHash, moonHash
    );
    assertThat(result).isEqualTo(expected);
  }

  private String format(String formatStr, Object... args) {
    String work = formatStr;
    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];
      work = work.replace("{" + i + "}", arg.toString());
    }
    assertThat(work).isNotEqualTo(formatStr);
    return work;
  }

  private CharSequence hash(Object object) {
    return Integer.toUnsignedString(System.identityHashCode(object), 36);
  }
}
