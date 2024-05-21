package onlineshop;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pasca
 */
public class ProductTest {
    
    public ProductTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of rateReliability method, of class Product.
     */
    @Test
    public void testRateReliability() {
        System.out.println("rateReliability");

        // a new Product gets created with an initial rating of 4.0
        Product instance = new Product(1, "TestModel", "TestManufacturer", 100.0);
        instance.rateReliability(4.0);

        // Check that the rating is 4.0 and number of reviews 1
        assertEquals(4.0, instance.get_reliability_rating(), 0.001);
        assertEquals(1, instance.get_no_of_reviews());

        // Add another rating of 5 and check that the new rating is 4.5 with 2 reviews
        instance.rateReliability(5.0);
        assertEquals(4.5, instance.get_reliability_rating(), 0.001);
        assertEquals(2, instance.get_no_of_reviews());

        // Add another rating of 3 and check that the new rating is 4 with 3 reviews
        instance.rateReliability(3.0);
        assertEquals(4.0, instance.get_reliability_rating(), 0.001);
        assertEquals(3, instance.get_no_of_reviews());
    }
}
