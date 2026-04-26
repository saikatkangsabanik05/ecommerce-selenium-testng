package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HomePage - Page Object for the Products/Home page after login
 */
public class HomePage extends BasePage {

    // ---- Locators ----
    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".inventory_item")
    private List<WebElement> productItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(css = ".product_sort_container")
    private WebElement sortDropdown;

    @FindBy(css = "#shopping_cart_container")
    private WebElement cartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenu;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppLink;

    // ---- Constructor ----
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ---- Actions ----
    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isHomePageDisplayed() {
        return isDisplayed(pageTitle) && getText(pageTitle).equals("Products");
    }

    public int getProductCount() {
        return productItems.size();
    }

    public List<String> getAllProductNames() {
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getAllProductPrices() {
        return productPrices.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public void addProductToCartByName(String productName) {
        log.info("Adding product to cart: {}", productName);
        // Build a dynamic XPath to find the Add to Cart button for this product
        String buttonId = "add-to-cart-" + productName.toLowerCase()
                .replace(" ", "-")
                .replace("(", "")
                .replace(")", "")
                .replace(".", "");
        driver.findElement(By.id(buttonId)).click();
    }

    public void addProductToCartByIndex(int index) {
        List<WebElement> addButtons = driver.findElements(By.cssSelector("[data-test^='add-to-cart']"));
        if (index < addButtons.size()) {
            log.info("Adding product at index {} to cart", index);
            addButtons.get(index).click();
        }
    }

    public void removeProductFromCartByIndex(int index) {
        List<WebElement> removeButtons = driver.findElements(By.cssSelector("[data-test^='remove']"));
        if (index < removeButtons.size()) {
            log.info("Removing product at index {} from cart", index);
            removeButtons.get(index).click();
        }
    }

    public ProductDetailPage clickOnProductByName(String productName) {
        log.info("Clicking on product: {}", productName);
        productNames.stream()
                .filter(e -> e.getText().equals(productName))
                .findFirst()
                .ifPresent(WebElement::click);
        return new ProductDetailPage(driver);
    }

    public CartPage goToCart() {
        log.info("Navigating to Cart");
        click(cartIcon);
        return new CartPage(driver);
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            return 0;
        }
    }

    public void sortBy(String option) {
        log.info("Sorting products by: {}", option);
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(option);
    }

    public void sortByValue(String value) {
        new Select(sortDropdown).selectByValue(value);
    }

    public LoginPage logout() {
        log.info("Logging out");
        click(hamburgerMenu);
        click(logoutLink);
        return new LoginPage(driver);
    }

    public void resetAppState() {
        click(hamburgerMenu);
        click(resetAppLink);
    }
}
