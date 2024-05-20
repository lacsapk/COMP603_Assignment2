package onlineshop;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBManager {
    private static final String URL = "jdbc:derby://localhost:1527/ProductDB;create=true";
    private static final String USER_NAME = "pdc"; 
    private static final String PASSWORD = "pdc"; 
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
                conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                System.out.println(URL + " Get Connected Successfully ....");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
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

     public void createProductTable(List<Product> products) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE PRODUCT (ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), NAME VARCHAR(100), MANUFACTURER VARCHAR(100), PRICE DOUBLE, RATING DOUBLE, REVIEWS INT)");

            for (Product product : products) {
                String sql = String.format("INSERT INTO PRODUCT (NAME, MANUFACTURER, PRICE, RATING, REVIEWS) VALUES ('%s', '%s', %.2f, %.2f, %d)",
                        product.get_model_name(), product.get_name_of_manufacturer(), product.get_retail_price(), product.get_reliability_rating(), product.get_no_of_reviews());
                statement.executeUpdate(sql);
            }

            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
