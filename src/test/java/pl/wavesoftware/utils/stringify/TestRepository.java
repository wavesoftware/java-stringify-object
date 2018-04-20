package pl.wavesoftware.utils.stringify;

import org.hibernate.collection.internal.PersistentList;

import java.util.Arrays;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
final class TestRepository {
  @SuppressWarnings("unchecked")
  Planet createTestPlanet() {
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
