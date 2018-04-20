
# Stringify Object for Java

[![Build Status](https://travis-ci.org/wavesoftware/java-stringify-object.svg?branch=master)](https://travis-ci.org/wavesoftware/java-stringify-object)

A utility to safely inspect any Java Object as String representation. It's best to be used with JPA entity model to be dumped
to log files.

It utilize `@Inspect` annotation to indicate which fields
 should be displayed. It has support for cycles, and Hibernate lazy loaded elements.

 ## Usage

```java
// define fields to inspect
class Person {
  @Inspect private int id;
  @Inspect private Person parent;
  @Inspect private List<Person> childs;
  @Inspect private Account account;
}

// inspect an object
List<Person> people = query.getResultList();
ObjectStringifier stringifier = new ObjectStringifier(people);
assert "<Person id=15, parent=<Person id=16, parent=null, "
 + "childs=[(↻)], account=⁂Lazy>, childs=[], "
 + "account=⁂Lazy>".equals(stringifier.toString());
```

## Features

 * String representation of any Java class
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

## Requirements

 * JDK >= 7
 * [EID Exceptions](https://github.com/wavesoftware/java-eid-exceptions)

### Contributing

Contributions are welcome!

To contribute, follow the standard [git flow](http://danielkummer.github.io/git-flow-cheatsheet/) of:

1. Fork it
1. Create your feature branch (`git checkout -b feature/my-new-feature`)
1. Commit your changes (`git commit -am 'Add some feature'`)
1. Push to the branch (`git push origin feature/my-new-feature`)
1. Create new Pull Request

Even if you can't contribute code, if you have an idea for an improvement please open an [issue](https://github.com/wavesoftware/java-stringify-object/issues).
