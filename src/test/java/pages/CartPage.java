package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CartPage - Page Object for the Shopping Cart page
 */
public class CartPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(css = ".cart_quantity")
    private List<WebElement> cartItemQuantities;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isCartPageDisplayed() {
        return isDisplayed(pageTitle) && getText(pageTitle).equals("Your Cart");
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public List<String> getCartItemNames() {
        return cartItemNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<Double> getCartItemPrices() {
        return cartItemPrices.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public boolean isItemInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    public void removeItemFromCart(String productName) {
        log.info("Removing '{}' from cart", productName);
        String removeId = "remove-" + productName.toLowerCase()
                .replace(" ", "-")
                .replace("(", "")
                .replace(")", "")
                .replace(".", "");
        driver.findElement(By.id(removeId)).click();
    }

    public void removeAllItems() {
        List<WebElement> removeButtons = driver.findElements(By.cssSelector("[data-test^='remove']"));
        for (WebElement btn : removeButtons) {
            btn.click();
        }
        log.info("All cart items removed");
    }

    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    public HomePage continueShopping() {
        log.info("Continuing shopping");
        click(continueShoppingButton);
        return new HomePage(driver);
    }

    public CheckoutPage proceedToCheckout() {
        log.info("Proceeding to checkout");
        click(checkoutButton);
        return new CheckoutPage(driver);
    }
}
