package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration properties from config.properties
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    // Use absolute path so Eclipse & Maven both find the file correctly
    private static final String CONFIG_PATH = System.getProperty("user.dir")
            + "/src/test/resources/config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
            log.info("Configuration loaded successfully from: {}", CONFIG_PATH);
        } catch (IOException e) {
            log.warn("config.properties not found at '{}', using built-in defaults. Error: {}",
                    CONFIG_PATH, e.getMessage());
            // Don't throw — fall through to getProperty() defaults below
        }
    }

    public static String getProperty(String key) {
        // 1. System property wins (-Dbrowser=chrome from CLI or Eclipse VM args)
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // 2. config.properties value
        if (properties != null) {
            value = properties.getProperty(key);
        }
        // 3. Built-in safe defaults (so Eclipse runs work out of the box)
        if (value == null || value.isEmpty()) {
            log.warn("Property '{}' not found — using built-in default", key);
            switch (key) {
                case "browser":               return "chrome";
                case "headless":              return "false";
                case "implicit.wait":         return "10";
                case "explicit.wait":         return "20";
                case "page.load.timeout":     return "30";
                case "retry.count":           return "1";
                case "screenshot.on.failure": return "true";
                case "screenshot.path":       return "screenshots/";
                case "report.path":           return "reports/";
                case "report.name":           return "ECommerceTestReport";
                case "standard.user":         return "standard_user";
                case "locked.user":           return "locked_out_user";
                case "performance.user":      return "performance_glitch_user";
                case "password":              return "secret_sauce";
                case "base.url":              return "https://www.saucedemo.com";
                case "test.firstname":        return "John";
                case "test.lastname":         return "Doe";
                case "test.zipcode":          return "10001";
                default:
                    log.error("No default defined for property: '{}'", key);
                    return "";
            }
        }
        return value;
    }

    public static String getBaseUrl()            { return getProperty("base.url"); }
    public static String getBrowser()            { return getProperty("browser"); }
    public static boolean isHeadless()           { return Boolean.parseBoolean(getProperty("headless")); }
    public static int getImplicitWait()          { return Integer.parseInt(getProperty("implicit.wait")); }
    public static int getExplicitWait()          { return Integer.parseInt(getProperty("explicit.wait")); }
    public static int getPageLoadTimeout()       { return Integer.parseInt(getProperty("page.load.timeout")); }
    public static String getStandardUser()       { return getProperty("standard.user"); }
    public static String getPassword()           { return getProperty("password"); }
    public static String getLockedUser()         { return getProperty("locked.user"); }
    public static String getPerformanceUser()    { return getProperty("performance.user"); }
    public static String getScreenshotPath()     { return getProperty("screenshot.path"); }
    public static String getReportPath()         { return getProperty("report.path"); }
    public static String getReportName()         { return getProperty("report.name"); }
    public static int getRetryCount()            { return Integer.parseInt(getProperty("retry.count")); }
    public static boolean isScreenshotOnFailure(){ return Boolean.parseBoolean(getProperty("screenshot.on.failure")); }
    public static String getTestFirstName()      { return getProperty("test.firstname"); }
    public static String getTestLastName()       { return getProperty("test.lastname"); }
    public static String getTestZipCode()        { return getProperty("test.zipcode"); }
}
