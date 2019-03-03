package network.tide.squiggle.dao;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static network.tide.squiggle.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Tests {

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
        withContents(connection -> {
            for (int i = 0; i < CUSTOMERS.length; ++i) {
                assertEquals(CUSTOMERS[i], CustomerDao.select(connection, i + 1));
            }
            assertNull(CustomerDao.select(connection, CUSTOMERS.length + 1));
        });
    }

//    @Test
//    public void testCustomerSelectByCity() throws SQLException {
//        withContents(connection -> {
//            assertEquals(Arrays.asList(CUSTOMERS_ATLANTA), CustomerDao.selectByCity(connection, ATLANTA));
//            assertEquals(Arrays.asList(CUSTOMERS_BERLIN), CustomerDao.selectByCity(connection, BERLIN));
//            assertEquals(Collections.emptyList(), CustomerDao.selectByCity(connection, CANBERRA));
//        });
//    }

    @Test
    public void testOrderSelect() throws SQLException {
        withContents(connection -> {
            for (int i = 0; i < ORDERS.length; ++i) {
                assertEquals(ORDERS[i], OrderDao.select(connection, i + 1));
            }
            assertNull(CustomerDao.select(connection, ORDERS.length + 1));
        });
    }

//    @Test
//    public void testOrderSelectByCity() throws SQLException {
//        withContents(connection -> {
//            assertEquals(Arrays.asList(ORDERS_ATLANTA), OrderDao.selectByCity(connection, ATLANTA));
//            assertEquals(Arrays.asList(ORDERS_BERLIN), OrderDao.selectByCity(connection, BERLIN));
//            assertEquals(Collections.emptyList(), OrderDao.selectByCity(connection, CANBERRA));
//        });
//    }

    @Test
    public void testOrderItemSelect() throws SQLException {
        withContents(connection -> {
            for (int i = 0; i < ORDER_ITEMS.length; ++i) {
                assertEquals(ORDER_ITEMS[i], OrderItemDao.select(connection, i + 1));
            }
            assertNull(OrderItemDao.select(connection, ORDER_ITEMS.length + 1));
        });
    }

    private static void withContents(Consumer consumer) throws SQLException {
        withDatabase(
                connection -> withSequences(connection,
                        () -> withTables(connection,
                                () -> withRecords(connection, () -> {
                                    consumer.accept(connection);
                                    return null;
                                }))));
    }

    private static <T> T withSequences(Connection connection, Supplier<T> supplier) throws SQLException {
        return withSequence(connection, "customer_id_seq",
                () -> withSequence(connection, "order_id_seq",
                        () -> withSequence(connection, "product_id_seq",
                                () -> withSequence(connection, "order_item_id_seq", supplier))));
    }

    private static <T> T withTables(Connection connection, Supplier<T> supplier) throws SQLException {
        String customer = "CREATE TABLE customer (\n" +
                "id INTEGER DEFAULT nextval('customer_id_seq'::regclass) NOT NULL PRIMARY KEY,\n" +
                "name TEXT NOT NULL,\n" +
                "city TEXT NOT NULL\n" +
                ")";
        String order = "CREATE TABLE \"order\" (\n" +
                "id INTEGER DEFAULT nextval('order_id_seq'::regclass) NOT NULL PRIMARY KEY,\n" +
                "issued_at TIMESTAMP WITH TIME ZONE NOT NULL,\n" +
                "customer_id INT NOT NULL REFERENCES customer\n" +
                ")";
        String product = "CREATE TABLE product (\n" +
                "id INTEGER DEFAULT nextval('product_id_seq'::regclass) NOT NULL PRIMARY KEY,\n" +
                "name TEXT NOT NULL,\n" +
                "price INTEGER NOT NULL\n" +
                ")";
        String orderItem = "CREATE TABLE order_item (\n" +
                "id INTEGER DEFAULT nextval('order_item_id_seq'::regclass) NOT NULL,\n" +
                "order_id INTEGER NOT NULL REFERENCES \"order\",\n" +
                "product_id INTEGER NOT NULL REFERENCES product,\n" +
                "quantity INTEGER NOT NULL\n" +
                ")";
        return withTable(connection, "customer", customer,
                () -> withTable(connection, "order", order,
                        () -> withTable(connection, "product", product,
                                () -> withTable(connection, "order_item", orderItem, supplier))));
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
