package onlineshop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

public class FileInputOutput {
    private Inventory inventory;

    public FileInputOutput(Inventory inventory) {
        this.inventory = inventory;
    }

    // Save Updated rating to File
    public void saveProductsToFile(String filename) {
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            nf.setGroupingUsed(false); // So that no ' gets added "2200.00" instead of "2'200.00"

            // Save values as a String and write it into the File 
            for (Product product : inventory.getAllProducts()) {
                if (product != null) {
                    String line = product.get_model_name() + "," + product.get_name_of_manufacturer() + "," + nf.format(product.get_retail_price()) + "," + nf.format(product.get_reliability_rating()) + "," + product.get_no_of_reviews();
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Load the Strings out of File and output it
    public void viewOrders() {
        try ( BufferedReader reader = new BufferedReader(new FileReader("./resources/orders.txt"))) {
            String line;
            System.out.println("\nCurrent Orders:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line + "\n"); // adding new lines for the code ethic
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }

    }

    // Save ordered Product as String to File
    public void saveOrderToFile(String modelName, String manufacturer, int amount, double totalCost) {
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter("./resources/orders.txt", true))) {
            String orderInfo = amount + "x " + modelName + " by " + manufacturer + " for a total of: " + NumberFormat.getCurrencyInstance().format(totalCost);
            writer.write(orderInfo);
            writer.newLine();
            System.out.println("\nYour order has been placed! Thank you for your purchase.\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    //	overriding toString method to print a meaningful enumerated string of products' list
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;
        for (Product product : inventory.getAllProducts()) {
            stringBuilder.append(count++).append(". ").append(product).append('\n');
        }
        return stringBuilder.toString();
    }
}
