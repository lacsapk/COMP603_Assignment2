package onlineshop;

import java.text.NumberFormat;

public class Product {
    //	declaring the private instances variables that are used later
    private String model_name;
    private String name_of_manufacturer;
    private double retail_price;
    private double reliability_rating; // average of ratings by all the reviewers
    private int no_of_reviews;

    // this constructor takes model name, manufacturer's name, and retail price as its parameters
    public Product(String model_name, String name_of_manufacturer, double retail_price) {
        this.model_name = model_name;
        this.name_of_manufacturer = name_of_manufacturer;
        this.retail_price = retail_price;
        reliability_rating = 0.0;
        no_of_reviews = 0;
    }
    
    // use constructor takes model name and retail price

    // getter for model_name
    public String get_model_name() {
        return model_name;
    }

    // getter for name_of_manufacturer
    public String get_name_of_manufacturer() {
        return name_of_manufacturer;
    }

    // getter for retail_price
    public double get_retail_price() {
        return retail_price;
    }

    //	gette for reliability_rating (rating between 0.0 and 5.0)
    public double get_reliability_rating() {
        return reliability_rating;
    }

    //	getter for no_of_reviews
    public int get_no_of_reviews() {
        return no_of_reviews;
    }

    // setter for retail_price
    public void set_retail_price(double retail_price) {
        this.retail_price = retail_price;
    }

    // setter for ReliabilityRating
    public void setReliabilityRating(double rating, int reviews) {
        this.reliability_rating = rating;
        this.no_of_reviews = reviews;
    }

    // method to update Reliability Rating based on the following formula
    public void rateReliability(double reliability_rate) {
        double old_no_of_reviews = no_of_reviews;
        no_of_reviews++;
        reliability_rating = (reliability_rating * old_no_of_reviews + reliability_rate) / no_of_reviews;
    }

    //	overriding toString method of the Object class to print useful information about each product
    @Override
    public String toString() {
        // A number formatter to format the retail_price by adding a suitable notation and currency sign
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        // returning information of the products in the required format
        return model_name + ", " + name_of_manufacturer + ", " + nf.format(retail_price) + ", " + reliability_rating
                + " (based on " + no_of_reviews + " customer ratings)";
    }
}
