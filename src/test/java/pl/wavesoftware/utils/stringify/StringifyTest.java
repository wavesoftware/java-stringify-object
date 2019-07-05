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


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import pl.wavesoftware.utils.stringify.api.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.api.InspectionPoint;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 2018-04-18
 */
class StringifyTest {

  private final TestRepository testRepository = new TestRepository();

  @Test
  void inQuietMode() {
    // given
    Planet planet = testRepository.createTestPlanet();
    Stringify stringifier = Stringify.of(planet);
    stringifier.mode(Mode.QUIET);

    // when
    String repr = stringifier.toString();

    // then
    assertThat(repr).isEqualTo(
      "<Earth name=\"Earth\", rocky=true, planetSystem=<PlanetSystem" +
      " planets=⁂Lazy>, moon=<Moon name=\"Moon\", rocky=null, planetSystem=(↻), phase=FULL_MOON," +
      " visits={\"1969\": [\"Apollo 11\",\"Apollo 12\"], \"1971\": [\"Apollo 14\",\"Apollo 15\"]," +
      " \"1972\": [\"Apollo 16\",\"Apollo 17\"]}>, dayOfYear=14, type='A'>"
    );
  }

  @Test
  void inPromiscuousMode() {
    // given
    Planet planet = testRepository.createTestPlanet();
    Stringify stringifier = Stringify.of(planet);

    // when
    String repr = stringifier.toString();

    // then
    assertThat(repr).isEqualTo(
      "<Earth name=\"Earth\", rocky=true, planetSystem=<PlanetSystem" +
      " planets=⁂Lazy>, moon=<Moon name=\"Moon\", rocky=null, planetSystem=(↻), phase=FULL_MOON," +
      " visits={\"1969\": [\"Apollo 11\",\"Apollo 12\"], \"1971\": [\"Apollo 14\",\"Apollo 15\"]," +
      " \"1972\": [\"Apollo 16\",\"Apollo 17\"]}>, dayOfYear=14, type='A'>"
    );
  }

  @Test
  void byLombok() {
    // given
    Planet planet = testRepository.createTestPlanet();

    // when
    String repr = planet.toString();

    // then
    assertThat(repr).isEqualTo(
      "Earth(moon=Moon(phase=FULL_MOON, visits={1969=[Apollo 11, Apollo 12]," +
      " 1971=[Apollo 14, Apollo 15], 1972=[Apollo 16, Apollo 17]}), dayOfYear=14, type=A)"
    );
  }

  @Test
  void withCustomPredicate() {
    // given
    SimpleUser user = testRepository.createTestSimpleUser();
    Stringify stringifier = Stringify.of(user);

    // when
    ProductionEnvironment.setProduction(true);
    String productionResult = stringifier.toString();
    ProductionEnvironment.setProduction(false);
    String developmentResult = stringifier.toString();

    // then
    assertThat(productionResult).isEqualTo("<SimpleUser login=\"llohan\">");
    assertThat(developmentResult).isEqualTo(
      "<SimpleUser login=\"llohan\", password=\"1234567890\", phoneNumber=\"555-123-445\">"
    );

    assertThat(new AlwaysTruePredicate()).accepts(
      new InspectionPoint() {
        @Override
        public Field getField() {
          return null;
        }

        @Override
        public Object getContainingObject() {
          return null;
        }

        @Override
        public Supplier<Object> getValueSupplier() {
          return null;
        }
      }
    );
  }

  @Test
  void withCustomPredicateWithPredicateFactory() {
    // given
    User user = testRepository.createTestUser();
    IsInDevelopment isDevelopment = new IsInDevelopmentImpl(true);
    IsInDevelopment isProduction = new IsInDevelopmentImpl(false);
    BeanFactory productionBeanFactory = getBeanFactory(isProduction);
    BeanFactory developmentBeanFactory = getBeanFactory(isDevelopment);
    Stringify stringifier = Stringify.of(user);

    // when
    stringifier.beanFactory(productionBeanFactory);
    String productionResult = stringifier.toString();
    stringifier.beanFactory(developmentBeanFactory);
    String developmentResult = stringifier.toString();
    stringifier.mode(Mode.QUIET);
    stringifier.beanFactory(developmentBeanFactory);
    String developmentQuietResult = stringifier.toString();

    // then
    assertThat(productionResult).isEqualTo("<User login=\"jdoe\">");
    assertThat(developmentResult).isEqualTo("<User login=\"jdoe\", password=\"1qaz2wsx!@\">");
    assertThat(developmentQuietResult).isEqualTo("<User password=\"1qaz2wsx!@\">");
  }

