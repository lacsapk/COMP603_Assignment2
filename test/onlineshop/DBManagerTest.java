package onlineshop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DBManagerTest {

    private DBManager instance;

    public DBManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before // Setup before test
    public void setUp() {
        instance = new DBManager();
        instance.establishConnection();
    }

    @After // to clean up after test
    public void tearDown() {
        instance.closeConnection();
    }

    @Test
    public void testSaveOrder() {
        System.out.println("saveOrder");
        String modelName = "TestModel";
        String manufacturer = "TestManufacturer";
        int amount = 10;
        double totalCost = 1000.0;
        // saves the created test Order to the table Orders
        instance.saveOrder(modelName, manufacturer, amount, totalCost);

        // gets a list of Strings from the table Orders
        List<String> orders = instance.getOrders();
        // Checks that the list isnt null
        assertNotNull("Orders should be retrievable", orders);
        // Checks that the test Order is in the list
        assertTrue("Order should be saved", orders.stream().anyMatch(order -> order.contains("TestModel")));
    }

    @Test
    public void testGetOrders() {
        System.out.println("getOrders");
        List<String> orders = instance.getOrders();
        assertNotNull("Orders should be retrievable", orders); // just checks that the list isnt null
        assertFalse("Order list should not be empty", orders.isEmpty()); // checks that the list isnt empty
    }

    @Test
    public void testUpdateProducts() {
        System.out.println("updateProducts");
        Inventory inventory = new Inventory();
        // Creates a new Product and adds it to the inventory
        Product product1 = new Product(1, "TestModel1", "TestManufacturer1", 100.0);
        inventory.add(product1);

        // creates the Table Product and adds the created inventory
        instance.createProductTable();
        instance.updateProducts(inventory);

        // Changes the value of a product in the inventory and updates it to the table Product
        product1.set_retail_price(150.0);
        instance.updateProducts(inventory);

        // Gets the Products from the DB and checks if the price is updated 
        List<Product> productsFromDb = instance.getProductsFromDatabase();
        assertNotNull("Products should be retrievable from the database", productsFromDb); //Checks that its not null
        // Checks that the created product has the new value
        assertTrue("Product in the database should be updated with the new price",
                productsFromDb.stream().anyMatch(p -> p.get_model_name().equals("TestModel1") && p.get_retail_price() == 150.0));
    }

    @Test
    public void testGetProductsFromDatabase() {
        System.out.println("getProductsFromDatabase");
        List<Product> result = instance.getProductsFromDatabase();
        assertNotNull("Products should be retrievable from the database", result);
        assertFalse("Product list should not be empty", result.isEmpty());
    }

    @Test
    public void testCreateProductTable() {
        // First part Checks if the Table has been created
        System.out.println("createProductTable");
        instance.createProductTable();
        boolean tableExists = instance.checkTableExists("PRODUCT");
        assertTrue("Product table should be created", tableExists);

        // Defines the Columns and valuetypes the table must have
        String[] expectedColumns = {"ID", "NAME", "MANUFACTURER", "PRICE", "RATING", "REVIEWS"};
        String[] expectedTypes = {"INT", "VARCHAR", "VARCHAR", "DOUBLE", "DOUBLE", "INT"};

        // gets all the values from the table Product
        try ( Statement stmt = instance.getConnection().createStatement();  ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT")) {

            // get the information from the received data
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Check if the number of columns matches
            assertEquals("Number of columns should match", expectedColumns.length, columnCount);

            // Check each columns name and type
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i).toUpperCase();
                String columnType = metaData.getColumnTypeName(i).toUpperCase();
                assertEquals("Column name should match", expectedColumns[i - 1], columnName);
                // ignore diffrences between Int and Integer aswell as Double and Decimal
                boolean typeMatches = columnType.equals(expectedTypes[i - 1])
                        || (expectedTypes[i - 1].equals("INT") && columnType.equals("INTEGER"))
                        || (expectedTypes[i - 1].equals("DOUBLE") && columnType.equals("DECIMAL"));
                assertTrue("Column type should match", typeMatches);
            }
        } catch (SQLException e) {
            fail("SQLException occurred while checking table structure: " + e.getMessage());
        }
    }

    @Test
    // Checks if the Table has been created
    public void testCreateOrderTable() {
        System.out.println("createOrderTable");
        instance.createOrderTable();
        // Assuming there is a method to check if the table exists
        boolean tableExists = instance.checkTableExists("ORDERS");
        assertTrue("Order table should be created", tableExists);
    }
}
