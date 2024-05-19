
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<String, Product> inventory;

    // this constructor initializes the private instance variables
    public Inventory() {
        inventory = new HashMap<>();
    }

    // method for adding products into the inventory
    public void add(Product product) {
        inventory.put(product.get_model_name(), product);
    }

    // this method returns the product on the specified index given as its parameter
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
}
