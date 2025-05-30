import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import rahulshettyacademy.abstractComponents.AbstractComponent;
import rahulshettyacademy.pageobjects.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;


public class StandAloneTest {
    static String orderItem = "ZARA COAT 3";
    static String country = "India";

    public static void main(String[] args) throws IOException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/trafique/selenium-profile");
        options.addArguments("profile-directory=Profile 1");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Makes it look more human
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);

        TakesScreenshot ss = (TakesScreenshot) driver;

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            AbstractComponent abstractComponent = new AbstractComponent(driver);

            LandingPage landingPage = new LandingPage(driver);
            landingPage.goTo("https://rahulshettyacademy.com/client");
            landingPage.loginApplication("a@gmail.com", "easypw");

            ProductCatalog productCatalog = new ProductCatalog(driver);
            productCatalog.addToCart(orderItem);

            abstractComponent.goToCart();

            Checkout checkout = new Checkout(driver);
            Boolean match = checkout.VerifyCart(orderItem);
            Assert.assertTrue(match);
            checkout.checkoutCart();

            PlaceOrder placeOrder = new PlaceOrder(driver);
            String placeOrderText = placeOrder.verifyTitle(orderItem);
            Assert.assertTrue(placeOrderText.contains(orderItem));
            placeOrder.submitOrder(country);

            VerifyOrder verifyOrder = new VerifyOrder(driver);
            String confirmMessage = verifyOrder.verifyOrderConfirmation();
            Assert.assertEquals(confirmMessage, "THANKYOU FOR THE ORDER.");
            String orderId = verifyOrder.captureOrderId();
            abstractComponent.goToOrders();
            String finalId = verifyOrder.verifyOrderId(orderId);
            Assert.assertTrue(finalId.equals(orderId));

        } catch (Exception e) {
            e.printStackTrace();
            File src = ss.getScreenshotAs(OutputType.FILE);
            File dest = new File("screenshots/error_screenshot.png");
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        finally {
            driver.quit();
        }
    }
}
