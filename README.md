[![Build Status via Travis CI](https://api.travis-ci.org/enepomnyaschih/squiggle-sql.svg?branch=master)](https://travis-ci.org/enepomnyaschih/squiggle-sql)
[![Coverage Status](https://coveralls.io/repos/github/enepomnyaschih/squiggle-sql/badge.svg?branch=master)](https://coveralls.io/github/enepomnyaschih/squiggle-sql?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.zatarox/squiggle/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.zatarox/squiggle)

Squiggle is a little Java library for dynamically generating SQL SELECT statements. It's sweet spot is for applications
that need to build up complicated queries with criteria that changes at runtime. Ordinarily it can be quite painful to
figure out how to build this string. Squiggle takes much of this pain away.

# Features

* Concise and intuitive API.
* No dependencies on classes outside of JDK 1.8.
* Small, lightweight, fast.
* Generates clean SQL designed that is very human readable.
* Supports all basic SQL features.

# Example

```java
// define table
Table employee = new Table("employee");
TableColumn employeeFirstName = employee.get("firstname");
TableColumn employeeLastName = employee.get("lastname");
TableColumn employeeAge = employee.get("age");

// build query
TableReference e = employee.refer();

SelectQuery select = new SelectQuery();

select.addToSelection(e.get(employeeFirstName));
select.addToSelection(e.get(employeeLastName));

select.addCriteria(new MatchCriteria(e.get(employeeAge), MatchCriteria.GREATEREQUAL, Literal.of(18)));

select.addOrder(e.get(employeeAge), Order.DESCENDING);

select.toString();
```

Which produces:

```SQL
SELECT
    e.firstname,
    e.lastname
FROM
    employee e
WHERE
    e.age >= 18
ORDER BY
    e.age DESC
```

# More query features

```java
// define tables
Table order = new Table("order");
TableColumn orderId = order.get("id");
TableColumn orderTotalPrice = order.get("total_price");
TableColumn orderStatus = order.get("status");
TableColumn orderItems = order.get("items");
TableColumn orderDelivery = order.get("delivery");
TableColumn orderWarehouseId = order.get("warehouse_id");

Table warehouse = new Table("warehouse");
TableColumn warehouseId = warehouse.get("id");
TableColumn warehouseSize = warehouse.get("size");
TableColumn warehouseLocation = warehouse.get("location");

Table offer = new Table("offer");
TableColumn offerLocation = offer.get("location");
TableColumn offerValid = offer.get("valid");

// basic query
TableReference o = order.refer();

SelectQuery select = new SelectQuery();

select.addToSelection(o.get(orderId));
select.addToSelection(o.get(orderTotalPrice));

// matches
select.addCriteria(new MatchCriteria(
        o.get(orderStatus), MatchCriteria.EQUALS, new TypeCast(Literal.of("processed"), "status")));
select.addCriteria(new MatchCriteria(
        o.get(orderItems), MatchCriteria.LESS, Literal.of(5)));
select.addCriteria(new InCriteria(o.get(orderDelivery),
        Literal.of("post"), Literal.of("fedex"), Literal.of("goat")));

// join
TableReference w = warehouse.refer();

select.addCriteria(new MatchCriteria(
        o.get(orderWarehouseId), MatchCriteria.EQUALS, w.get(warehouseId)));

// use joined table
select.addToSelection(w.get(warehouseLocation));
select.addCriteria(new MatchCriteria(
        w.get(warehouseSize), MatchCriteria.EQUALS, Literal.of("big")));

// build subselect query
TableReference f = offer.refer();

SelectQuery subSelect = new SelectQuery();

subSelect.addToSelection(f.get(offerLocation));
subSelect.addCriteria(new MatchCriteria(
        f.get(offerValid), MatchCriteria.EQUALS, Literal.of(true)));

// add subselect to original query
select.addCriteria(new InCriteria(w.get(warehouseLocation), subSelect));

select.toString();
```

Which produces:

```SQL
SELECT
    o.id,
    o.total_price,
    w.location
FROM
    order o,
    warehouse w
WHERE
    o.status = 'processed'::status AND
    o.items < 5 AND
    o.delivery IN ('post', 'fedex', 'goat') AND
    o.warehouse_id = w.id AND
    w.size = 'big' AND
    w.location IN ((
        SELECT
            o.location
        FROM
            offer o
        WHERE
            o.valid = true
    ))
```

# JDBC prepared statements

```java
// define table
Table employee = new Table("employee");
TableColumn employeeName = employee.get("name");
TableColumn employeeAge = employee.get("age");

// build query
TableReference e = employee.refer();

SelectQuery select = new SelectQuery();

select.addToSelection(e.get(employeeName));

select.addCriteria(new MatchCriteria(
        e.get(employeeAge), MatchCriteria.LESS, Parameter.of(30)));

// with java.sql.Connection connection...
PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection));
```

Which produces a statement with a query:

```SQL
SELECT
    e.name
FROM
    employee e
WHERE
    e.age < ?
```

and integer value of 30 as a parameter.
