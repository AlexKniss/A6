package playwriteLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

public class SimplePlaywrightTest {
    
    @Test
    @DisplayName("Simple Browser Test")
    void testBrowserLaunch() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        
        try {
            page.navigate("https://depaul.bncollege.com/");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            
            // Take a screenshot to verify it worked
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("test-screenshot.png")));
            
            System.out.println("Page title: " + page.title());
            System.out.println("Page URL: " + page.url());
            
        } finally {
            page.close();
            browser.close();
            playwright.close();
        }
    }
}