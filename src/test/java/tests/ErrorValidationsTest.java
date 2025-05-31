package tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;
import rahulshettyacademy.pageobjects.*;
import testcomponents.BaseTest;
import testcomponents.Retry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ErrorValidationsTest extends BaseTest {

    @Test
            (
                    groups = {"ErrorHandling"},
                    retryAnalyzer = Retry.class
            )
    public void loginErrorValidation() {
        landingPage.loginApplication("a@a.com", "wrongP");

        LandingPage landingPage = new LandingPage(driver);
        String errorMessage = landingPage.getErrorMessage();
        Assert.assertEquals(errorMessage, "Incorrect email oir password.");

    }

    @Test
            (groups = {"ErrorHandling"})
    public void productErrorValidation() {

        String orderItem = "ZARA COAT 3";
        String country = "India";

        ProductCatalog productCatalog = landingPage.loginApplication("thoufeeque.rafique@gmail.com", "GHcwBkaZZ$fcW6W");
        productCatalog.addToCart(orderItem);
        Checkout checkout = productCatalog.goToCart();

        Boolean match = checkout.VerifyCart(orderItem + "3");
        Assert.assertFalse(match);

    }

}