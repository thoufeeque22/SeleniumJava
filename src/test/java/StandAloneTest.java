import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;


public class StandAloneTest {
    static String orderItem = "ZARA COAT 3";
    static String country = "India";

    static WebDriver driver_init() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/trafique/selenium-profile");
        options.addArguments("profile-directory=Profile 1");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Makes it look more human
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    static void login(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://rahulshettyacademy.com/client");
        driver.findElement(By.id("userEmail")).sendKeys("a@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("easypw");
        driver.findElement(By.id("login")).click();
    }

    static void addToCart(WebDriver driver, WebDriverWait wait) {
        WebElement itemCard = driver.findElement(By.className("card-body"));
        System.out.println("itemCard.getText() = " + itemCard.getText());
        if (itemCard.getText().contains(orderItem)) {
            itemCard.findElement(By.xpath(".//button[contains(text(),'Add To Cart')]")).click();
            System.out.println(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[aria-label='Product Added To Cart']"))).getText());
        }
    }

    static void gotoCart(WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".fa-shopping-cart"))).click();
    }

    static void verifyCartItems(WebDriver driver) {
        String cartItemText = driver.findElement(By.cssSelector(".infoWrap")).getText();
        Assert.assertTrue(cartItemText.contains(orderItem));
    }

    static void checkout(WebDriver driver) {
        driver.findElement(By.cssSelector(".subtotal button")).click();

        String placeOrderText = driver.findElement(By.cssSelector(".item__title")).getText();
        Assert.assertTrue(placeOrderText.contains(orderItem));

        driver.findElement(By.cssSelector("[placeholder='Select Country']")).sendKeys(country);

        List<WebElement> countryList = driver.findElements(By.cssSelector(".list-group-item"));
        for (WebElement listCountry : countryList) {
            if (listCountry.getText().equals(country)) {
                listCountry.click();
                break;
            }
        }
        driver.findElement(By.cssSelector(".action__submit")).click();
    }

    static void verifyOrder(WebDriver driver) {
        String orderId = driver.findElement(By.cssSelector("label[class='ng-star-inserted']")).getText().split(" ")[1];

        driver.findElement(By.xpath("//button[contains(text(),'ORDERS')]")).click();

        List<WebElement> finalOrderIds = driver.findElements(By.cssSelector("tbody th"));
        for (WebElement finalId: finalOrderIds) {
            Assert.assertTrue(finalId.getText().contains(orderId));
            break;
        }
    }

    public static void main(String[] args) throws IOException {

        WebDriver driver = driver_init();

        TakesScreenshot ss = (TakesScreenshot) driver;

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            login(driver);

            addToCart(driver, wait);

            gotoCart(wait);

            verifyCartItems(driver);

            checkout(driver);

            verifyOrder(driver);

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
