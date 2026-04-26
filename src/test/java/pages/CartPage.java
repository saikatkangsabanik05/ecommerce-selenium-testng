package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WaitUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(css = ".inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
        // Wait for cart page to fully load
        try { Thread.sleep(2000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isCartPageDisplayed() {
        try {
            WaitUtil.waitForElementVisible(driver, pageTitle);
            return getText(pageTitle).equals("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        try {
            return driver.findElements(By.cssSelector(".cart_item")).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getCartItemNames() {
        return driver.findElements(By.cssSelector(".inventory_item_name"))
                .stream().map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getCartItemPrices() {
        return driver.findElements(By.cssSelector(".inventory_item_price"))
                .stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public boolean isItemInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    public void removeItemFromCart(String productName) {
        String removeId = "remove-" + productName.toLowerCase()
                .replace(" ", "-")
                .replace("(", "")
                .replace(")", "")
                .replace(".", "");
        try {
            WebElement btn = driver.findElement(By.id(removeId));
            jsClick(btn);
            Thread.sleep(500);
        } catch (Exception e) {
            log.error("Could not remove item: {}", productName);
        }
    }

    public void removeAllItems() {
        List<WebElement> removeButtons = driver.findElements(
            By.cssSelector("[data-test^='remove']"));
        for (WebElement btn : removeButtons) {
            jsClick(btn);
            try { Thread.sleep(300); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isCartEmpty() {
        return driver.findElements(By.cssSelector(".cart_item")).isEmpty();
    }

    public HomePage continueShopping() {
        WaitUtil.waitForElementClickable(driver, continueShoppingButton);
        jsClick(continueShoppingButton);
        WaitUtil.waitForUrlContains(driver, "inventory");
        return new HomePage(driver);
    }

    public CheckoutPage proceedToCheckout() {
        WaitUtil.waitForElementClickable(driver, checkoutButton);
        jsClick(checkoutButton);
        WaitUtil.waitForUrlContains(driver, "checkout-step-one");
        return new CheckoutPage(driver);
    }
}