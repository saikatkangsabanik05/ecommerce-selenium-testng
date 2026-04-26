package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * LoginTest - Covers all login-related test scenarios
 *
 * Test Cases:
 *  TC_LOGIN_001 - Valid login with standard user
 *  TC_LOGIN_002 - Login with locked-out user
 *  TC_LOGIN_003 - Login with invalid credentials
 *  TC_LOGIN_004 - Login with empty username
 *  TC_LOGIN_005 - Login with empty password
 *  TC_LOGIN_006 - Login with both fields empty
 *  TC_LOGIN_007 - Successful logout after login
 *  TC_LOGIN_008 - Data-driven login with multiple invalid credentials
 */
public class LoginTest extends BaseTest {

    private LoginPage getLoginPage() {
        return new LoginPage(getDriver());
    }

    // -------------------------------------------------------
    // TC_LOGIN_001
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_LOGIN_001 - Verify successful login with valid credentials")
    public void testValidLogin() {
        log.info("TC_LOGIN_001: Valid login test");
        LoginPage loginPage = getLoginPage();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
            "Login page should be displayed");

        HomePage homePage = loginPage.login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );

        Assert.assertTrue(homePage.isHomePageDisplayed(),
            "Home page should be displayed after successful login");
        Assert.assertEquals(homePage.getPageTitle(), "Products",
            "Page title should be 'Products'");

        log.info("TC_LOGIN_001 PASSED: User logged in successfully");
    }

    // -------------------------------------------------------
    // TC_LOGIN_002
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_LOGIN_002 - Verify error for locked-out user")
    public void testLockedOutUserLogin() {
        log.info("TC_LOGIN_002: Locked-out user login test");
        LoginPage loginPage = getLoginPage();

        loginPage.enterUsername(ConfigReader.getLockedUser())
                 .enterPassword(ConfigReader.getPassword())
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error message should be displayed for locked user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
            "Error should mention 'locked out'");

        log.info("TC_LOGIN_002 PASSED: Locked user error displayed correctly");
    }

    // -------------------------------------------------------
    // TC_LOGIN_003
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_LOGIN_003 - Verify error for invalid credentials")
    public void testInvalidCredentials() {
        log.info("TC_LOGIN_003: Invalid credentials test");
        LoginPage loginPage = getLoginPage();

        loginPage.enterUsername("invalid_user")
                 .enterPassword("wrong_password")
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
            "Error message text mismatch");

        log.info("TC_LOGIN_003 PASSED");
    }

    // -------------------------------------------------------
    // TC_LOGIN_004
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_LOGIN_004 - Verify error when username is empty")
    public void testEmptyUsername() {
        log.info("TC_LOGIN_004: Empty username test");
        LoginPage loginPage = getLoginPage();

        loginPage.enterPassword(ConfigReader.getPassword())
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error should appear for empty username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
            "Error should say username is required");

        log.info("TC_LOGIN_004 PASSED");
    }

    // -------------------------------------------------------
    // TC_LOGIN_005
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_LOGIN_005 - Verify error when password is empty")
    public void testEmptyPassword() {
        log.info("TC_LOGIN_005: Empty password test");
        LoginPage loginPage = getLoginPage();

        loginPage.enterUsername(ConfigReader.getStandardUser())
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error should appear for empty password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
            "Error should say password is required");

        log.info("TC_LOGIN_005 PASSED");
    }

    // -------------------------------------------------------
    // TC_LOGIN_006
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_LOGIN_006 - Verify error when both fields are empty")
    public void testBothFieldsEmpty() {
        log.info("TC_LOGIN_006: Both fields empty test");
        LoginPage loginPage = getLoginPage();

        loginPage.clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error should appear when both fields are empty");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
            "First validation should be username");

        log.info("TC_LOGIN_006 PASSED");
    }

    // -------------------------------------------------------
    // TC_LOGIN_007
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_LOGIN_007 - Verify user can logout successfully")
    public void testLogout() {
        log.info("TC_LOGIN_007: Logout test");
        LoginPage loginPage = getLoginPage();

        HomePage homePage = loginPage.login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );

        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should be on home page");

        LoginPage loginPageAfterLogout = homePage.logout();

        Assert.assertTrue(loginPageAfterLogout.isLoginPageDisplayed(),
            "Login page should be displayed after logout");

        log.info("TC_LOGIN_007 PASSED: Logout successful");
    }

    // -------------------------------------------------------
    // TC_LOGIN_008 - Data-Driven
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          dataProvider = "invalidCredentials",
          description = "TC_LOGIN_008 - Data-driven invalid credential tests")
    public void testInvalidLoginDataDriven(String username, String password, String expectedError) {
        log.info("TC_LOGIN_008: Data-driven login - user='{}', expectedError='{}'", username, expectedError);
        LoginPage loginPage = getLoginPage();

        loginPage.enterUsername(username)
                 .enterPassword(password)
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be shown");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),
            "Expected error: '" + expectedError + "' but got: '" + loginPage.getErrorMessage() + "'");

        log.info("TC_LOGIN_008 data row PASSED");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentialsData() {
        return new Object[][] {
            { "",               "",               "Username is required"                      },
            { "standard_user",  "",               "Password is required"                      },
            { "",               "secret_sauce",   "Username is required"                      },
            { "bad_user",       "bad_pass",       "Username and password do not match"        },
            { "locked_out_user","secret_sauce",   "locked out"                               },
        };
    }
}
