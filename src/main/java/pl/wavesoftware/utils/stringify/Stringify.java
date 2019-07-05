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

import pl.wavesoftware.utils.stringify.api.Configuration;
import pl.wavesoftware.utils.stringify.api.Inspect;
import pl.wavesoftware.utils.stringify.api.Mode;
import pl.wavesoftware.utils.stringify.impl.DefaultStringify;
import pl.wavesoftware.utils.stringify.spi.BeanFactory;

/**
 * <h1>Stringify Object for Java</h1>
 * A utility to safely inspect any Java Object as String representation. It's best to be
 * used with domain model (also with JPA entities) with intention to dump those entities
 * as text to log files.
 * <p>
 * It runs in two modes: {@code PROMISCUOUS} (by default) and {@code QUIET}. In
 * {@code PROMISCUOUS} mode every defined field is automatically inspected, unless the
 * field is annotated with {@code @DoNotInspect} annotation. In {@code QUIET} mode only
 * fields annotated with @Inspect will gets inspected.
 * <p>
 * This library has proper support for object graph cycles, and JPA (Hibernate)
 * lazy loaded elements.
 *
 * <h2>Usage</h2>
 * <pre>
 * // In PROMISCUOUS mode (by default) define fields to exclude
 * class Person {
 *   private int id;
 *   &#064;DisplayNull
 *   private Person parent;
 *   private List&lt;Person&gt; childs;
 *   private Account account;
 *   &#064;Inspect(conditionally = IsInDevelopment.class)
 *   private String password;
 *   &#064;DoNotInspect
 *   private String ignored;
 * }
 *
 * // inspect an object
 * List&lt;Person&gt; people = query.getResultList();
 * Stringify stringify = Stringify.of(people);
 * // stringify.beanFactory(...);
 * assert "&lt;Person id=15, parent=&lt;Person id=16, parent=null, "
 *  + "childs=[(↻)], account=⁂Lazy&gt;, childs=[], "
 *  + "account=⁂Lazy&gt;".equals(stringify.toString());
 * </pre>
 *
 *
 * <h2>Features</h2>
 * <ul>
 *     <li>String representation of any Java class in two modes {@code PROMISCUOUS}
 *     and {@code QUIET}</li>
 *     <li>Fine tuning of which fields to display</li>
 *     <li>Support for cycles in object graph - {@code (↻)} is displayed instead</li>
 *     <li>Support for Hibernate lazy loaded entities - {@code ⁂Lazy} is displayed
 *     instead</li>
 * </ul>
 *
 * <h2>vs. Lombok @ToString</h2>
 *
 * Stringify Object for Java is designed for <strong>slightly different</strong> use case
 * then Lombok.
 * <p>
 * Lombok {@code @ToString} is designed to quickly inspect fields of simple objects by
 * generating static simple implementation of this mechanism.
 *
 * <p>
 * Stringify Object for Java is designed to inspect complex objects that can have cycles
 * and can be managed by JPA provider like Hibernate (introducing Lazy Loading problems).
 *
 * <h3>Pros of Lombok vs Stringify Object</h3>
 *
 * <ul>
 *     <li>Lombok is <strong>fast</strong> - it's statically generated code
 *     without using Reflection API.</li>
 *     <li>Lombok is <strong>easy</strong> - it's zero configuration in most cases.</li>
 * </ul>
 *
 * <h3>Cons of Lombok vs Stringify Object</h3>
 *
 * <ul>
 *     <li>Lombok can't <strong>detect cycles</strong> is object graph, which implies
 *     {@code StackOverflowException} being thrown in that case</li>
 *     <li>Lombok can't detect a <strong>lazy loaded entities</strong>, which leads to
 *     force loading it from JPA by invoking SQL statements. It's typical <strong>n+1
 *     problem</strong>, but with nasty consequences - your {@code toString()} method is
 *     invoking SQL without your knowledge!!</li>
 * </ul>
 *
 * @author <a href="mailto:krzysztof.suszynski@wavesoftware.pl">Krzysztof Suszynski</a>
 * @since 1.0.0
 */
public interface Stringify extends Configuration, CharSequence {

  /**
   * Creates string representation of given object using {@link Inspect} on given fields.
   *
   * @return a string representation of given object
   */
  CharSequence stringify();

  /**
   * Creates a stringify from a given object
   *
   * @param object an object to inspect
   * @return a stringify
   */
  static Stringify of(Object object) {
    return new DefaultStringify(object);
  }

  @Override
  Stringify mode(Mode mode);

  @Override
  Stringify beanFactory(BeanFactory beanFactory);
}
