[![Build Status via Travis CI](https://travis-ci.org/gchauvet/squiggle-sql.svg?branch=master)](https://travis-ci.org/gchauvet/squiggle-sql)
[![Coverage Status](https://coveralls.io/repos/github/gchauvet/squiggle-sql/badge.svg?branch=master)](https://coveralls.io/github/gchauvet/squiggle-sql?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.zatarox/squiggle/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.zatarox/squiggle)

Squiggle is a little Java library for dynamically generating SQL SELECT statements. It's sweet spot is for applications that need to build up complicated queries with criteria that changes at runtime. Ordinarily it can be quite painful to figure out how to build this string. Squiggle takes much of this pain away.

The code for Squiggle is intentionally clean and simple. Rather than provide support for every thing you could ever do with SQL, it provides support for the most common situations and allows you to easily modify the source to suit your needs.

#Features
Concise and intuitive API.
Simple code, so easy to customize.
No dependencies on classes outside of JDK 1.5
Small, lightweight, fast.
Generates clean SQL designed that is very human readable.
Supports joins and sub-selects.
Combine criteria with AND, OR and NOT operators.
Supports functions in selects and WHERE criteria

Example
Here's a very simple example:

```java
SelectQuery select = new SelectQuery();

Table people = new Table("people");

select.addColumn(people, "firstname");
select.addColumn(people, "lastname");

select.addOrder(people, "age", Order.DESCENDING);

System.out.println(select);
````

Which produces:
```SQL
SELECT
   people.firstname ,
   people.lastname
FROM
    people
ORDER BY
    people.age DESC
```
