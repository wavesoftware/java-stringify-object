# Stringify Object for Java

[![Build Status](https://travis-ci.org/wavesoftware/java-stringify-object.svg?branch=master)](https://travis-ci.org/wavesoftware/java-stringify-object)

A utility to safely inspect any Java Object as String representation. It's best to be used with JPA entity model to be dumped
to log files.

It utilize `Inspect` annotation to indicate which fields
 should be displayed. It has support for cycles, and Hibernate lazy loaded elements.

 ## Usage

```java
// define fields to inspect
class Person {
  @Inspect
  private int id;
  @Inspect
  private Person parent;
  @Inspect
  private List<Person> childs;
  @Inspect
  private Account account;
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
