# Stringify Object for Java  
  
[![Build Status](https://travis-ci.org/wavesoftware/java-stringify-object.svg?branch=master)](https://travis-ci.org/wavesoftware/java-stringify-object) [![Quality Gate](https://sonar.wavesoftware.pl/api/badges/gate?key=pl.wavesoftware.utils:stringify-object)](https://sonar.wavesoftware.pl/dashboard/index/pl.wavesoftware.utils:stringify-object) [![Coverage Status](https://coveralls.io/repos/github/wavesoftware/java-stringify-object/badge.svg?branch=master)](https://coveralls.io/github/wavesoftware/java-stringify-object?branch=master) [![Maven Central](https://img.shields.io/maven-central/v/pl.wavesoftware.utils/stringify-object.svg)](https://bintray.com/bintray/jcenter/pl.wavesoftware.utils%3Astringify-object)  
  
  
A utility to safely inspect any Java Object as String representation. It's best to be used with domain model (also with JPA entities) with intention to dump those entities as text to log files.  

It runs in two modes: `PROMISCUOUS` (by default) and `QUIET`. In `PROMISCUOUS` mode every defined field is automatically inspected, unless the field is annotated with `@DoNotInspect` annotation.
In `QUIET` mode only fields annotated with `@Inspect` will gets inspected.
  
This library has proper support for object graph cycles, and JPA (Hibernate) lazy loaded elements.
  
 ## Usage  

 ### In Promiscuous mode

```java  
// In PROMISCUOUS mode define fields to exclude
class Person {
  private int id;
  @DisplayNull
  private Person parent;
  private List<Person> childs;
  private Account account;
  @Inspect(conditionally = IsInDevelopment.class)
  private String password;
  @DoNotInspect
  private String ignored;
}
  
// inspect an object  
List<Person> people = query.getResultList();  
ObjectStringifier stringifier = new ObjectStringifier(people);  
stringifier.setMode(Mode.PROMISCUOUS);
// stringifier.setBeanFactory(...);
assert "<Person id=15, parent=<Person id=16, parent=null, "  
 + "childs=[(↻)], account=⁂Lazy>, childs=[], "  
 + "account=⁂Lazy>".equals(stringifier.toString());  
```
  
 ### In Quiet mode
```java  
// In QUIET mode define fields to inspect  
class Person {  
  @Inspect private int id;
  @Inspect @DisplayNull private Person parent;
  @Inspect private List<Person> childs;
  @Inspect private Account account;
  private String ignored;
}
  
// inspect an object  
List<Person> people = query.getResultList();  
ObjectStringifier stringifier = new ObjectStringifier(people);  
stringifier.setMode(Mode.QUIET);
assert "<Person id=15, parent=<Person id=16, parent=null, "  
 + "childs=[(↻)], account=⁂Lazy>, childs=[], "  
 + "account=⁂Lazy>".equals(stringifier.toString());  
```  
  
## Features  
  
 * String representation of any Java class in two modes `PROMISCUOUS` and `QUIET`
 * Fine tuning of which fields to display  
 * Support for cycles in object graph - `(↻)` is displayed instead  
 * Support for Hibernate lazy loaded entities - `⁂Lazy` is displayed instead  
  
## vs. Lombok @ToString  
  
Stringify Object for Java is designed for **slightly different** use case then Lombok.  
  
Lombok `@ToString` is designed to quickly inspect fields of simple objects by generating static simple implementation of this mechanism.  
  
Stringify Object for Java is designed to inspect complex objects that can have cycles and can be managed by JPA provider like Hibernate (introducing Lazy Loading problems).  
  
#### Pros of Lombok vs Stringify Object  
  
 * Lombok is **fast** - it's statically generated code without using Reflection API.  
 * Lombok is **easy** - it's zero configuration in most cases.  
  
#### Cons of Lombok vs Stringify Object  
  
 * Lombok can't **detect cycles** is object graph, which implies `StackOverflowException` being thrown in that case  
 * Lombok can't detect a **lazy loaded entities**, which leads to force loading it from JPA by invoking SQL statements. It's typical **n+1 problem**, but with nasty consequences - your `toString()` method is invoking SQL without your knowledge!!  
  
## Dependencies  
  
 * Java >= 7
 * [EID Exceptions](https://github.com/wavesoftware/java-eid-exceptions) library 
  
### Contributing  
  
Contributions are welcome!  
  
To contribute, follow the standard [git flow](http://danielkummer.github.io/git-flow-cheatsheet/) of:  
  
1. Fork it  
1. Create your feature branch (`git checkout -b feature/my-new-feature`)  
1. Commit your changes (`git commit -am 'Add some feature'`)  
1. Push to the branch (`git push origin feature/my-new-feature`)  
1. Create new Pull Request  
  
Even if you can't contribute code, if you have an idea for an improvement please open an [issue](https://github.com/wavesoftware/java-stringify-object/issues).
