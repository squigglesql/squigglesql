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
* Supports JDBC PreparedStatement compilation.
* Provides matching sets of raw literals, SQL parameters and result set readers.

# Examples

## Basic example

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

## More query features

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

## JDBC prepared statements

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

## Result set readers (JdbcUtils.read*)

```java
// define table
Table employee = new Table("employee");
TableColumn employeeName = employee.get("name");
TableColumn employeeAge = employee.get("age");

// build query
TableReference e = employee.refer();

SelectQuery select = new SelectQuery();

ResultColumn employeeNameResult = select.addToSelection(e.get(employeeName));
ResultColumn employeeAgeResult = select.addToSelection(e.get(employeeAge));

try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
    try (ResultSet rs = statement.executeQuery()) {
        String name = JdbcUtils.readString(rs, employeeNameResult.getIndex()));
        int age = JdbcUtils.readIntegerNotNull(rs, employeeAgeResult.getIndex());
    }
}
```

# Best practices

## Intro

This topic describes how to better organize your Java classes to gain a full advantage from dynamic SQL compilation.
You shouldn't treat these patterns as a dogma - instead, you should take them as a base template for further code
extensions.

Assuming that you have the following tables:

* *customer* (id, name, city)
* *order* (id, issued_at, *customer_id*)
* *product* (id, name, price)
* *order_item* (id, *order_id*, *product_id*, quantity)

You should start with the following.

## Model classes

Let's define 4 simple immutable Java models. Example for *order_item* table:

```java
public class OrderItem {

    private final int id;
    private final Order order;
    private final Product product;
    private final int quantity;

    public OrderItem(int id, Order order, Product product, int quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
```

Define Customer, Order and Product models in the same way. Use the following data types: int, String, Instant.

## Single-table DAO

The next step is to define DAO (Database Access Object) classes with the database tables and the model selection
methods as follows:

```java
public abstract class CustomerDao {

    private static final Table TABLE = new Table("customer");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn NAME = TABLE.get("name");
    private static final TableColumn CITY = TABLE.get("city");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<Customer> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn name = query.addToSelection(ref.get(NAME));
        ResultColumn city = query.addToSelection(ref.get(CITY));

        joiner.accept(ref.get(ID));

        return rs -> new Customer(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readString(rs, city.getIndex()));
    }
}
```

Private static constants define the table and its columns. The method adds the table columns to a SelectQuery, adds
custom criterias determined by `joiner` function and returns a ResultMapper instance allowing you to map raw result
records to new model instances. This method provides a generalized way to obtain Customer instances from database
by their primary key, regardless of the other tables involved in the query. The simplest use case is a Customer
retrieving directly by its primary key:

```java
    public static Customer select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<Customer> mapper = addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, MatchCriteria.EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }
```

Notice that all low-level record mapping code is hidden in `addToQuery` method, so this high-level `select` function
does exactly as much as its names states - makes a select query to this table with a certain criteria. You don't need
to copy and paste big portions of the SQL code query over and over again just to customize selection criterias or join
this table with the other tables, and we'll demonstrate it soon.

## Multi-table DAO

You can use the same approach to define DAO classes for database tables containing references to the other tables. Just
call `addToQuery` methods of referred tables to transparently add them to a query:

```java
public abstract class OrderDao {

    private static final Table TABLE = new Table("order");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn ISSUED_AT = TABLE.get("issued_at");
    private static final TableColumn CUSTOMER_ID = TABLE.get("customer_id");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<Order> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn issuedAt = query.addToSelection(ref.get(ISSUED_AT));

        ResultMapper<Customer> customer = CustomerDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(CUSTOMER_ID)));
        });

        joiner.accept(ref.get(ID));

        return rs -> new Order(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readInstant(rs, issuedAt.getIndex()),
                customer.apply(rs));
    }
}
```

Notice haw we use `CustomerDao.addToQuery` call to join orders with customers in a single query. Then you just need to
call `customer.apply(rs)` function to read a proper Customer instance referred by the Order.

Now you can easily select full Order instances in the same way as above:

```java
    public static Order select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<Order> mapper = addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, MatchCriteria.EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }
```

Let's define the remaining tables to demonstrate the consistency of this approach regardless of number of tables
involved, as OrderItem selection involves all 4 tables into the query:

```java
public abstract class ProductDao {

    private static final Table TABLE = new Table("product");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn NAME = TABLE.get("name");
    private static final TableColumn PRICE = TABLE.get("price");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<Product> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn name = query.addToSelection(ref.get(NAME));
        ResultColumn price = query.addToSelection(ref.get(PRICE));

        joiner.accept(ref.get(ID));

        return rs -> new Product(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readIntegerNotNull(rs, price.getIndex()));
    }
}

public abstract class OrderItemDao {

    private static final Table TABLE = new Table("order_item");
    private static final TableColumn ID = TABLE.get("id");
    private static final TableColumn ORDER_ID = TABLE.get("order_id");
    private static final TableColumn PRODUCT_ID = TABLE.get("product_id");
    private static final TableColumn QUANTITY = TABLE.get("quantity");

    public interface Joiner {

        void accept(Selectable idRef);
    }

    public static ResultMapper<OrderItem> addToQuery(SelectQuery query, Joiner joiner) {
        TableReference ref = TABLE.refer();

        ResultColumn id = query.addToSelection(ref.get(ID));
        ResultColumn quantity = query.addToSelection(ref.get(QUANTITY));

        ResultMapper<Order> order = OrderDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(ORDER_ID)));
        });

        ResultMapper<Product> product = ProductDao.addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, ref.get(PRODUCT_ID)));
        });

        joiner.accept(ref.get(ID));

        return rs -> new OrderItem(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                order.apply(rs),
                product.apply(rs),
                JdbcUtils.readIntegerNotNull(rs, quantity.getIndex()));
    }
}
```

Selection query of complex OrderItem instance is still consistently easy:

```java
    public static OrderItem select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        ResultMapper<OrderItem> mapper = addToQuery(query, idRef -> {
            query.addCriteria(new MatchCriteria(idRef, EQUALS, Parameter.of(id)));
        });
        return JdbcUtils.selectOne(query, connection, mapper);
    }
```

Notice that all work above is made mostly with static functions with short lists of arguments. It means that the API is
granular, easy to use and to cover with tests.

Notice that you don't really need to worry about the text of the SQL query behind all this Java code. With just a bunch
of simple and clear Java classes we've managed to transform Squiggle SQL query builder to a fully-capable database
communication framework. Meanwhile, it is very different from the alternative frameworks:

* As opposed to Hibernate, Squiggle SQL doesn't use any reflection. There's no magic behind it, but pure
object-oriented design.
* As opposed to ScalikeJDBC, Squiggle SQL doesn't force you to write raw SQL code or respect the original order of
QueryDSL calls. Say no to copy & paste.
