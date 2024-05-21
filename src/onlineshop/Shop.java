package onlineshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Shop {

    //	declaring an instance variable of my_online_shop
    private static Inventory inventory = new Inventory();
    private static Scanner scn = new Scanner(System.in);
    private static DBManager dbManager = new DBManager();

    //	main method
    public static void main(String[] args) {
        ShopFunctions shopFunctions = new ShopFunctions(inventory, scn, dbManager);

        // Checks if a Table with the Products already exists, if not it gets added
        dbManager.initializeProductTable();
        // Checks if a Table with the Orders already exists, if not it gets added
        dbManager.initializeOrderTable();
        // Get products from database and add to inventory
        dbManager.initializeInventory(inventory);

        // this loop will repeatedly show the menu and ask for an input until it is terminated by choosing "C" option
        while (true) {
            //	this will print the menu on the console
            System.out.print("   ____        _ _               _____ _                 \n");
            System.out.print("  / __ \\      | (_)             / ____| |                \n");
            System.out.print(" | |  | |_ __ | |_ _ __   ___  | (___ | |__   ___  _ __  \n");
            System.out.print(" | |  | | '_ \\| | | '_ \\ / _ \\  \\___ \\| '_ \\ / _ \\| '_ \\ \n");
            System.out.print(" | |__| | | | | | | | | |  __/  ____) | | | | (_) | |_) |\n");
            System.out.print("  \\____/|_| |_|_|_|_| |_|\\___| |_____/|_| |_|\\___/| .__/ \n");
            System.out.print("                                                  | |    \n");
            System.out.print("                                                  |_|    \n");

            System.out.println("\nChoose one of these options:\nA. Display inventory\nB. Search a Product\nC. View Orders\nD. Exit application");
            System.out.print("Type \"A\", \"B\" or \"C\" to select and \"D\" to exit the application:");
            String option = scn.nextLine();

            // handling both cases of the letter A
            if (option.toLowerCase().charAt(0) == 'a') {
                shopFunctions.displayInventory();

            } // handling both cases of the letter B
            else if (option.equalsIgnoreCase("b")) {
                shopFunctions.searchProduct();

            } // handling both cases of the letter C
            else if (option.toLowerCase().charAt(0) == 'c') {
                dbManager.viewOrders();

            } // handling both cases of the letter D
            else if (option.toLowerCase().charAt(0) == 'd') {
                dbManager.closeConnection(); // Close Connection to DB
                System.out.println("exiting...");
                break; // terminating the loop

            } else {
                System.out.println("Invalid Input, please enter \"A\", \"B\", \"C\" or \"D\"\n");
            }
        }
    }
}
