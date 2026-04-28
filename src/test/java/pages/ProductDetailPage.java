package pages;

import base.BasePage;
import utils.WaitUtil;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ProductDetailPage - Page Object for Product Detail page
 */
public class ProductDetailPage extends BasePage {

    @FindBy(css = ".inventory_details_name")
    private WebElement productName;

    @FindBy(css = ".inventory_details_desc")
    private WebElement productDescription;

    @FindBy(css = ".inventory_details_price")
    private WebElement productPrice;

    @FindBy(css = "[data-test^='add-to-cart']")
    private WebElement addToCartButton;

    @FindBy(css = "[data-test^='remove']")
    private WebElement removeFromCartButton;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    public double getProductPrice() {
        return Double.parseDouble(getText(productPrice).replace("$", ""));
    }

    public ProductDetailPage addToCart() {
        log.info("Adding product to cart from detail page");
        WaitUtil.waitForElementClickable(driver, addToCartButton);
        jsClick(addToCartButton);
        try { Thread.sleep(1500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public ProductDetailPage removeFromCart() {
        log.info("Removing product from cart from detail page");
        click(removeFromCartButton);
        return this;
    }

    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(addToCartButton);
    }

    public boolean isRemoveButtonDisplayed() {
        return isDisplayed(removeFromCartButton);
    }

    public HomePage goBackToProducts() {
        log.info("Going back to products page — navigating directly");
        driver.get("https://www.saucedemo.com/inventory.html");
        try { Thread.sleep(1500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new HomePage(driver);
    }
}
