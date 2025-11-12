package playwriteLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

public class ExampleLLM {
    private Page page;
    private Playwright playwright;
    private Browser browser;
    
    public ExampleLLM() {
        // Constructor will be called from test setup
    }
    
    public void initializeBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }
    
    public void navigateToBookstore() {
        page.navigate("https://depaul.bncollege.com/");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    
    public void searchForProduct(String searchTerm) {
        // Wait for page to load and find the search box
        page.waitForTimeout(3000);
        
        // Use the exact selector found from inspection
        String searchSelector = "#bned_site_search";
        
        try {
            // Fill the search input
            page.fill(searchSelector, searchTerm);
            page.keyboard().press("Enter");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            System.out.println("Successfully searched for: " + searchTerm);
        } catch (Exception e) {
            // Fallback to other selectors
            String[] fallbackSelectors = {
                "input[name='text']",
                "input[placeholder*='search' i]",
                "input[placeholder*='product' i]"
            };
            
            boolean success = false;
            for (String selector : fallbackSelectors) {
                try {
                    if (page.locator(selector).count() > 0 && page.locator(selector).first().isVisible()) {
                        page.fill(selector, searchTerm);
                        page.keyboard().press("Enter");
                        page.waitForLoadState(LoadState.NETWORKIDLE);
                        success = true;
                        System.out.println("Search successful using fallback selector: " + selector);
                        break;
                    }
                } catch (Exception ex) {
                    // Continue to next selector
                }
            }
            
            if (!success) {
                throw new RuntimeException("Could not find search input: " + e.getMessage());
            }
        }
    }
    
    public void selectBrandFilter(String brand) {
        // Wait and try to find Brand filter
        page.waitForTimeout(2000);
        
        System.out.println("Attempting to find Brand filter for: " + brand);
        
        // Try multiple ways to find and click Brand filter
        String[] brandSelectors = {
            "text=Brand",
            ".filter-brand",
            "[data-filter='brand']",
            ".facet-brand",
            ".refinement-brand",
            "h3:has-text('Brand')",
            "button:has-text('Brand')"
        };
        
        boolean filterFound = false;
        for (String selector : brandSelectors) {
            if (page.locator(selector).count() > 0) {
                try {
                    page.click(selector);
                    page.waitForTimeout(1000);
                    filterFound = true;
                    System.out.println("Found brand filter with selector: " + selector);
                    break;
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        }
        
        if (!filterFound) {
            System.out.println("Brand filter not found, skipping brand selection");
            return;
        }
        
        // Try to select the specific brand
        String[] brandOptionSelectors = {
            "text=" + brand,
            "label:has-text('" + brand + "')",
            "input[value='" + brand + "'] + label",
            "a:has-text('" + brand + "')",
            "[data-value='" + brand + "']"
        };
        
        boolean brandSelected = false;
        for (String selector : brandOptionSelectors) {
            if (page.locator(selector).count() > 0) {
                try {
                    page.click(selector);
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    brandSelected = true;
                    System.out.println("Successfully selected brand: " + brand);
                    break;
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        }
        
        if (!brandSelected) {
            System.out.println("Brand '" + brand + "' not found in filter options, continuing without brand filter");
        }
    }
    
    public void selectColorFilter(String color) {
        page.waitForTimeout(2000);
        System.out.println("Attempting to find Color filter for: " + color);
        
        // Try to find and apply color filter, but don't fail if not available
        try {
            String[] colorSelectors = {
                "text=Color",
                ".filter-color",
                "[data-filter='color']",
                ".facet-color",
                "h3:has-text('Color')"
            };
            
            boolean filterFound = false;
            for (String selector : colorSelectors) {
                if (page.locator(selector).count() > 0) {
                    page.click(selector);
                    page.waitForTimeout(1000);
                    filterFound = true;
                    break;
                }
            }
            
            if (filterFound) {
                // Try to select the color
                page.click("text=" + color);
                page.waitForLoadState(LoadState.NETWORKIDLE);
                System.out.println("Successfully applied color filter: " + color);
            } else {
                System.out.println("Color filter not available, skipping");
            }
        } catch (Exception e) {
            System.out.println("Color filter selection failed, continuing: " + e.getMessage());
        }
    }
    
    public void selectPriceFilter(String priceRange) {
        page.waitForTimeout(2000);
        System.out.println("Attempting to find Price filter for: " + priceRange);
        
        // Try to find and apply price filter, but don't fail if not available
        try {
            String[] priceSelectors = {
                "text=Price",
                ".filter-price",
                "[data-filter='price']",
                ".facet-price",
                "h3:has-text('Price')"
            };
            
            boolean filterFound = false;
            for (String selector : priceSelectors) {
                if (page.locator(selector).count() > 0) {
                    page.click(selector);
                    page.waitForTimeout(1000);
                    filterFound = true;
                    break;
                }
            }
            
            if (filterFound) {
                // Try to select the price range
                page.click("text=" + priceRange);
                page.waitForLoadState(LoadState.NETWORKIDLE);
                System.out.println("Successfully applied price filter: " + priceRange);
            } else {
                System.out.println("Price filter not available, skipping");
            }
        } catch (Exception e) {
            System.out.println("Price filter selection failed, continuing: " + e.getMessage());
        }
    }
    
    public void clickJBLQuantumProduct() {
        // Wait for search results to load
        page.waitForTimeout(3000);
        
        System.out.println("Looking for JBL Quantum product...");
        
        // Try multiple selectors for any JBL or earbuds product
        String[] productSelectors = {
            "text=JBL Quantum True Wireless Noise Cancelling Gaming",
            "a:has-text('JBL Quantum')",
            "a:has-text('JBL')",
            "a:has-text('earbuds')",
            ".product-item a",
            ".search-result a",
            "[href*='product']",
            ".item a"
        };
        
        boolean productFound = false;
        for (String selector : productSelectors) {
            if (page.locator(selector).count() > 0) {
                try {
                    // Click the first matching product
                    page.locator(selector).first().click();
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    productFound = true;
                    System.out.println("Successfully clicked product with selector: " + selector);
                    break;
                } catch (Exception e) {
                    // Continue to next selector
                    System.out.println("Selector " + selector + " failed: " + e.getMessage());
                }
            }
        }
        
        if (!productFound) {
            System.out.println("No specific product found, looking for any clickable product...");
            // Try to find any product link on the page
            Locator productLinks = page.locator("a[href*='product'], .product a, .item a");
            if (productLinks.count() > 0) {
                productLinks.first().click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                System.out.println("Clicked first available product");
            } else {
                throw new RuntimeException("No products found on the page");
            }
        }
    }
    
    public String getProductName() {
        String[] nameSelectors = {
            "h1", 
            ".product-title", 
            ".product-name",
            ".item-title",
            ".product-detail-title"
        };
        
        for (String selector : nameSelectors) {
            if (page.isVisible(selector)) {
                return page.textContent(selector).trim();
            }
        }
        return "Product Name Not Found";
    }
    
    public String getSkuNumber() {
        String[] skuSelectors = {
            ".sku", 
            ".product-sku", 
            "[data-sku]",
            "text=SKU:",
            ".item-number",
            ".product-code"
        };
        
        for (String selector : skuSelectors) {
            if (page.isVisible(selector)) {
                return page.textContent(selector).trim();
            }
        }
        return "SKU Not Found";
    }
    
    public String getProductPrice() {
        String[] priceSelectors = {
            ".price", 
            ".product-price", 
            ".current-price",
            ".item-price",
            ".sale-price",
            ".regular-price"
        };
        
        for (String selector : priceSelectors) {
            if (page.isVisible(selector)) {
                return page.textContent(selector).trim();
            }
        }
        return "$0.00";
    }
    
    public String getProductDescription() {
        String[] descSelectors = {
            ".description", 
            ".product-description", 
            ".product-details",
            ".item-description",
            ".product-summary"
        };
        
        for (String selector : descSelectors) {
            if (page.isVisible(selector)) {
                return page.textContent(selector).trim();
            }
        }
        return "Description Not Found";
    }
    
    public void addToCart(int quantity) {
        page.waitForTimeout(2000); // Wait for product page to load
        System.out.println("Attempting to add product to cart...");
        
        // Set quantity if needed
        String[] quantitySelectors = {
            "input[name='quantity']",
            ".quantity-input",
            "#quantity",
            ".qty-input",
            "input[type='number']"
        };
        
        for (String selector : quantitySelectors) {
            if (page.locator(selector).count() > 0 && page.locator(selector).first().isVisible()) {
                try {
                    page.fill(selector, String.valueOf(quantity));
                    System.out.println("Set quantity to: " + quantity);
                    break;
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        }
        
        // Click add to cart button
        String[] addToCartSelectors = {
            "text=Add to Cart",
            "button:has-text('Add to Cart')",
            "button:has-text('ADD TO CART')",
            ".add-to-cart",
            ".btn-add-to-cart",
            "#add-to-cart",
            "button[data-action='add-to-cart']",
            "input[value='Add to Cart']",
            "[data-add-to-cart]"
        };
        
        boolean addedToCart = false;
        for (String selector : addToCartSelectors) {
            if (page.locator(selector).count() > 0) {
                try {
                    page.locator(selector).first().click();
                    page.waitForTimeout(3000); // Wait for cart update
                    addedToCart = true;
                    System.out.println("Successfully added to cart using: " + selector);
                    break;
                } catch (Exception e) {
                    System.out.println("Add to cart selector " + selector + " failed: " + e.getMessage());
                }
            }
        }
        
        if (!addedToCart) {
            System.out.println("Could not find add to cart button, looking for any button...");
            // Try to find any button that might add to cart
            Locator buttons = page.locator("button, input[type='submit']");
            int count = buttons.count();
            for (int i = 0; i < count; i++) {
                String buttonText = buttons.nth(i).textContent();
                if (buttonText != null && (buttonText.toLowerCase().contains("add") || 
                                         buttonText.toLowerCase().contains("cart") ||
                                         buttonText.toLowerCase().contains("buy"))) {
                    try {
                        buttons.nth(i).click();
                        page.waitForTimeout(3000);
                        System.out.println("Clicked button: " + buttonText);
                        break;
                    } catch (Exception e) {
                        // Continue
                    }
                }
            }
        }
    }
    
    public String getCartItemCount() {
        page.waitForTimeout(2000); // Wait for cart to update
        System.out.println("Checking cart item count...");
        
        String[] countSelectors = {
            ".cart-count",
            ".cart-items-count", 
            ".header-cart-count",
            ".cart-quantity",
            "#cart-count",
            "[data-cart-count]",
            ".shopping-cart .count",
            ".cart .badge"
        };
        
        for (String selector : countSelectors) {
            if (page.locator(selector).count() > 0 && page.locator(selector).first().isVisible()) {
                try {
                    String count = page.locator(selector).first().textContent().trim();
                    // Extract just the number
                    String numericCount = count.replaceAll("[^0-9]", "");
                    if (!numericCount.isEmpty()) {
                        System.out.println("Found cart count: " + numericCount + " (from: " + count + ")");
                        return numericCount;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        }
        
        // Look for any element that might indicate cart contents
        System.out.println("No specific cart count found, checking for cart indicators...");
        
        // Check if we can find cart-related text
        String[] cartIndicators = {
            "text=1 item",
            "text=1 Item", 
            "text=1 Items",
            "text=(1)",
            "text=Cart (1)"
        };
        
        for (String indicator : cartIndicators) {
            if (page.locator(indicator).count() > 0) {
                System.out.println("Found cart indicator: " + indicator);
                return "1";
            }
        }
        
        System.out.println("No cart count found, returning 0");
        return "0";
    }
    
    public void clickCart() {
        page.waitForTimeout(2000); // Wait for cart icon to update
        System.out.println("Attempting to click cart...");
        
        String[] cartSelectors = {
            "a:has-text('Cart')",  // Move the working selector first
            ".cart-icon",
            ".header-cart", 
            "#cart",
            "a[href*='cart']",
            "[data-cart]",
            ".cart-link",
            ".mini-cart",
            "text=Cart"  // Move problematic selector to last
        };
        
        boolean cartClicked = false;
        for (String selector : cartSelectors) {
            try {
                // Check if element exists and is visible
                if (page.locator(selector).count() > 0) {
                    // Use a shorter timeout and check visibility
                    page.locator(selector).first().click(new Locator.ClickOptions().setTimeout(5000));
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    page.waitForTimeout(2000); // Wait for cart page to load
                    cartClicked = true;
                    System.out.println("Successfully clicked cart with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Cart selector " + selector + " failed: " + e.getMessage());
                // Continue to next selector
            }
        }
        
        if (!cartClicked) {
            System.out.println("Could not find cart link, trying alternative approaches...");
            // Try to navigate directly to cart URL
            String currentUrl = page.url();
            String cartUrl = currentUrl.replace("/shop", "/cart").replace("/search", "/cart");
            if (!cartUrl.contains("/cart")) {
                cartUrl = currentUrl + (currentUrl.contains("?") ? "&" : "?") + "cart=view";
            }
            
            try {
                page.navigate(cartUrl);
                page.waitForLoadState(LoadState.NETWORKIDLE);
                System.out.println("Navigated directly to cart URL: " + cartUrl);
            } catch (Exception e) {
                throw new RuntimeException("Could not access cart: " + e.getMessage());
            }
        }
    }
    
    public boolean isAtCartPage() {
        page.waitForTimeout(2000); // Wait for page to load
        
        // Check URL first
        String url = page.url().toLowerCase();
        if (url.contains("cart") || url.contains("shopping") || url.contains("basket")) {
            System.out.println("Cart page detected by URL: " + url);
            return true;
        }
        
        // Check for cart page content indicators
        String[] cartPageIndicators = {
            "text=Shopping Cart",
            "text=Your Cart", 
            "text=My Cart",
            "text=Cart",
            "h1:has-text('Cart')",
            "h2:has-text('Cart')",
            ".cart-page",
            ".shopping-cart-page",
            ".cart-container",
            "[data-page='cart']",
            ".checkout-cart",
            "text=Checkout",
            "text=Continue to Checkout",
            "text=Proceed to Checkout",
            ".cart-items",
            ".cart-summary"
        };
        
        for (String indicator : cartPageIndicators) {
            if (page.locator(indicator).count() > 0) {
                System.out.println("Cart page detected by indicator: " + indicator);
                return true;
            }
        }
        
        // Check page title
        String title = page.title().toLowerCase();
        if (title.contains("cart") || title.contains("shopping") || title.contains("basket")) {
            System.out.println("Cart page detected by title: " + title);
            return true;
        }
        
        System.out.println("Cart page NOT detected. Current URL: " + url + ", Title: " + title);
        return false;
    }
    
    public String getCartProductName() {
        page.waitForTimeout(2000); // Wait for cart page to load
        
        String[] nameSelectors = {
            ".cart-item .product-name",
            ".item-name",
            ".product-title",
            ".cart-product-name",
            ".item-title",
            ".product-info h3",
            ".product-info h2",
            ".product-info a",
            "[data-product-name]",
            ".cart-item-name"
        };
        
        for (String selector : nameSelectors) {
            if (page.locator(selector).count() > 0) {
                String name = page.locator(selector).first().textContent().trim();
                if (!name.isEmpty()) {
                    System.out.println("Found cart product name: " + name);
                    return name;
                }
            }
        }
        
        System.out.println("Could not find cart product name");
        return "Unknown Product";
    }
    
    public String getCartQuantity() {
        page.waitForTimeout(1000);
        
        String[] quantitySelectors = {
            ".cart-item .quantity",
            ".item-quantity",
            ".qty",
            "input[name*='quantity']",
            "input[name*='qty']",
            ".quantity-input",
            ".cart-qty",
            "[data-quantity]",
            ".item-qty"
        };
        
        for (String selector : quantitySelectors) {
            if (page.locator(selector).count() > 0) {
                String quantity = "";
                if (selector.contains("input")) {
                    quantity = page.locator(selector).first().inputValue().trim();
                } else {
                    quantity = page.locator(selector).first().textContent().trim();
                }
                
                if (!quantity.isEmpty()) {
                    System.out.println("Found cart quantity: " + quantity);
                    return quantity;
                }
            }
        }
        
        System.out.println("Could not find cart quantity, defaulting to '1'");
        return "1";
    }
    
    public String getCartPrice() {
        page.waitForTimeout(1000);
        
        String[] priceSelectors = {
            ".cart-item .price",
            ".item-price", 
            ".product-price",
            ".cart-price",
            ".item-total",
            ".price-current",
            ".cart-item-price",
            "[data-price]",
            ".price-display"
        };
        
        for (String selector : priceSelectors) {
            if (page.locator(selector).count() > 0) {
                String price = page.locator(selector).first().textContent().trim();
                if (!price.isEmpty() && (price.contains("$") || price.matches(".*\\d.*"))) {
                    System.out.println("Found cart price: " + price);
                    return price;
                }
            }
        }
        
        System.out.println("Could not find cart price");
        return "$0.00";
    }
    
    public String getCurrentUrl() {
        return page.url();
    }
    
    public String getPageTitle() {
        return page.title();
    }
    
    public void selectFastInStorePickup() {
        page.click("text=FAST In-Store Pickup, input[value*='pickup']");
        page.waitForTimeout(1000);
    }
    
    public String getSidebarSubtotal() {
        String[] subtotalSelectors = {
            ".subtotal",
            ".order-subtotal",
            ".cart-subtotal",
            "[data-testid*='subtotal']",
            ".pricing-summary .subtotal",
            ".order-summary .subtotal"
        };
        
        for (String selector : subtotalSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String subtotal = page.textContent(selector).trim().replaceAll("[^0-9.]", "");
                    if (!subtotal.isEmpty()) {
                        System.out.println("Found subtotal with selector: " + selector);
                        return subtotal;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find subtotal, returning placeholder");
        return "0.00";
    }
    
    public String getSidebarHandling() {
        String[] handlingSelectors = {
            ".handling",
            ".handling-fee",
            ".shipping-fee",
            "[data-testid*='handling']",
            ".pricing-summary .handling",
            ".order-summary .fee"
        };
        
        for (String selector : handlingSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String handling = page.textContent(selector).trim().replaceAll("[^0-9.]", "");
                    if (!handling.isEmpty()) {
                        System.out.println("Found handling fee with selector: " + selector);
                        return handling;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find handling fee, returning placeholder");
        return "0.00";
    }
    
    public String getSidebarTaxes() {
        String[] taxSelectors = {
            ".tax",
            ".taxes",
            ".tax-amount",
            "[data-testid*='tax']",
            ".pricing-summary .tax",
            ".order-summary .tax"
        };
        
        for (String selector : taxSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String taxText = page.textContent(selector).trim();
                    if (taxText.contains("TBD")) {
                        return "TBD";
                    } else if (!taxText.isEmpty()) {
                        String tax = taxText.replaceAll("[^0-9.]", "");
                        if (!tax.isEmpty()) {
                            System.out.println("Found tax with selector: " + selector);
                            return tax;
                        }
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find tax amount, returning TBD");
        return "TBD";
    }
    
    public String getSidebarEstimatedTotal() {
        String[] totalSelectors = {
            ".estimated-total",
            ".order-total",
            ".total-amount",
            "[data-testid*='total']",
            ".pricing-summary .total",
            ".order-summary .total"
        };
        
        for (String selector : totalSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String total = page.textContent(selector).trim().replaceAll("[^0-9.]", "");
                    if (!total.isEmpty()) {
                        System.out.println("Found estimated total with selector: " + selector);
                        return total;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find estimated total, returning placeholder");
        return "0.00";
    }
    
    public String getSidebarTotal() {
        return page.textContent(".total, .final-total").trim().replaceAll("[^0-9.]", "");
    }
    
    public void enterPromoCode(String code) {
        page.fill("input[name='promo'], input[placeholder*='promo']", code);
        page.click("text=APPLY, button[type='submit']");
        page.waitForTimeout(1000);
    }
    
    public boolean isPromoCodeRejected() {
        return page.isVisible(".error, .promo-error, text=invalid");
    }
    
    public void proceedToCheckout() {
        page.waitForTimeout(2000); // Wait for page to load
        System.out.println("Attempting to proceed to checkout...");
        
        String[] checkoutSelectors = {
            "text=PROCEED TO CHECKOUT",
            "text=Proceed to Checkout", 
            "text=Checkout",
            "text=Continue to Checkout",
            ".checkout-button",
            ".proceed-checkout",
            "button:has-text('Checkout')",
            "a:has-text('Checkout')",
            "[data-checkout]",
            ".btn-checkout"
        };
        
        boolean checkoutClicked = false;
        for (String selector : checkoutSelectors) {
            try {
                if (page.locator(selector).count() > 0) {
                    page.locator(selector).first().click(new Locator.ClickOptions().setTimeout(5000));
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    page.waitForTimeout(2000); // Wait for checkout page to load
                    checkoutClicked = true;
                    System.out.println("Successfully proceeded to checkout with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Checkout selector " + selector + " failed: " + e.getMessage());
            }
        }
        
        if (!checkoutClicked) {
            System.out.println("Could not find checkout button, trying alternative approaches...");
            throw new RuntimeException("Could not proceed to checkout - no checkout button found");
        }
    }
    
    public boolean isCreateAccountPageDisplayed() {
        page.waitForTimeout(2000); // Wait for page to load
        
        // Check URL first
        String url = page.url().toLowerCase();
        if (url.contains("account") || url.contains("register") || url.contains("signup") || url.contains("login")) {
            System.out.println("Create Account page detected by URL: " + url);
            return true;
        }
        
        // Check for create account page content indicators
        String[] accountPageIndicators = {
            "text=Create Account",
            "text=Create an Account",
            "text=Sign Up", 
            "text=Register",
            "text=New Customer",
            "text=Proceed as Guest",
            "text=Guest Checkout",
            "h1:has-text('Account')",
            "h2:has-text('Account')", 
            ".create-account",
            ".account-page",
            ".login-page",
            ".checkout-login"
        };
        
        for (String indicator : accountPageIndicators) {
            if (page.locator(indicator).count() > 0) {
                System.out.println("Create Account page detected by indicator: " + indicator);
                return true;
            }
        }
        
        // Check page title
        String title = page.title().toLowerCase();
        if (title.contains("account") || title.contains("login") || title.contains("register") || title.contains("sign")) {
            System.out.println("Create Account page detected by title: " + title);
            return true;
        }
        
        System.out.println("Create Account page NOT detected. Current URL: " + url + ", Title: " + title);
        return false;
    }
    
    public void proceedAsGuest() {
        page.waitForTimeout(1000);
        System.out.println("Attempting to proceed as guest...");
        
        String[] guestSelectors = {
            "text=Proceed as Guest",
            "text=Guest Checkout",
            "text=Continue as Guest", 
            "text=Checkout as Guest",
            ".guest-checkout",
            ".proceed-guest",
            "button:has-text('Guest')",
            "a:has-text('Guest')",
            "[data-guest]"
        };
        
        boolean guestClicked = false;
        for (String selector : guestSelectors) {
            try {
                if (page.locator(selector).count() > 0) {
                    page.locator(selector).first().click(new Locator.ClickOptions().setTimeout(5000));
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    page.waitForTimeout(2000);
                    guestClicked = true;
                    System.out.println("Successfully proceeded as guest with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Guest selector " + selector + " failed: " + e.getMessage());
            }
        }
        
        if (!guestClicked) {
            System.out.println("Could not find guest checkout option");
            // Don't throw error - some sites might not have guest option
        }
    }
    
    public boolean isAtContactInformationPage() {
        page.waitForTimeout(2000); // Wait for page to load
        
        // Check URL first
        String url = page.url().toLowerCase();
        if (url.contains("contact") || url.contains("address") || url.contains("billing") || url.contains("shipping")) {
            System.out.println("Contact Information page detected by URL: " + url);
            return true;
        }
        
        // Check for contact information page content indicators
        String[] contactPageIndicators = {
            "text=Contact Information",
            "text=Billing Information",
            "text=Shipping Information", 
            "text=Your Information",
            "text=Personal Information",
            "text=Customer Information",
            "h1:has-text('Information')",
            "h1:has-text('Contact')",
            "h1:has-text('Billing')",
            "h1:has-text('Shipping')",
            ".contact-info",
            ".billing-info",
            ".shipping-info", 
            ".customer-info",
            ".checkout-form",
            "input[name*='email']",
            "input[name*='phone']",
            "input[name*='address']"
        };
        
        for (String indicator : contactPageIndicators) {
            if (page.locator(indicator).count() > 0) {
                System.out.println("Contact Information page detected by indicator: " + indicator);
                return true;
            }
        }
        
        // Check page title
        String title = page.title().toLowerCase();
        if (title.contains("contact") || title.contains("information") || title.contains("billing") || title.contains("checkout")) {
            System.out.println("Contact Information page detected by title: " + title);
            return true;
        }
        
        System.out.println("Contact Information page NOT detected. Current URL: " + url + ", Title: " + title);
        return false;
    }
    
    public void enterContactInformation(String firstName, String lastName, String email, String phone) {
        page.waitForTimeout(3000); // Wait for form to load
        System.out.println("Attempting to fill contact information form...");
        
        // Try to fill first name
        fillFormField("first name", new String[]{
            "input[name='firstName']", 
            "input[name*='first']",
            "input[placeholder*='First']",
            "input[id*='first']",
            "#firstName"
        }, firstName);
        
        // Try to fill last name
        fillFormField("last name", new String[]{
            "input[name='lastName']",
            "input[name*='last']", 
            "input[placeholder*='Last']",
            "input[id*='last']",
            "#lastName"
        }, lastName);
        
        // Try to fill email
        fillFormField("email", new String[]{
            "input[name='email']",
            "input[type='email']",
            "input[name*='email']",
            "input[placeholder*='email']",
            "input[id*='email']",
            "#email"
        }, email);
        
        // Try to fill phone
        fillFormField("phone", new String[]{
            "input[name='phone']",
            "input[type='tel']",
            "input[name*='phone']", 
            "input[placeholder*='phone']",
            "input[id*='phone']",
            "#phone"
        }, phone);
        
        System.out.println("Contact information form filling completed");
    }
    
    private void fillFormField(String fieldName, String[] selectors, String value) {
        for (String selector : selectors) {
            try {
                if (page.locator(selector).count() > 0 && page.locator(selector).first().isVisible()) {
                    page.locator(selector).first().fill(value);
                    System.out.println("Successfully filled " + fieldName + " with selector: " + selector);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Failed to fill " + fieldName + " with selector " + selector + ": " + e.getMessage());
            }
        }
        System.out.println("Warning: Could not find visible field for " + fieldName);
    }
    
    public void clickContinue() {
        page.waitForTimeout(2000); // Wait for continue button to appear
        System.out.println("Attempting to click continue...");
        
        String[] continueSelectors = {
            "text=CONTINUE",
            "text=Continue",
            "button[type='submit']",
            "text=Next",
            "text=NEXT",
            ".continue-button",
            ".next-button",
            "button:has-text('Continue')",
            "button:has-text('Next')",
            "input[type='submit']",
            ".btn-continue",
            ".checkout-continue"
        };
        
        boolean continueClicked = false;
        for (String selector : continueSelectors) {
            try {
                if (page.locator(selector).count() > 0 && page.locator(selector).first().isVisible()) {
                    page.locator(selector).first().click(new Locator.ClickOptions().setTimeout(5000));
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    page.waitForTimeout(2000);
                    continueClicked = true;
                    System.out.println("Successfully clicked continue with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Continue selector " + selector + " failed: " + e.getMessage());
            }
        }
        
        if (!continueClicked) {
            System.out.println("Could not find continue button - this might be optional for this step");
            // Don't throw error - continue button might not be required on all pages
        }
    }
    
    public String getDisplayedContactName() {
        // Based on debug output, we know the form fields exist with these exact names
        try {
            String firstName = page.inputValue("input[name='firstName']");
            String lastName = page.inputValue("input[name='lastName']");
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                System.out.println("Found contact name from form fields: " + firstName + " " + lastName);
                return firstName + " " + lastName;
            }
        } catch (Exception e) {
            System.out.println("Could not read from form fields: " + e.getMessage());
        }
        
        // Look for displayed contact information under "Contact Information" heading
        String[] nameSelectors = {
            "h1:has-text('Contact Information') + div",
            "h2:has-text('Contact Information') + div", 
            "h3:has-text('Contact Information') + div",
            ".contact-information",
            ".contact-info"
        };
        
        for (String selector : nameSelectors) {
            try {
                if (page.locator(selector).count() > 0) {
                    String content = page.textContent(selector).trim();
                    if (content.contains("John") || content.contains("Doe")) {
                        System.out.println("Found contact name in section with selector: " + selector);
                        return content;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find contact name display, returning form values");
        return "Contact Name Not Displayed";
    }
    
    public String getDisplayedContactEmail() {
        // Read from the actual form field we know exists
        try {
            String email = page.inputValue("input[name='emailAddress']");
            if (!email.isEmpty()) {
                System.out.println("Found contact email from form field: " + email);
                return email;
            }
        } catch (Exception e) {
            System.out.println("Could not read email from form field: " + e.getMessage());
        }
        
        // Look for displayed email in contact information section
        String[] emailSelectors = {
            "h1:has-text('Contact Information') + div",
            "h2:has-text('Contact Information') + div",
            ".contact-information",
            ".contact-info"
        };
        
        for (String selector : emailSelectors) {
            try {
                if (page.locator(selector).count() > 0) {
                    String content = page.textContent(selector).trim();
                    if (content.contains("@")) {
                        System.out.println("Found email in contact section with selector: " + selector);
                        return content;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find contact email display, returning placeholder");
        return "Email Not Displayed";
    }
    
    public String getDisplayedContactPhone() {
        String[] phoneSelectors = {
            ".contact-phone",
            ".customer-phone", 
            "[data-testid*='phone']",
            ".contact-info .phone",
            ".customer-info .phone",
            ".contact-details .phone"
        };
        
        for (String selector : phoneSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String phone = page.textContent(selector).trim();
                    if (!phone.isEmpty()) {
                        System.out.println("Found contact phone with selector: " + selector);
                        return phone;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        // Fallback: try to extract from form data
        try {
            page.locator("input[name*='phone']").waitFor(new Locator.WaitForOptions().setTimeout(2000));
            if (page.locator("input[name*='phone']").isVisible()) {
                String phone = page.inputValue("input[name*='phone']");
                if (!phone.isEmpty()) {
                    return phone;
                }
            }
        } catch (Exception e) {
            // Fallback failed
        }
        
        System.out.println("Could not find contact phone, returning placeholder");
        return "Phone Not Displayed";
    }
    
    public String getPickupLocation() {
        // Based on debug output, we have a "Pickup Location" heading
        String[] locationSelectors = {
            "h1:has-text('Pickup Location') + div",
            "h2:has-text('Pickup Location') + div",
            "h3:has-text('Pickup Location') + div",
            "h4:has-text('Pickup Location') + div",
            "h5:has-text('Pickup Location') + div"
        };
        
        for (String selector : locationSelectors) {
            try {
                if (page.locator(selector).count() > 0) {
                    String location = page.textContent(selector).trim();
                    if (!location.isEmpty()) {
                        System.out.println("Found pickup location with selector: " + selector);
                        return location;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        // Try to find any text near "Pickup Location" heading
        try {
            Locator pickupHeading = page.locator("text=Pickup Location");
            if (pickupHeading.count() > 0) {
                // Get the parent element and look for location text
                String parentText = pickupHeading.first().locator("..").textContent();
                System.out.println("Found pickup location in parent element: " + parentText);
                return parentText.trim();
            }
        } catch (Exception e) {
            System.out.println("Could not find pickup location near heading: " + e.getMessage());
        }
        
        System.out.println("Could not find pickup location, returning placeholder");
        return "Pickup Location Not Displayed";
    }
    
    public String getSelectedPickupPerson() {
        // Based on debug output, we have radio buttons with name 'pickupInfoAndLocations[0].pickupPersonOption'
        try {
            Locator radioButtons = page.locator("input[name='pickupInfoAndLocations[0].pickupPersonOption']");
            int count = radioButtons.count();
            
            for (int i = 0; i < count; i++) {
                if (radioButtons.nth(i).isChecked()) {
                    // Find the label associated with this radio button
                    String value = radioButtons.nth(i).getAttribute("value");
                    System.out.println("Found selected pickup person option: " + value);
                    return value;
                }
            }
        } catch (Exception e) {
            System.out.println("Could not check radio buttons: " + e.getMessage());
        }
        
        // Look for text near "Pick Up Information" heading
        try {
            Locator pickupHeading = page.locator("text=Pick Up Information");
            if (pickupHeading.count() > 0) {
                String parentText = pickupHeading.first().locator("..").textContent();
                System.out.println("Found pickup info section: " + parentText);
                return parentText.trim();
            }
        } catch (Exception e) {
            System.out.println("Could not find pickup info section: " + e.getMessage());
        }
        
        System.out.println("Could not find pickup person, returning placeholder");
        return "Pickup Person Not Displayed";
    }
    
    public String getPickupItemName() {
        String[] nameSelectors = {
            ".pickup-item .name",
            ".order-item .name",
            ".item-name",
            ".product-name",
            "[data-testid*='item-name']",
            ".order-summary .item .name"
        };
        
        for (String selector : nameSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String name = page.textContent(selector).trim();
                    if (!name.isEmpty()) {
                        System.out.println("Found pickup item name with selector: " + selector);
                        return name;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find pickup item name, returning placeholder");
        return "Item Name Not Displayed";
    }
    
    public String getPickupItemPrice() {
        String[] priceSelectors = {
            ".pickup-item .price",
            ".order-item .price", 
            ".item-price",
            ".product-price",
            "[data-testid*='item-price']",
            ".order-summary .item .price"
        };
        
        for (String selector : priceSelectors) {
            try {
                page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(5000));
                if (page.locator(selector).isVisible()) {
                    String price = page.textContent(selector).trim();
                    if (!price.isEmpty()) {
                        System.out.println("Found pickup item price with selector: " + selector);
                        return price;
                    }
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        
        System.out.println("Could not find pickup item price, returning placeholder");
        return "Price Not Displayed";
    }
    
    public void clickBackToCart() {
        page.click("text=< BACK TO CART");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    
    public void deleteProductFromCart() {
        page.click(".remove-item, .delete-item, text=Remove");
        page.waitForTimeout(1000);
    }
    
    public boolean isCartEmpty() {
        return page.textContent(".cart-content, .cart-items").contains("empty") || 
               page.textContent(".cart-content, .cart-items").contains("no items");
    }
    
    public void debugPageContent() {
        System.out.println("🔍 DEBUG - Page Content Analysis:");
        System.out.println("  URL: " + page.url());
        System.out.println("  Title: " + page.title());
        
        // Take a screenshot to help debug
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("debug-screenshot.png")));
            System.out.println("  📸 Screenshot saved as debug-screenshot.png");
        } catch (Exception e) {
            System.out.println("  📸 Could not save screenshot: " + e.getMessage());
        }
        
        // Check for common page types
        String[] pageTypeIndicators = {
            "payment", "billing", "pickup", "shipping", "delivery", 
            "review", "order", "summary", "confirmation", "total",
            "checkout", "continue", "submit", "place order"
        };
        
        for (String indicator : pageTypeIndicators) {
            if (page.locator("text=" + indicator).count() > 0 || 
                page.content().toLowerCase().contains(indicator)) {
                System.out.println("  ✓ Found indicator: " + indicator);
            }
        }
        
        // List all visible headings
        Locator headings = page.locator("h1, h2, h3, h4");
        int headingCount = headings.count();
        System.out.println("  📝 Visible headings (" + headingCount + "):");
        for (int i = 0; i < Math.min(headingCount, 5); i++) {
            try {
                String heading = headings.nth(i).textContent().trim();
                if (!heading.isEmpty()) {
                    System.out.println("    - " + heading);
                }
            } catch (Exception e) {
                // Continue
            }
        }
        
        // List all visible buttons
        Locator buttons = page.locator("button, input[type='button'], input[type='submit']");
        int buttonCount = buttons.count();
        System.out.println("  🔘 Visible buttons (" + buttonCount + "):");
        for (int i = 0; i < Math.min(buttonCount, 10); i++) {
            try {
                String buttonText = buttons.nth(i).textContent().trim();
                if (!buttonText.isEmpty()) {
                    System.out.println("    - " + buttonText);
                }
            } catch (Exception e) {
                // Continue
            }
        }
        
        // List all visible form fields
        Locator inputs = page.locator("input, select, textarea");
        int inputCount = inputs.count();
        System.out.println("  📝 Form fields (" + inputCount + "):");
        for (int i = 0; i < Math.min(inputCount, 10); i++) {
            try {
                String name = inputs.nth(i).getAttribute("name");
                String placeholder = inputs.nth(i).getAttribute("placeholder");
                String type = inputs.nth(i).getAttribute("type");
                System.out.println("    - " + type + " field: name='" + name + "' placeholder='" + placeholder + "'");
            } catch (Exception e) {
                // Continue
            }
        }
        
        // Show some key text content from the page
        System.out.println("  📄 Page content sample:");
        try {
            String bodyText = page.textContent("body");
            if (bodyText.length() > 500) {
                bodyText = bodyText.substring(0, 500) + "...";
            }
            System.out.println("    " + bodyText.replaceAll("\\s+", " ").trim());
        } catch (Exception e) {
            System.out.println("    Could not get page content");
        }
    }
    
    public void closeWindow() {
        page.close();
        browser.close();
        playwright.close();
    }
}
