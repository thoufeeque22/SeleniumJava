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

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class StandAloneTest {
    public static void main(String[] args) throws IOException {
        String orderItem = "ZARA COAT 3";
        String country = "India";
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/trafique/selenium-profile");
        options.addArguments("profile-directory=Profile 1");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Makes it look more human
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);
        TakesScreenshot ss = (TakesScreenshot) driver;
        try {
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("https://rahulshettyacademy.com/client");
            driver.findElement(By.id("userEmail")).sendKeys("a@gmail.com");
            driver.findElement(By.id("userPassword")).sendKeys("easypw");
            driver.findElement(By.id("login")).click();

            WebElement itemCard = driver.findElement(By.className("card-body"));
            System.out.println("itemCard.getText() = " + itemCard.getText());
            if (itemCard.getText().contains(orderItem)) {
                itemCard.findElement(By.xpath(".//button[contains(text(),'Add To Cart')]")).click();
                System.out.println(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[aria-label='Product Added To Cart']"))).getText());
            }

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".fa-shopping-cart"))).click();

            String cartItemText = driver.findElement(By.cssSelector(".infoWrap")).getText();
            Assert.assertTrue(cartItemText.contains(orderItem));

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
            String orderId = driver.findElement(By.cssSelector("label[class='ng-star-inserted']")).getText().split(" ")[1];

            driver.findElement(By.xpath("//button[contains(text(),'ORDERS')]")).click();

            List<WebElement> finalOrderIds = driver.findElements(By.cssSelector("tbody th"));
            for (WebElement finalId: finalOrderIds) {
                if (finalId.getText().contains(orderId)) {
                    System.out.println("Verified order id ordered");
                }
            }
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
