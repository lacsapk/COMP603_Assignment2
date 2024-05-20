package onlineshop;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ShopFunctions {
    private Inventory inventory;
    private Scanner scn;
    private RateProduct rateProduct;
    private OrderProduct orderProduct;

    public ShopFunctions(Inventory onlineShop, Scanner scanner) {
        this.inventory = onlineShop;
        this.scn = scanner;
        this.rateProduct = new RateProduct(onlineShop, scanner);
        this.orderProduct = new OrderProduct(onlineShop, scanner);
    }

    // Desplays all the availiable Products
    public void displayInventory() {
        boolean continueInput = true;

        System.out.println("\nAll products:");

        // Display all products with an index for the user to choose
        List<String> modelNames = new ArrayList<>(inventory.getInventory().keySet());
        for (int i = 0; i < modelNames.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.getInventory().get(modelNames.get(i)));
        }

        // Loop for when an inputMissmatch is caught
        while (continueInput) {
            
            System.out.print("\nSelect Product by typing the number: ");

            try {
                int productIndex = scn.nextInt();
                scn.nextLine();

                // Validate the selected product number if not valid a helpful message appears
                while (productIndex <= 0 || productIndex > modelNames.size()) {
                    System.out.println(productIndex + " is not a valid article-Nr. please try again!\n");
                    System.out.print("Select Product by typing the number: ");
                    productIndex = scn.nextInt();
                    scn.nextLine();
                }

                // Convert the index to model name
                String selectedModelName = modelNames.get(productIndex - 1);
                Product selectedProduct = inventory.getInventory().get(selectedModelName);
                System.out.println(selectedProduct);
                rateOrder(selectedProduct);
                continueInput = false;

            } catch (InputMismatchException e) {
                StaticMethods.printInvalidInputMessage();
                scn.nextLine();
            }
        }
    }

    // Searches for a Product based on its modelName = Key
    public void searchProduct() {
        System.out.print("\nEnter the product model name to search: ");
        String modelName = scn.nextLine();
        Product product = inventory.getProduct(modelName);
        if (product != null) {
            System.out.println("Product found: " + product);
            rateOrder(product);
        } else {
            System.out.println("Product not found.");
        }
        System.out.println();
    }

    // DecisionPoint (Routing) for rating a Product or ordering a product
    private void rateOrder(Product selectedProduct) {
        System.out.println("\nA. Order Product / B. Rate Product");
        System.out.print("Type \"A\" or \"B\" to select and \"C\" to exit:");
        String option = scn.nextLine();

        while (option.toLowerCase().charAt(0) != 'a' && option.toLowerCase().charAt(0) != 'b' && option.toLowerCase().charAt(0) != 'c') {
            System.out.println(option + " is not a valid option, please try again!\n");
            System.out.print("Type \"A\" or \"B\" to select and \"C\" to exit:");
            option = scn.nextLine();
        }

        // handling both cases of the letter A
        if (option.toLowerCase().charAt(0) == 'a') {
            orderProduct.orderProducts(selectedProduct);

        } // handling both cases of the letter B
        else if (option.equalsIgnoreCase("b")) {
            rateProduct.rateProducts(selectedProduct);

        } // handling both cases of letter C
        else if (option.equalsIgnoreCase("c")) {
            System.out.println("going to menu...");
            System.out.println();
        }
    }
}
