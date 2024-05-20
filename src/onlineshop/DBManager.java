package onlineshop;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


    public List<Product> getProductsFromDatabase() {
        List<Product> products = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT NAME, MANUFACTURER, PRICE, RATING, REVIEWS FROM PRODUCT");

            while (rs.next()) {
                String name = rs.getString("NAME");
                String manufacturer = rs.getString("MANUFACTURER");
                double price = rs.getDouble("PRICE");
                double rating = rs.getDouble("RATING");
                int reviews = rs.getInt("REVIEWS");

                Product product = new Product(name, manufacturer, price);
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
