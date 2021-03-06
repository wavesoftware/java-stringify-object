/*
 * Copyright 2018-2019 Wave Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.wavesoftware.utils.stringify;

import org.hibernate.collection.internal.PersistentList;
import org.hibernate.proxy.AbstractLazyInitializer;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author <a href="mailto:krzysztof.suszynski@coi.gov.pl">Krzysztof Suszynski</a>
 * @since 20.04.18
 */
public final class TestRepository {
  @SuppressWarnings("unchecked")
  public Planet createTestPlanet() {
    PlanetSystem earthPlanetSystem = new PlanetSystem();
    Moon moon = new Moon(Phase.FULL_MOON);
    moon.setPlanetSystem(earthPlanetSystem);
    moon.getVisits().put("1969", Arrays.asList("Apollo 11", "Apollo 12"));
    moon.getVisits().put("1971", Arrays.asList("Apollo 14", "Apollo 15"));
    moon.getVisits().put("1972", Arrays.asList("Apollo 16", "Apollo 17"));
    moon.setIgnored("Ignored value");
    Earth earth = new Earth();
    earth.setDayOfYear(14);
    earth.setType('A');
    earth.setMoon(moon);
    earth.setIgnored("This should not be presented");
    earth.setPlanetSystem(earthPlanetSystem);
    earthPlanetSystem.setPlanets(new PersistentList());
    return earth;
  }

  User createTestUser() {
    return new User("jdoe", "1qaz2wsx!@");
  }

  SimpleUser createTestSimpleUser() {
    return new SimpleUser(
      "llohan",
      "1234567890",
      "555-123-445",
      "AB12 1234 1234 1234 1234 12"
    );
  }

  Person createPerson() {
    Account a1 = new LazyAccount();
    Account a2 = new LazyAccount();
    Person child = new Person();
    Person parent = new Person();
    parent.setId(16);
    parent.setChilds(Collections.singletonList(child));
    parent.setAccount(a2);
    parent.setPassword("!@#$4321qwer");
    parent.setIgnored("Ignore it!");
    child.setId(15);
    child.setParent(parent);
    child.setChilds(new ArrayList<>());
    child.setAccount(a1);
    child.setPassword("rewq1234$#@!");
    child.setIgnored("Dump this");
    return child;
  }

  private static final class LazyAccount extends Account implements HibernateProxy {
    private static final long serialVersionUID = 20180427194122L;

    @Override
    public Object writeReplace() {
      return hashCode();
    }

    @Override
    public LazyInitializer getHibernateLazyInitializer() {
      return new AbstractLazyInitializer() {
        @Override
        public Class<?> getPersistentClass() {
          return LazyAccount.class;
        }
      };
    }
  }
}
