package playwriteLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

public class PlaywrightRecorder {
    
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        
        try {
            // Navigate to the bookstore
            System.out.println("Navigating to DePaul bookstore...");
            page.navigate("https://depaul.bncollege.com/");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            
            // Wait a bit for the page to fully load
            page.waitForTimeout(3000);
            
            // Take a screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("bookstore-home.png")));
            
            // Try to find search elements and print their selectors
            System.out.println("Looking for search elements...");
            
            // Check for various search input elements
            String[] searchSelectors = {
                "input[type='search']",
                "input[placeholder*='Search']", 
                "input[name='q']",
                "input[name='search']",
                ".search-input",
                "#search",
                ".header-search input"
            };
            
            for (String selector : searchSelectors) {
                if (page.isVisible(selector)) {
                    System.out.println("Found search input: " + selector);
                }
            }
            
            // Keep browser open for manual inspection
            System.out.println("Browser is open. Press Enter to continue with search test...");
            System.in.read();
            
            // Try searching for earbuds
            System.out.println("Attempting to search for 'earbuds'...");
            
            // Try the most common search selector
            page.fill("input[type='search']", "earbuds");
            page.keyboard().press("Enter");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForTimeout(2000);
            
            // Take another screenshot after search
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("search-results.png")));
            
            System.out.println("Search completed. Check search-results.png");
            System.out.println("Press Enter to close...");
            System.in.read();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            page.close();
            browser.close();
            playwright.close();
        }
    }
}