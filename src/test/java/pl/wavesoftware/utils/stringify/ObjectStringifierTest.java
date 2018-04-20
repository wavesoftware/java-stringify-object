package pl.wavesoftware.utils.stringify;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
public class ObjectStringifierTest {

  private final TestRepository testRepository = new TestRepository();

  @Test
  public void testToString() {
    // given
    Planet planet = testRepository.createTestPlanet();
    ObjectStringifier stringifier = new ObjectStringifier(planet);

    // when
    String repr = stringifier.toString();

    // then
    assertEquals("<Earth name=\"Earth\", rocky=true, planetSystem=<PlanetSystem" +
      " planets=⁂Lazy>, moon=<Moon name=\"Moon\", rocky=null, planetSystem=(↻), phase=FULL_MOON," +
      " visits={\"1969\": [\"Apollo 11\",\"Apollo 12\"], \"1971\": [\"Apollo 14\",\"Apollo 15\"]," +
      " \"1972\": [\"Apollo 16\",\"Apollo 17\"]}>, dayOfYear=14, type='A'>", repr);
  }

  @Test
  public void testToStringByLombok() {
    // given
    Planet planet = testRepository.createTestPlanet();

    // when
    String repr = planet.toString();

    // then
    assertEquals("Earth(moon=Moon(phase=FULL_MOON, visits={1969=[Apollo 11, Apollo 12]," +
      " 1971=[Apollo 14, Apollo 15], 1972=[Apollo 16, Apollo 17]}), dayOfYear=14, type=A)", repr);
  }

}
