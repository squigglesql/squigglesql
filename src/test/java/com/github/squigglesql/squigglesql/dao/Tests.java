package com.github.squigglesql.squigglesql.dao;

import com.github.squigglesql.squigglesql.databases.TestDatabase;
import com.github.squigglesql.squigglesql.databases.TestDatabaseColumn;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;

import static com.github.squigglesql.squigglesql.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Tests {

    private static final TestDatabaseColumn[] CUSTOMER_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(CustomerDao.NAME.getName(), "TEXT", true, null),
            new TestDatabaseColumn(CustomerDao.CITY.getName(), "TEXT", true, null)
    };

    private static final TestDatabaseColumn[] ORDER_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(OrderDao.ISSUED_AT.getName(), "TIMESTAMP WITH TIME ZONE", true, null),
            new TestDatabaseColumn(OrderDao.CUSTOMER_ID.getName(), "INTEGER", true, CustomerDao.TABLE.getName())
    };

    private static final TestDatabaseColumn[] PRODUCT_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(ProductDao.NAME.getName(), "TEXT", true, null),
            new TestDatabaseColumn(ProductDao.PRICE.getName(), "INTEGER", true, null)
    };

    private static final TestDatabaseColumn[] ORDER_ITEM_COLUMNS = new TestDatabaseColumn[]{
            new TestDatabaseColumn(OrderItemDao.ORDER_ID.getName(), "INTEGER", true, OrderDao.TABLE.getName()),
            new TestDatabaseColumn(OrderItemDao.PRODUCT_ID.getName(), "INTEGER", true, ProductDao.TABLE.getName()),
            new TestDatabaseColumn(OrderItemDao.QUANTITY.getName(), "INTEGER", true, null)
    };

    private static final String ATLANTA = "Atlanta";
    private static final String BERLIN = "Berlin";
    private static final String CANBERRA = "Canberra";

    private static final Customer AARON = new Customer(1, "Aaron", ATLANTA);
    private static final Customer BOB = new Customer(2, "Bob", BERLIN);
    private static final Customer CHRIS = new Customer(3, "Chris", ATLANTA);
    private static final Customer DANIEL = new Customer(4, "Daniel", BERLIN);

    private static final Customer[] CUSTOMERS = new Customer[]{AARON, BOB, CHRIS, DANIEL};
    private static final Customer[] CUSTOMERS_ATLANTA = new Customer[]{AARON, CHRIS};
    private static final Customer[] CUSTOMERS_BERLIN = new Customer[]{BOB, DANIEL};

    private static final Product APPLE = new Product(1, "Apple", 10);
    private static final Product BANANA = new Product(2, "Banana", 12);
    private static final Product CABBAGE = new Product(3, "Cabbage", 30);
    private static final Product DAIKON = new Product(4, "Daikon", 20);

    private static final Product[] PRODUCTS = new Product[]{APPLE, BANANA, CABBAGE, DAIKON};

    private static final Order[] ORDERS = new Order[]{
            new Order(1, OffsetDateTime.of(2019, 1, 5, 13, 0, 0, 0, ZoneOffset.UTC).toInstant(), AARON),
            new Order(2, OffsetDateTime.of(2019, 1, 3, 10, 0, 0, 0, ZoneOffset.UTC).toInstant(), CHRIS),
            new Order(3, OffsetDateTime.of(2019, 3, 1, 20, 0, 0, 0, ZoneOffset.UTC).toInstant(), BOB),
            new Order(4, OffsetDateTime.of(2019, 3, 15, 8, 0, 0, 0, ZoneOffset.UTC).toInstant(), CHRIS),
            new Order(5, OffsetDateTime.of(2019, 3, 15, 9, 0, 0, 0, ZoneOffset.UTC).toInstant(), CHRIS),
            new Order(6, OffsetDateTime.of(2019, 4, 1, 10, 0, 0, 0, ZoneOffset.UTC).toInstant(), AARON)
    };
    private static final Order[] ORDERS_ATLANTA = new Order[]{ORDERS[0], ORDERS[1], ORDERS[3], ORDERS[4], ORDERS[5]};
    private static final Order[] ORDERS_BERLIN = new Order[]{ORDERS[2]};

    private static final OrderItem[] ORDER_ITEMS = new OrderItem[]{
            new OrderItem(1, ORDERS[0], BANANA, 3),
            new OrderItem(2, ORDERS[0], DAIKON, 1),
            new OrderItem(3, ORDERS[1], APPLE, 10),
            new OrderItem(4, ORDERS[2], CABBAGE, 4),
            new OrderItem(5, ORDERS[3], BANANA, 1),
            new OrderItem(6, ORDERS[3], APPLE, 1),
            new OrderItem(7, ORDERS[3], DAIKON, 1),
            new OrderItem(8, ORDERS[4], CABBAGE, 5),
            new OrderItem(9, ORDERS[5], APPLE, 1)
    };

    @Test
    public void testCustomerSelect() throws SQLException {
        withContents((connection, database) -> {
            for (int i = 0; i < CUSTOMERS.length; ++i) {
                assertEquals(CUSTOMERS[i], CustomerDao.select(connection, i + 1));
            }
            assertNull(CustomerDao.select(connection, CUSTOMERS.length + 1));
        });
    }

    @Test
    public void testCustomerSelectByCity() throws SQLException {
        withContents((connection, database) -> {
            assertEquals(Arrays.asList(CUSTOMERS_ATLANTA), CustomerDao.selectByCity(connection, ATLANTA));
            assertEquals(Arrays.asList(CUSTOMERS_BERLIN), CustomerDao.selectByCity(connection, BERLIN));
            assertEquals(Collections.emptyList(), CustomerDao.selectByCity(connection, CANBERRA));
        });
    }

    @Test
    public void testOrderSelect() throws SQLException {
        withContents((connection, database) -> {
            for (int i = 0; i < ORDERS.length; ++i) {
                assertEquals(ORDERS[i], OrderDao.select(connection, i + 1));
            }
            assertNull(CustomerDao.select(connection, ORDERS.length + 1));
        });
    }

    @Test
    public void testOrderSelectByCity() throws SQLException {
        withContents((connection, database) -> {
            assertEquals(Arrays.asList(ORDERS_ATLANTA), OrderDao.selectByCity(connection, ATLANTA));
            assertEquals(Arrays.asList(ORDERS_BERLIN), OrderDao.selectByCity(connection, BERLIN));
            assertEquals(Collections.emptyList(), OrderDao.selectByCity(connection, CANBERRA));
        });
    }

    @Test
    public void testOrderItemSelect() throws SQLException {
        withContents((connection, database) -> {
            for (int i = 0; i < ORDER_ITEMS.length; ++i) {
                assertEquals(ORDER_ITEMS[i], OrderItemDao.select(connection, i + 1));
            }
            assertNull(OrderItemDao.select(connection, ORDER_ITEMS.length + 1));
        });
    }

    private static void withContents(Consumer consumer) throws SQLException {
        withDatabase(
                (connection, database) -> withTables(connection, database,
                        () -> withRecords(connection, () -> {
                            consumer.accept(connection, database);
                            return null;
                        })));
    }

    private static <T> T withTables(Connection connection, TestDatabase database, Supplier<T> supplier) throws SQLException {
        return withTable(connection, database, CustomerDao.TABLE.getName(), CUSTOMER_COLUMNS,
                () -> withTable(connection, database, OrderDao.TABLE.getName(), ORDER_COLUMNS,
                        () -> withTable(connection, database, ProductDao.TABLE.getName(), PRODUCT_COLUMNS,
                                () -> withTable(connection, database, OrderItemDao.TABLE.getName(), ORDER_ITEM_COLUMNS, supplier))));
    }

    private static <T> T withRecords(Connection connection, Supplier<T> supplier) throws SQLException {
        for (Customer customer : CUSTOMERS) {
            CustomerDao.insert(connection, customer.getName(), customer.getCity());
        }
        for (Order order : ORDERS) {
            OrderDao.insert(connection, order.getIssuedAt(), order.getCustomer());
        }
        for (Product product : PRODUCTS) {
            ProductDao.insert(connection, product.getName(), product.getPrice());
        }
        for (OrderItem orderItem : ORDER_ITEMS) {
            OrderItemDao.insert(connection, orderItem.getOrder(), orderItem.getProduct(), orderItem.getQuantity());
        }
        return supplier.get();
    }
}
