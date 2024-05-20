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

    private static final String URL = "jdbc:derby:ProductDB;create=true";
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

    public void createProductTable() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE PRODUCT (ID INT PRIMARY KEY, NAME VARCHAR(100), MANUFACTURER VARCHAR(100), PRICE DOUBLE, RATING DOUBLE, REVIEWS INT)");

            // Insert initial data manually
            statement.addBatch("INSERT INTO PRODUCT VALUES (1, 'PlayStation 5', 'PlayStation inc', 999.00, 3.37, 12)");
            statement.addBatch("INSERT INTO PRODUCT VALUES (2, 'Samsung Galaxy', 'Samsung Electronics', 1400.00, 3.55, 4)");
            statement.addBatch("INSERT INTO PRODUCT VALUES (3, 'Nintendo Switch', 'Nintendo Corporation', 639.00, 4.60, 5)");
            statement.addBatch("INSERT INTO PRODUCT VALUES (4, 'Xbox', 'Microsoft Corporation', 1100.00, 4.95, 2)");
            statement.addBatch("INSERT INTO PRODUCT VALUES (5, 'Iphone 14', 'Apple Technology Company', 2200.00, 3.50, 5)");

            statement.executeBatch();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

public void updateProducts(Inventory inventory) {
    String updateSQL = "UPDATE PRODUCT SET NAME = ?, MANUFACTURER = ?, PRICE = ?, RATING = ?, REVIEWS = ? WHERE ID = ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
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
