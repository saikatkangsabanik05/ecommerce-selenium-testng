# 🛒 eCommerce Selenium TestNG Automation Framework

A production-ready test automation framework for eCommerce applications built with **Java + Selenium WebDriver + TestNG**, following the **Page Object Model (POM)** design pattern.

> **Target Application:** [SauceDemo](https://www.saucedemo.com) — a demo eCommerce site  
> **Framework:** Java 11 | Selenium 4 | TestNG 7 | Maven

---

## 📁 Project Structure

```
ecommerce-selenium-testng/
├── pom.xml
├── src/test/
│   ├── resources/
│   │   ├── config.properties       # Browser, URL, credentials, timeouts
│   │   ├── testng.xml              # Test suite (Smoke + Regression)
│   │   └── log4j2.xml              # Logging configuration
│   └── java/
│       ├── base/
│       │   ├── BaseTest.java       # @BeforeMethod/@AfterMethod (driver setup)
│       │   └── BasePage.java       # Common Selenium actions
│       ├── pages/                  # Page Object Model
│       │   ├── LoginPage.java
│       │   ├── HomePage.java
│       │   ├── ProductDetailPage.java
│       │   ├── CartPage.java
│       │   └── CheckoutPage.java
│       ├── tests/                  # TestNG Test Classes
│       │   ├── LoginTest.java      # 8 test cases
│       │   ├── HomePageTest.java   # 6 test cases
│       │   ├── ProductTest.java    # 7 test cases
│       │   ├── CartTest.java       # 9 test cases
│       │   ├── CheckoutTest.java   # 9 test cases
│       │   └── SortingTest.java    # 4 test cases
│       ├── utils/
│       │   ├── ConfigReader.java
│       │   ├── DriverManager.java  # ThreadLocal WebDriver
│       │   ├── WaitUtil.java
│       │   ├── ScreenshotUtil.java
│       │   └── ExcelDataProvider.java
│       └── listeners/
│           ├── ExtentReportListener.java
│           ├── RetryListener.java
│           └── ScreenshotListener.java
├── screenshots/                    # Failure screenshots (auto-created)
├── reports/                        # HTML reports (auto-created)
└── test-data/                      # Excel data files
```

---

## ✅ Test Coverage (43 Test Cases)

| Module         | Test File          | Cases | Groups              |
|----------------|--------------------|-------|---------------------|
| Login          | LoginTest          | 8     | smoke, regression   |
| Home/Products  | HomePageTest       | 6     | smoke, regression   |
| Product Detail | ProductTest        | 7     | smoke, regression   |
| Shopping Cart  | CartTest           | 9     | smoke, regression   |
| Checkout       | CheckoutTest       | 9     | smoke, regression   |
| Sorting        | SortingTest        | 4     | regression          |

---

## 🔧 Tech Stack

| Tool               | Purpose                              |
|--------------------|--------------------------------------|
| Java 11            | Programming language                 |
| Selenium 4         | Browser automation                   |
| TestNG 7           | Test framework & assertions          |
| Maven              | Build & dependency management        |
| WebDriverManager   | Automatic browser driver management  |
| ExtentReports 5    | Rich HTML test reports               |
| Log4j2             | Logging                              |
| Apache POI         | Excel-based data-driven testing      |
| Java Faker         | Fake test data generation            |

---

## 🚀 How to Run

### Prerequisites
- Java 11+
- Maven 3.8+
- Chrome / Firefox / Edge browser

### Install & Run

```bash
# 1. Clone the repo
git clone <repo-url>
cd ecommerce-selenium-testng

# 2. Run all tests (default: Chrome)
mvn test

# 3. Run only Smoke tests
mvn test -Dgroups=smoke

# 4. Run only Regression tests
mvn test -Dgroups=regression

# 5. Run with a specific browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge

# 6. Run in headless mode (great for CI/CD)
mvn test -Dheadless=true

# 7. Combined options
mvn test -Dbrowser=chrome -Dheadless=true -Dgroups=smoke
```

---

## 📊 Reports & Logs

After execution:
- **HTML Report:** `reports/ECommerceTestReport_<timestamp>.html`
- **Log File:** `reports/test-execution.log`
- **Screenshots:** `screenshots/<testName>_<timestamp>.png` (on failure)

---

## ⚙️ Configuration

Edit `src/test/resources/config.properties`:

```properties
base.url=https://www.saucedemo.com
browser=chrome           # chrome | firefox | edge
headless=false
implicit.wait=10
explicit.wait=20
retry.count=1
screenshot.on.failure=true
```

---

## 🏗️ Key Design Decisions

| Feature           | Implementation                                |
|-------------------|-----------------------------------------------|
| Thread Safety     | `ThreadLocal<WebDriver>` in DriverManager     |
| Parallel Exec     | TestNG `parallel="methods"` in testng.xml     |
| Reporting         | ExtentReports listener with embedded screenshots |
| Retry Logic       | Auto-retry on failure via `IAnnotationTransformer` |
| Data-Driven       | `@DataProvider` + Apache POI for Excel data   |
| Config Override   | System property overrides (`-Dbrowser=firefox`) |

---

## 🧪 Sample Test (End-to-End Checkout)

```java
@Test(groups = {"smoke"}, description = "Complete checkout flow")
public void testCompleteCheckoutEndToEnd() {
    LoginPage loginPage = new LoginPage(getDriver());
    HomePage homePage = loginPage.login("standard_user", "secret_sauce");
    homePage.addProductToCartByIndex(0);
    CartPage cartPage = homePage.goToCart();
    CheckoutPage checkoutPage = cartPage.proceedToCheckout();
    checkoutPage.fillShippingInfo("John", "Doe", "10001")
                .clickContinue()
                .clickFinish();
    Assert.assertTrue(checkoutPage.isOrderConfirmed());
}
```

---

## 📌 CI/CD Integration (GitHub Actions)

```yaml
name: eCommerce Selenium Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
      - name: Run Tests
        run: mvn test -Dheadless=true -Dbrowser=chrome
      - name: Upload Reports
        uses: actions/upload-artifact@v3
        with:
          name: test-reports
          path: reports/
```

---

## 👨‍💻 Author

**eCommerce Test Automation Framework**  
Built with ❤️ using Java + Selenium + TestNG
