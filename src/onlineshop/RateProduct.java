package onlineshop;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RateProduct {
    private Inventory inventory;
    private Scanner scn;
    private FileInputOutput fileIO;
    private DBManager dbManager;

    public RateProduct(Inventory onlineShop, Scanner scanner) {
        this.inventory = onlineShop;
        this.scn = scanner;
        this.fileIO = new FileInputOutput(inventory);
    }

    public void rateProducts(Product selectedProduct) {
        boolean continueInput = true;
        double input_rating;

        // Loop for when an inputMissmatch is caught
        while (continueInput) {
            System.out.print("\nEnter your rating (0.0 to 5.0): ");

            try {
                input_rating = scn.nextDouble();
                scn.nextLine();

                // Validating rating, if not valid a helpful message appears
                while (input_rating < 0.0 || input_rating > 5.0) {
                    System.out.println(input_rating + " is not valid entry, please try again!\n");
                    System.out.print("Enter your rating (0.0 to 5.0): ");
                    input_rating = scn.nextDouble();
                    scn.nextLine();
                }
                continueInput = false;
                selectedProduct.rateReliability(input_rating); // Show the new rating
            } catch (InputMismatchException e) {
                StaticMethods.printInvalidInputMessage();
                scn.nextLine();
            }
        }

        // Update the new rating or cancel?
        System.out.println(selectedProduct + "\n");
        System.out.println("A. Update / B. Cancel");
        System.out.print("Type \"A\" or \"B\" to select: ");
        String option = scn.nextLine();

        // Validate input, if not valid a helpful message appears
        while (option.toLowerCase().charAt(0) != 'a' && option.toLowerCase().charAt(0) != 'b') {
            System.out.println(option + " is not a valid option, please try again!\n");
            System.out.print("Type \"A\" or \"B\" to select:");
            option = scn.nextLine();
        }

        // handling both cases of the letter A
        if (option.toLowerCase().charAt(0) == 'a') {
            System.out.println("\nRating updated!\n");
            // Save updated products back to the file
            dbManager.updateProducts(inventory);

        } // handling both cases of the letter B
        else if (option.equalsIgnoreCase("b")) {
            System.out.println();
            return;
        } 
    }
}
