package onlineshop;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String URL = "jdbc:derby:ShopDB;create=true";
    private Connection conn;

    public DBManager() {
        establishConnection();
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void establishConnection() {
        if (this.conn == null) {
            try {
                conn = DriverManager.getConnection(URL);
                System.out.println("Embedded Derby database connected successfully.");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Closing connection to database...");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Checks if a Table with the Products already exists, if not it gets added
    public void initializeProductTable() {
        if (!checkTableExists("PRODUCT")) {
            createProductTable();
            System.out.println("Product table created and populated with initial data.");
        } else {
            System.out.println("Product table already exists.");
        }
    }

    // Checks if a Table with the Orders already exists, if not it gets added
    public void initializeOrderTable() {
        if (!checkTableExists("ORDERS")) {
            createOrderTable();
            System.out.println("Orders table created.");
        } else {
            System.out.println("Orders table already exists.");
        }
    }

    // Starts the process of loading Products from the DB and adding them to the inventory as products
    public void initializeInventory(Inventory inventory) {
        List<Product> products = getProductsFromDatabase();
        for (Product product : products) {
            inventory.add(product);
        }
    }

    // Checks if the Table already exists
    public boolean checkTableExists(String tableName) {
        boolean exists = false;
        try {
            DatabaseMetaData dbmd = this.conn.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, tableName.toUpperCase(), null);

            if (rs.next()) {
                exists = true;
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return exists;
    }

    // Creates the Table called Product
    public void createProductTable() {
        try {
            Statement statement = conn.createStatement();

            // Create the PRODUCT table
            statement.addBatch("CREATE TABLE PRODUCT (ID INT PRIMARY KEY, NAME VARCHAR(100), MANUFACTURER VARCHAR(100), PRICE DOUBLE, RATING DOUBLE, REVIEWS INT)");

            // Insert initial data using a single batch
            statement.addBatch("INSERT INTO PRODUCT VALUES (1, 'PlayStation 5', 'PlayStation inc', 999.00, 3.37, 12), "
                    + "(2, 'Samsung Galaxy', 'Samsung Electronics', 1400.00, 3.55, 4), "
                    + "(3, 'Nintendo Switch', 'Nintendo Corporation', 639.00, 4.60, 5), "
                    + "(4, 'Xbox', 'Microsoft Corporation', 1100.00, 4.95, 2), "
                    + "(5, 'Iphone 14', 'Apple Technology Company', 2200.00, 3.50, 5)");

            // Execute the batch
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // creates the Table called Order
    public void createOrderTable() {
        try {
            Statement statement = conn.createStatement();

            // Create the ORDERS table
            statement.addBatch("CREATE TABLE ORDERS (ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), AMOUNT INT, MODELNAME VARCHAR(100), MANUFACTURER VARCHAR(100), TOTAL DOUBLE)");

            // Execute the batch to create the table
            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Saves an order to the Table Orders
    public void saveOrder(String modelName, String manufacturer, int amount, double totalCost) {
        String insertSQL = "INSERT INTO ORDERS (AMOUNT, MODELNAME, MANUFACTURER, TOTAL) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, amount);
            pstmt.setString(2, modelName);
            pstmt.setString(3, manufacturer);
            pstmt.setDouble(4, totalCost);
            pstmt.executeUpdate();
            System.out.println("Order has been placed.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Displays the Table Orders
    public void viewOrders() {
        String querySQL = "SELECT AMOUNT, MODELNAME, MANUFACTURER, TOTAL FROM ORDERS";
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(querySQL)) {
            System.out.println("\nCurrent Orders:");
            while (rs.next()) {
                int amount = rs.getInt("AMOUNT");
                String modelName = rs.getString("MODELNAME");
                String manufacturer = rs.getString("MANUFACTURER");
                double total = rs.getDouble("TOTAL");

                // Simplified output using string concatenation
                String orderInfo = amount + " x " + modelName + " by " + manufacturer + " for a total of: " + String.format("%.2f", total);
                System.out.println(orderInfo);
            }
        } catch (SQLException ex) {
            System.out.println("Error reading from database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Updates the Table Product with the new values
    public void updateProducts(Inventory inventory) {
        String updateSQL = "UPDATE PRODUCT SET NAME = ?, MANUFACTURER = ?, PRICE = ?, RATING = ?, REVIEWS = ? WHERE ID = ?";

        try ( PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            for (Product product : inventory.getAllProducts()) {
                if (product != null) {
                    pstmt.setString(1, product.get_model_name());
                    pstmt.setString(2, product.get_name_of_manufacturer());
                    pstmt.setDouble(3, product.get_retail_price());
                    pstmt.setDouble(4, product.get_reliability_rating());
                    pstmt.setInt(5, product.get_no_of_reviews());
                    pstmt.setInt(6, product.get_id());

                    // Add to batch
                    pstmt.addBatch();
                }
            }
            // Execute batch update
            pstmt.executeBatch();
        } catch (SQLException ex) {
            System.out.println("Error updating database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Loads the Products from the Database and adds them as products (Class Product)
    public List<Product> getProductsFromDatabase() {
        List<Product> products = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT ID, NAME, MANUFACTURER, PRICE, RATING, REVIEWS FROM PRODUCT");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String manufacturer = rs.getString("MANUFACTURER");
                double price = rs.getDouble("PRICE");
                double rating = rs.getDouble("RATING");
                int reviews = rs.getInt("REVIEWS");

                Product product = new Product(id, name, manufacturer, price);
                product.setReliabilityRating(rating, reviews);
                products.add(product);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return products;
    }
}
