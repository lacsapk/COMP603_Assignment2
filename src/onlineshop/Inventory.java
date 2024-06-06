package onlineshop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {

    private Map<String, Product> inventory;

    //This constructor initializes the private instance variables 
    public Inventory() {
        inventory = new HashMap<>();
    }

    // method for adding products to the inventory
    public void add(Product product) {
        inventory.put(product.get_model_name(), product);
    }

    //This method returns the product on the specified index given as its parameter
    public Product getProduct(String modelName) {
        return inventory.get(modelName);
    }

    // Method to get the inventory map
    public Map<String, Product> getInventory() {
        return inventory;
    }

    public Collection<Product> getAllProducts() {
        return inventory.values();
    }

    public List<Product> getAllProductsAsList() {
        return new ArrayList<>(inventory.values());
    }
}