  @Test
  void onPerson() {
    // given
    Person person = testRepository.createPerson();
    Stringify stringifier = Stringify.of(person);
    IsInDevelopment isProduction = new IsInDevelopmentImpl(false);
    BeanFactory productionBeanFactory = getBeanFactory(isProduction);
    stringifier.beanFactory(productionBeanFactory);

    // when
    String productionResult = stringifier.toString();

    // then
    assertThat(productionResult).isEqualTo(
      "<Person id=15, parent=<Person id=16, parent=null, " +
      "childs=[(↻)], account=⁂Lazy>, childs=[], account=⁂Lazy>"
    );
  }

  @Test
  void onPersonWithCustomPerdicateLogic() {
    // given
    Person person = testRepository.createPerson();
    Stringify stringifier = Stringify.of(person);
    IsInDevelopment isInDevelopment = new IsInDevelopmentPredicate(value ->
      value instanceof String
        && ((String) value).contains("!@#$")
    );
    BeanFactory productionBeanFactory = getBeanFactory(isInDevelopment);
    stringifier.beanFactory(productionBeanFactory);

    // when
    String productionResult = stringifier.toString();

    // then
    assertThat(productionResult).isEqualTo(
      "<Person id=15, parent=<Person id=16, parent=null, " +
      "childs=[(↻)], account=⁂Lazy, password=\"!@#$4321qwer\">, childs=[], " +
      "account=⁂Lazy>"
    );
    assertThat(stringifier).hasSize(52);
    assertThat(stringifier.charAt(1)).isEqualTo('P');
    assertThat(stringifier.subSequence(1, 7)).isEqualTo("Person");
  }

  @Test
  void fallbackToDefault() {
    // given
    Acme acme1 = new Acme("simple value");
    Stringify stringifier1 = Stringify.of(acme1);
    Acme acme2 = new Acme("value with secret");
    Stringify stringifier2 = Stringify.of(acme2);

    try {
      // when
      TestBeanFactory.ready = true;
      String result1 = stringifier1.toString();
      String result2 = stringifier2.toString();

      // then
      assertThat(result1).isEqualTo(
        "<Acme acme=\"simple value\">"
      );
      assertThat(result2).isEqualTo(
        "<Acme>"
      );
    } finally {
      TestBeanFactory.ready = false;
    }
  }

  private static boolean inspectionPointValue(final InspectionPoint inspectionPoint,
                                              final Predicate<Object> predicate) {
    return predicate.test(
      inspectionPoint
        .getValueSupplier()
        .get()
    );
  }

  private StaticBeanFactory getBeanFactory(IsInDevelopment isInDevelopmentFalse) {
    return new StaticBeanFactory(
      new AbstractMap.SimpleImmutableEntry<>(
        IsInDevelopment.class, isInDevelopmentFalse
      )
    );
  }

  @RequiredArgsConstructor
  private static final class IsInDevelopmentPredicate implements IsInDevelopment {
    private final Predicate<Object> predicate;

    @Override
    public boolean test(InspectionPoint inspectionPoint) {
      return inspectionPointValue(inspectionPoint, predicate);
    }
  }

  @RequiredArgsConstructor
  private static final class IsInDevelopmentImpl implements IsInDevelopment {
    private final boolean development;

    @Override
    public boolean test(InspectionPoint point) {
      return development;
    }
  }

  private static final class StaticBeanFactory implements BeanFactory {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    @SafeVarargs
    StaticBeanFactory(Map.Entry<Class<?>, Predicate<InspectionPoint>>... entries) {
      for (Map.Entry<Class<?>, Predicate<InspectionPoint>> entry : entries) {
        instances.put(entry.getKey(), entry.getValue());
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> contractClass) {
      Object bean = instances.get(contractClass);
      return (T) bean;
    }
  }

}
