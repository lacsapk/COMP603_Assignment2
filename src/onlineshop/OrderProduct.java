
import java.util.InputMismatchException;
import java.util.Scanner;

public class OrderProduct {
    private Inventory inventory;
    private Scanner scn;
    private FileInputOutput fileIO;

    public OrderProduct(Inventory onlineShop, Scanner scanner) {
        this.inventory = onlineShop;
        this.scn = scanner;
        this.fileIO = new FileInputOutput(inventory);
    }

    public void orderProducts(Product selectedProduct) {
        boolean continueInput = true;
        int amount = 0;

        // Loop for when an inputMissmatch is caught
        while (continueInput) {
            System.out.print("\nOrder Amount (max. 5):");

            try {
                amount = scn.nextInt();
                scn.nextLine();

                // Validate the number, if not valid a helpful message appears
                while (amount <= 0 || amount > 5) {
                    System.out.println(amount + " is not a valid amount, please try again!\n");
                    System.out.print("Order Amount (max. 5):");
                    amount = scn.nextInt();
                    scn.nextLine();
                }
                continueInput = false;
            } catch (InputMismatchException e) {
                StaticMethods.printInvalidInputMessage();
                scn.nextLine();
            }
        }

        double totalCost = selectedProduct.get_retail_price() * amount;
        System.out.println("- " + amount + "x " + selectedProduct.get_model_name() + " / Total: " + totalCost);
        System.out.println("\nA. Order / B. Cancel");
        System.out.print("Type \"A\" or \"B\" to select: ");
        String option = scn.nextLine();

        // Validate if the input, if not valid a helpful message appears
        while (option.toLowerCase().charAt(0) != 'a' && option.toLowerCase().charAt(0) != 'b') {
            System.out.println(option + " is not a valid option, please try again!\n");
            System.out.print("Type \"A\" or \"B\" to select:");
            option = scn.nextLine();
        }

        // handling both cases of the letter A
        if (option.toLowerCase().charAt(0) == 'a') {
            fileIO.saveOrderToFile(selectedProduct.get_model_name(), selectedProduct.get_name_of_manufacturer(), amount, totalCost);

        } // handling both cases of the letter B
        else if (option.equalsIgnoreCase("b")) {
            System.out.println();
            return;
        } 
    }
}

