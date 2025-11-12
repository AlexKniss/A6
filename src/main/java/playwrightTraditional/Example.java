package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;
import java.util.*;

public class Example {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                            .setRecordVideoDir(Paths.get("videos/"))
                            .setRecordVideoSize(1280, 720));
            Page page = context.newPage();
            page.navigate("https://depaul.bncollege.com/");

            //1.
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("earbuds");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).press("Enter");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
            page.locator(".facet__list.js-facet-list.js-facet-top-values > li:nth-child(3) > form > label > .facet__list__label > .facet__list__mark > .facet-unchecked > svg").first().click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
            page.locator("#facet-Color > .facet__values > .facet__list > li > form > label > .facet__list__label > .facet__list__mark > .facet-unchecked > svg").first().click();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
            page.locator("#facet-price > .facet__values > .facet__list > li:nth-child(2) > form > label > .facet__list__label > .facet__list__mark > .facet-unchecked > svg").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();

            assertThat(page.getByLabel("main").getByRole(AriaRole.HEADING)).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
            assertThat(page.getByLabel("main")).containsText("sku 668972707");
            assertThat(page.getByLabel("main")).containsText("$164.98");
            assertThat(page.getByLabel("main")).containsText("Adaptive noise cancelling allows awareness of environment when gaming on the go. Light weight, durable, water resist. USB-C dongle for low latency connection < than 30ms.");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart")).click();

            assertThat(page.locator("#headerDesktopView")).containsText("1 items");

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();

            //2.
            assertThat(page.getByLabel("main")).containsText("Your Shopping Cart");
            assertThat(page.getByLabel("main")).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
            assertThat(page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Quantity, edit and press"))).hasValue("1");
            assertThat(page.getByLabel("main")).containsText("$164.98");

            page.getByText("FAST In-Store PickupDePaul").click();

            assertThat(page.getByLabel("main")).containsText("Subtotal $164.98");
            assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
            assertThat(page.getByLabel("main")).containsText("Taxes TBD");
            assertThat(page.getByLabel("main")).containsText("Estimated Total $167.98");

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Enter Promo Code")).fill("TEST");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply Promo Code")).click();

            assertThat(page.locator("#js-voucher-result")).containsText("The coupon code entered is not valid.");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed To Checkout")).first().click();

            //3.
            assertThat(page.getByLabel("main")).containsText("Create Account");

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();

            //4.
            assertThat(page.getByLabel("main")).containsText("Contact Information");

            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name (required)")).fill("Alexander");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name (required)")).fill("Kniss");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address (required)")).fill("akniss@depaul.edu");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone Number (required)")).fill("8158760809");

            assertThat(page.getByLabel("main")).containsText("Order Subtotal $164.98");
            assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
            assertThat(page.getByLabel("main")).containsText("Tax TBD");
            assertThat(page.getByLabel("main")).containsText("Total $167.98 167.98 $");

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

            //5.
            assertThat(page.getByLabel("main")).containsText("Full Name Alexander Kniss Email Address akniss@depaul.edu Phone Number +18158760809");
            assertThat(page.locator("#bnedPickupPersonForm")).containsText("Pickup Location DePaul University Loop Campus & SAIC 1 E. Jackson Boulevard, , Illinois, Chicago, 60604");
            assertThat(page.locator(".sub-check").first()).isVisible();
            assertThat(page.getByLabel("main")).containsText("Order Subtotal $164.98");
            assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
            assertThat(page.getByLabel("main")).containsText("Tax TBD");
            assertThat(page.getByLabel("main")).containsText("Total $167.98 167.98 $");
            assertThat(page.getByLabel("main")).containsText("PICKUP DePaul University Loop Campus & SAIC JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black Quantity: Qty: 1 $164.98");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();

            //6.
            assertThat(page.getByLabel("main")).containsText("Order Subtotal $164.98");
            assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
            assertThat(page.getByLabel("main")).containsText("Tax $17.22");
            assertThat(page.getByLabel("main")).containsText("Total $185.20 185.2 $");
            assertThat(page.getByLabel("main")).containsText("PICKUP DePaul University Loop Campus & SAIC JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black Quantity: Qty: 1 $164.98");
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();

            //7.
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove product JBL Quantum")).click();
            assertThat(page.getByLabel("main").getByRole(AriaRole.HEADING)).containsText("Your cart is empty");
        }
    }
}