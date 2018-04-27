package pl.wavesoftware.utils.stringify;


import lombok.RequiredArgsConstructor;
import org.junit.Test;
import pl.wavesoftware.utils.stringify.configuration.AlwaysTruePredicate;
import pl.wavesoftware.utils.stringify.configuration.Mode;
import pl.wavesoftware.utils.stringify.configuration.Predicate;
import pl.wavesoftware.utils.stringify.configuration.BeanFactory;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author <a href="krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszyński</a>
 * @since 2018-04-18
 */
public class ObjectStringifierTest {

  private final TestRepository testRepository = new TestRepository();

  @Test
  public void testInQuietMode() {
    // given
    Planet planet = testRepository.createTestPlanet();
    ObjectStringifier stringifier = new ObjectStringifier(planet);
    stringifier.setMode(Mode.QUIET);

    // when
    String repr = stringifier.toString();

    // then
    assertEquals("<Earth name=\"Earth\", rocky=true, planetSystem=<PlanetSystem" +
      " planets=⁂Lazy>, moon=<Moon name=\"Moon\", rocky=null, planetSystem=(↻), phase=FULL_MOON," +
      " visits={\"1969\": [\"Apollo 11\",\"Apollo 12\"], \"1971\": [\"Apollo 14\",\"Apollo 15\"]," +
      " \"1972\": [\"Apollo 16\",\"Apollo 17\"]}>, dayOfYear=14, type='A'>", repr);
  }

  @Test
  public void testInPromiscuousMode() {
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
  public void testByLombok() {
    // given
    Planet planet = testRepository.createTestPlanet();

    // when
    String repr = planet.toString();

    // then
    assertEquals("Earth(moon=Moon(phase=FULL_MOON, visits={1969=[Apollo 11, Apollo 12]," +
      " 1971=[Apollo 14, Apollo 15], 1972=[Apollo 16, Apollo 17]}), dayOfYear=14, type=A)", repr);
  }

  @Test
  public void testWithCustomPredicate() throws NoSuchFieldException {
    // given
    SimpleUser user = testRepository.createTestSimpleUser();
    ObjectStringifier stringifier = new ObjectStringifier(user);

    // when
    ProductionEnvironment.setProduction(true);
    String productionResult = stringifier.toString();
    ProductionEnvironment.setProduction(false);
    String developmentResult = stringifier.toString();

    // then
    assertEquals("<SimpleUser login=\"llohan\">", productionResult);
    assertEquals("<SimpleUser login=\"llohan\", password=\"1234567890\", phoneNumber=\"555-123-445\">",
      developmentResult);
    assertTrue(new AlwaysTruePredicate().test(
      this.getClass().getDeclaredField("testRepository")
    ));
  }

  @Test
  public void testWithCustomPredicateWithPredicateFactory() {
    // given
    User user = testRepository.createTestUser();
    IsInDevelopment isDevelopment = new IsInDevelopmentImpl(true);
    IsInDevelopment isProduction = new IsInDevelopmentImpl(false);
    BeanFactory productionBeanFactory = getBeanFactory(isProduction);
    BeanFactory developmentBeanFactory = getBeanFactory(isDevelopment);
    ObjectStringifier stringifier = new ObjectStringifier(user);

    // when
    stringifier.setBeanFactory(productionBeanFactory);
    String productionResult = stringifier.toString();
    stringifier.setBeanFactory(developmentBeanFactory);
    String developmentResult = stringifier.toString();
    stringifier.setMode(Mode.QUIET);
    stringifier.setBeanFactory(developmentBeanFactory);
    String developmentQuietResult = stringifier.toString();

    // then
    assertEquals("<User login=\"jdoe\">", productionResult);
    assertEquals("<User login=\"jdoe\", password=\"1qaz2wsx!@\">", developmentResult);
    assertEquals("<User password=\"1qaz2wsx!@\">", developmentQuietResult);
  }

  @Test
  public void testOnPerson() {
    // given
    Person person = testRepository.createPerson();
    ObjectStringifier stringifier = new ObjectStringifier(person);
    IsInDevelopment isProduction = new IsInDevelopmentImpl(false);
    BeanFactory productionBeanFactory = getBeanFactory(isProduction);
    stringifier.setBeanFactory(productionBeanFactory);

    // when
    String productionResult = stringifier.toString();

    // then
    assertEquals("<Person id=15, parent=<Person id=16, parent=null, " +
      "childs=[(↻)], account=⁂Lazy>, childs=[], account=⁂Lazy>", productionResult);
  }

  private StaticBeanFactory getBeanFactory(IsInDevelopment isInDevelopmentFalse) {
    return new StaticBeanFactory(
      new AbstractMap.SimpleImmutableEntry<Class<?>, Predicate<Field>>(
        IsInDevelopment.class, isInDevelopmentFalse
      )
    );
  }

  @RequiredArgsConstructor
  private static final class IsInDevelopmentImpl implements IsInDevelopment {
    private final boolean development;

    @Override
    public boolean test(Field field) {
      return development;
    }
  }

  private static final class StaticBeanFactory implements BeanFactory {
    private final Map<Class<?>, Object> instances = new HashMap<>();

    @SafeVarargs
    StaticBeanFactory(Map.Entry<Class<?>, Predicate<Field>>... entries) {
      for (Map.Entry<Class<?>, Predicate<Field>> entry : entries) {
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
