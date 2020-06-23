[![Build Status via Travis CI](https://api.travis-ci.org/squigglesql/squigglesql.svg?branch=master)](https://travis-ci.org/squigglesql/squigglesql)
[![Coverage Status](https://coveralls.io/repos/github/squigglesql/squigglesql/badge.svg?branch=master)](https://coveralls.io/github/squigglesql/squigglesql?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.squigglesql/squigglesql/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.squigglesql/squigglesql)

Squiggle SQL is a little Java library for dynamic generation of SQL queries. Its sweet spot is applications
that need to build up complicated queries with criterias that change in runtime. Ordinarily it can be quite painful to
figure out how to build this string. Squiggle SQL takes much of this pain away.

# Features

* Concise and intuitive API.
* No dependencies on classes outside of JDK 1.8.
* Small, lightweight, fast.
* Generates clean SQL designed that is very human readable.
* Supports all basic SQL features.
* Supports JDBC PreparedStatement compilation.
* Supports MySQL, PostgreSQL, [H2](https://www.h2database.com) and custom SQL syntax.
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

select.addCriteria(Criteria.notLess(e.get(employeeAge), Literal.of(18)));

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
Table order = new Table("ordr");
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
select.addCriteria(Criteria.equal(o.get(orderStatus), new TypeCast(Literal.of("processed"), "status")));
select.addCriteria(Criteria.less(o.get(orderItems), Literal.of(5)));
select.addCriteria(Criteria.in(o.get(orderDelivery),
    Literal.of("post"), Literal.of("fedex"), Literal.of("goat")));

// join
TableReference w = warehouse.refer();

select.addFrom(new QualifiedJoin(o, QualifiedJoinKind.INNER, w,
        Criteria.equal(o.get(orderWarehouseId), w.get(warehouseId))));

// use joined table
select.addToSelection(w.get(warehouseLocation));
select.addCriteria(Criteria.notDistinct(w.get(warehouseSize), Literal.of("big")));

// build subselect query
TableReference f = offer.refer();

SelectQuery subSelect = new SelectQuery();

subSelect.addToSelection(f.get(offerLocation));
subSelect.addCriteria(Criteria.equal(f.get(offerValid), Literal.of(true)));

// add subselect to original query
select.addCriteria(Criteria.in(w.get(warehouseLocation), subSelect));

select.toString();
```

Which produces:

```SQL
SELECT
    o.id,
    o.total_price,
    w.location
FROM
    ordr o
    INNER JOIN warehouse w ON o.warehouse_id = w.id
WHERE
    o.status = 'processed'::status AND
    o.items < 5 AND
    o.delivery IN ('post', 'fedex', 'goat') AND
    w.size IS NOT DISTINCT FROM 'big' AND
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

select.addCriteria(Criteria.less(e.get(employeeAge), Parameter.of(30)));

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
TableColumn employeeId = employee.get("id");
TableColumn employeeName = employee.get("name");
TableColumn employeeAge = employee.get("age");

// build query
TableReference e = employee.refer();

SelectQuery select = new SelectQuery();

ResultColumn employeeIdResult = select.addToSelection(e.get(employeeId));
ResultColumn employeeNameResult = select.addToSelection(e.get(employeeName));
ResultColumn employeeAgeResult = select.addToSelection(e.get(employeeAge));

try (PreparedStatement statement = select.toStatement(new JdbcStatementCompiler(connection))) {
    try (ResultSet rs = statement.executeQuery()) {
        rs.next();
        int id = JdbcUtils.readIntegerNotNull(rs, employeeIdResult.getIndex());
        String name = JdbcUtils.readString(rs, employeeNameResult.getIndex());
        int age = JdbcUtils.readIntegerNotNull(rs, employeeAgeResult.getIndex());
    }
}
```

## Query execution helpers

You may use JdbcUtils methods selectOne, selectAll, insert and update to execute the queries. So, in the example above,
both try/catch blocks can be replaced with a single JdbcUtils.selectOne call.

```java
JdbcUtils.selectOne(select, connection, rs -> new Employee(
        JdbcUtils.readIntegerNotNull(rs, employeeIdResult.getIndex()),
        JdbcUtils.readString(rs, employeeNameResult.getIndex()),
        JdbcUtils.readIntegerNotNull(rs, employeeAgeResult.getIndex())));
```

You just need to define Employee model class for this.

## Insertion queries

To obtain a generated key of the insertion query, you must define a mapper.

```java
Table employee = new Table("employee");
TableColumn employeeName = employee.get("name");
TableColumn employeeAge = employee.get("age");

InsertQuery insert = new InsertQuery(employee);
insert.addValue(employeeName, Parameter.of("Homer"));
insert.addValue(employeeAge, Parameter.of(40));
int id = JdbcUtils.insert(insert, connection, rs -> rs.getInt(1));
```

# Best practices

See full source code of the tutorial here
https://github.com/squigglesql/squigglesql/tree/master/src/test/java/com/github/squigglesql/squigglesql/dao

## Intro

This topic describes how to better organize your Java classes to gain a full advantage from dynamic SQL compilation.
You shouldn't treat these patterns as a dogma - instead, you should take them as a base template for further code
extensions.

Assuming that you have the following tables:

* **customer** (id, name, city)
* **order** (id, issued_at, **customer_id**)
* **product** (id, name, price)
* **order_item** (id, **order_id**, **product_id**, quantity)

You should start with the following code.

## DAO classes

The first step is to define DAO (Database Access Object) classes with the database table and column definitions as
follows:

```java
public class CustomerDao {

    static final Table TABLE = new Table("customer");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");
    static final TableColumn CITY = TABLE.get("city");
}

public class OrderDao {

    static final Table TABLE = new Table("order");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn ISSUED_AT = TABLE.get("issued_at");
    static final TableColumn CUSTOMER_ID = TABLE.get("customer_id");
}

public class ProductDao {

    static final Table TABLE = new Table("product");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn NAME = TABLE.get("name");
    static final TableColumn PRICE = TABLE.get("price");
}

public class OrderItemDao {

    static final Table TABLE = new Table("order_item");

    static final TableColumn ID = TABLE.get("id");
    static final TableColumn ORDER_ID = TABLE.get("order_id");
    static final TableColumn PRODUCT_ID = TABLE.get("product_id");
    static final TableColumn QUANTITY = TABLE.get("quantity");
}
```

## Model classes

Let's define 4 simple immutable Java models. Example for **order_item** table:

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

Notice that model classes should not necessarily reflect all table columns. You may use multiple model classes for a
single database table containing data from different sets of columns.

## Model selection

Let's define `select` static method in CustomerDao to select a Customer by its identifier.

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;

...

    public static Customer select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        CustomerMapper mapper = new CustomerMapper(query);
        query.addCriteria(equal(mapper.getIdRef(), Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }
```

CustomerMapper object should add all the necessary columns to the query and provide a way to map the selection rows to
Customer object instances. Let's implement it as follows:

```java
import static com.github.squigglesql.squigglesql.dao.CustomerDao.*;

public class CustomerMapper implements ResultMapper<Customer> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn name;
    private final ResultColumn city;

    public CustomerMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        name = query.addToSelection(ref.get(NAME));
        city = query.addToSelection(ref.get(CITY));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    @Override
    public Customer apply(ResultSet rs) throws SQLException {
        return new Customer(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readString(rs, city.getIndex()));
    }
}
```

This class provides a generalized way to obtain Customer instances from database, regardless of matching criterias and
the other tables involved in the query. Notice that all low-level record mapping code is hidden in this class, so
the high-level `select` function does exactly as much as its name states - makes a select query to this table with
a specific criteria. You don't need to copy and paste big portions of the SQL code query over and over again just to
customize selection criterias or join this table with the other tables, and we'll demonstrate it soon.

## Table joins

You can use the same approach to define mappers for models distributed across several tables. Just reuse the existing
ResultMappers in new ones:

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;
import static com.github.squigglesql.squigglesql.dao.OrderDao.*;

public class OrderMapper implements ResultMapper<Order> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn issuedAt;
    private final CustomerMapper customer;

    public OrderMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        issuedAt = query.addToSelection(ref.get(ISSUED_AT));

        customer = new CustomerMapper(query);
        query.addCriteria(equal(customer.getIdRef(), ref.get(CUSTOMER_ID)));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    @Override
    public Order apply(ResultSet rs) throws SQLException {
        return new Order(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readInstant(rs, issuedAt.getIndex()),
                customer.apply(rs));
    }
}
```

Notice how we instantiate a CustomerMapper object to join orders with customers in a single query. Then we just call
`customer.apply(rs)` function to read a proper Customer instance referred by the Order.

Now you can easily select full Order instances in the same way as Customer instances. Add the following method to
OrderDao:

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;

...

    public static Order select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderMapper mapper = new OrderMapper(query);
        query.addCriteria(equal(mapper.getIdRef(), Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }
```

Let's define the remaining ResultMappers to demonstrate the consistency of this approach regardless of number of
tables involved, as OrderItem selection involves all 4 tables into the query:

```java
import static com.github.squigglesql.squigglesql.dao.ProductDao.*;

public class ProductMapper implements ResultMapper<Product> {

    private final TableReference ref;

    private final ResultColumn id;
    private final ResultColumn name;
    private final ResultColumn price;

    public ProductMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        name = query.addToSelection(ref.get(NAME));
        price = query.addToSelection(ref.get(PRICE));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    @Override
    public Product apply(ResultSet rs) throws SQLException {
        return new Product(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                JdbcUtils.readString(rs, name.getIndex()),
                JdbcUtils.readIntegerNotNull(rs, price.getIndex()));
    }
}
```

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;
import static com.github.squigglesql.squigglesql.dao.OrderItemDao.*;

public class OrderItemMapper implements ResultMapper<OrderItem> {

    private final TableReference ref;

    private final ResultColumn id;
    private final OrderMapper order;
    private final ProductMapper product;
    private final ResultColumn quantity;

    public OrderItemMapper(SelectQuery query) {
        ref = TABLE.refer();

        id = query.addToSelection(ref.get(ID));
        quantity = query.addToSelection(ref.get(QUANTITY));

        order = new OrderMapper(query);
        query.addCriteria(equal(order.getIdRef(), ref.get(ORDER_ID)));

        product = new ProductMapper(query);
        query.addCriteria(equal(product.getIdRef(), ref.get(PRODUCT_ID)));
    }

    public Selectable getIdRef() {
        return ref.get(ID);
    }

    @Override
    public OrderItem apply(ResultSet rs) throws SQLException {
        return new OrderItem(
                JdbcUtils.readIntegerNotNull(rs, id.getIndex()),
                order.apply(rs),
                product.apply(rs),
                JdbcUtils.readIntegerNotNull(rs, quantity.getIndex()));
    }
}
```

The selection query of a complex OrderItem instance in OrderDao is still consistently easy:

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;

...

    public static OrderItem select(Connection connection, int id) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderItemMapper mapper = new OrderItemMapper(query);
        query.addCriteria(equal(mapper.getIdRef(), EQUALS, Parameter.of(id)));
        return JdbcUtils.selectOne(query, connection, mapper);
    }
```

## Other selection criterias

What if we want to use a different selection criteria except primary keys? With this architecture, this is still not a
big challenge. Just expose the necessary table column references in ResultMapper implementations. Let's start with
selection of customers by a city. Add the following method to CustomerMapper:

```java
    public Selectable getCityRef() {
        return ref.get(CITY);
    }
```

Now the selection function in CustomerDao can be implemented as simple as this:

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;

...

    public static List<Customer> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        CustomerMapper mapper = new CustomerMapper(query);
        query.addCriteria(equal(mapper.getCityRef(), EQUALS, Parameter.of(city)));
        query.addOrder(mapper.getIdRef(), true);
        return JdbcUtils.selectAll(query, connection, mapper);
    }
```

So, basically, you just need to list all significant selection columns in ResultMapper implementations, and it opens
a possibility for you to use them in criterias.

You can relay the selection columns down the hierarchy. For example, to select orders by city, you need to define
getCityRef methods in OrderMapper:

```java
    public Selectable getCityRef() {
        return customer.getCityRef();
    }
```

Now the selection method in OrderDao can be easily implemented:

```java
import static com.github.squigglesql.squigglesql.criteria.Criteria.*;

...

    public static List<Order> selectByCity(Connection connection, String city) throws SQLException {
        SelectQuery query = new SelectQuery();
        OrderMapper mapper = new OrderMapper(query);
        query.addCriteria(equal(mapper.getCityRef(), EQUALS, Parameter.of(city)));
        query.addOrder(mapper.getIdRef(), true);
        return JdbcUtils.selectAll(query, connection, mapper);
    }
```

## Conclusion

Notice that Dao classes are built with static functions with short lists of arguments. It means that the API is
granular, easy to use and to cover with tests.

Notice that you don't really need to worry about the text of the SQL query behind all this Java code. With just a bunch
of simple and clear Java classes we've managed to transform Squiggle SQL query builder to a fully-capable database
interaction framework. Meanwhile, it is very different from the alternative frameworks:

* As opposed to Hibernate, Squiggle SQL doesn't use any reflection. There's no magic behind it, but pure
object-oriented design.
* As opposed to ScalikeJDBC, Squiggle SQL doesn't force you to write raw SQL code or respect the original order of
QueryDSL calls. It means that you don't need to copy & paste big chunks of code anymore.

See full source code of the tutorial here
https://github.com/squigglesql/squigglesql/tree/master/src/test/java/com/github/squigglesql/squigglesql/dao
