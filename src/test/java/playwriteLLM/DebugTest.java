package playwriteLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

public class DebugTest {
    
    @Test
    @DisplayName("Debug - Inspect Website Structure")
    void debugWebsiteStructure() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        
        try {
            System.out.println("Navigating to DePaul bookstore...");
            page.navigate("https://depaul.bncollege.com/");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(3000);
            
            // Take a screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("debug-homepage.png")));
            
            // Count and describe all visible inputs
            Locator allInputs = page.locator("input");
            int totalInputs = allInputs.count();
            System.out.println("\nTotal inputs found: " + totalInputs);
            
            Locator visibleInputs = page.locator("input:visible");
            int visibleCount = visibleInputs.count();
            System.out.println("Visible inputs: " + visibleCount);
            
            for (int i = 0; i < visibleCount; i++) {
                Locator input = visibleInputs.nth(i);
                String type = input.getAttribute("type");
                String placeholder = input.getAttribute("placeholder");
                String name = input.getAttribute("name");
                String id = input.getAttribute("id");
                
                System.out.println(String.format("Input %d: type=%s, placeholder='%s', name='%s', id='%s'", 
                    i, type, placeholder, name, id));
            }
            
            // Look specifically for search elements
            System.out.println("\nLooking for search-related elements...");
            
            // Check if there's a search form
            if (page.locator("form").count() > 0) {
                System.out.println("Found forms on page");
            }
            
            // Check common search patterns
            String[] searchTests = {
                "input[type='search']",
                "input[placeholder*='search' i]",
                "#search",
                ".search",
                "[data-search]"
            };
            
            for (String selector : searchTests) {
                int count = page.locator(selector).count();
                if (count > 0) {
                    System.out.println("Selector '" + selector + "' matches " + count + " elements");
                }
            }
            
            // Wait for manual inspection
            System.out.println("\nBrowser will stay open for 10 seconds for manual inspection...");
            page.waitForTimeout(10000);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            page.close();
            browser.close();
            playwright.close();
        }
    }
}