package playwriteLLM;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartDebugTest {
    private ExampleLLM exampleLLM;
    
    @BeforeEach
    void setUp() {
        exampleLLM = new ExampleLLM();
        exampleLLM.initializeBrowser();
    }
    
    @AfterEach
    void tearDown() {
        if (exampleLLM != null) {
            // Don't close immediately, wait to see results
            try {
                Thread.sleep(30000); // Wait 30 seconds to inspect
            } catch (InterruptedException e) {
                // ignore
            }
            exampleLLM.closeWindow();
        }
    }
    
    @Test
    void debugCartPage() {
        // Add product to cart
        exampleLLM.navigateToBookstore();
        exampleLLM.searchForProduct("earbuds");
        exampleLLM.selectBrandFilter("JBL");
        exampleLLM.selectColorFilter("Black"); 
        exampleLLM.selectPriceFilter("Over $50");
        exampleLLM.clickJBLQuantumProduct();
        exampleLLM.addToCart(1);
        
        // Navigate to cart
        exampleLLM.clickCart();
        
        System.out.println("=== CART PAGE DEBUG ===");
        System.out.println("Current URL: " + exampleLLM.getCurrentUrl());
        System.out.println("Page title: " + exampleLLM.getPageTitle());
        
        // Wait for manual inspection
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}