package pl.wavesoftware.utils.stringify;


import org.hibernate.collection.internal.PersistentList;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
public class ObjectStringifierTest {

  @Test
  public void testToString() {
    // given
    Planet planet = createTestPlanet();
    ObjectStringifier stringifier = new ObjectStringifier(planet);

    // when
    String repr = stringifier.toString();

    // then
    assertEquals("<Earth name=\"Earth\", rocky=true, planetSystem=<PlanetSystem" +
      " planets=⁂Lazy>, moon=<Moon name=\"Moon\", rocky=null, planetSystem=(↻), phase=FULL_MOON," +
      " visits={\"1969\": [\"Apollo 11\",\"Apollo 12\"], \"1971\": [\"Apollo 14\",\"Apollo 15\"]," +
      " \"1972\": [\"Apollo 16\",\"Apollo 17\"]}>, dayOfYear=14, type='A'>", repr);
  }

  private Planet createTestPlanet() {
    PlanetSystem earthPlanetSystem = new PlanetSystem();
    Moon moon = new Moon(Phase.FULL_MOON);
    moon.setPlanetSystem(earthPlanetSystem);
    moon.getVisits().put("1969", Arrays.asList("Apollo 11", "Apollo 12"));
    moon.getVisits().put("1971", Arrays.asList("Apollo 14", "Apollo 15"));
    moon.getVisits().put("1972", Arrays.asList("Apollo 16", "Apollo 17"));
    Earth earth = new Earth();
    earth.setDayOfYear(14);
    earth.setType('A');
    earth.setMoon(moon);
    earth.setPlanetSystem(earthPlanetSystem);
    earthPlanetSystem.setPlanets(new PersistentList());
    return earth;
  }
}
