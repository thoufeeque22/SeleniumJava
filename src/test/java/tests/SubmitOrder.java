package tests;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import rahulshettyacademy.pageobjects.*;
import testcomponents.BaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class SubmitOrder extends BaseTest{

        @Test
        public void submitOrder() throws IOException {

        TakesScreenshot ss = (TakesScreenshot) driver;
        try {
            String orderItem = "ZARA COAT 3";
            String country = "India";

            ProductCatalog productCatalog = landingPage.loginApplication("a@gmail.com", "easypw");
            productCatalog.addToCart(orderItem);
            Checkout checkout = productCatalog.goToCart();

            Boolean match = checkout.VerifyCart(orderItem);
            Assert.assertTrue(match);
            PlaceOrder placeOrder = checkout.checkoutCart();

            String placeOrderText = placeOrder.verifyTitle(orderItem);
            Assert.assertTrue(placeOrderText.contains(orderItem));
            VerifyOrder verifyOrder = placeOrder.submitOrder(country);

            String confirmMessage = verifyOrder.verifyOrderConfirmation();
            Assert.assertEquals(confirmMessage, "THANKYOU FOR THE ORDER.");
            String orderId = verifyOrder.captureOrderId();
            verifyOrder.goToOrders();
            String finalId = verifyOrder.verifyOrderId(orderId);
            Assert.assertEquals(orderId, finalId);

        } catch (Exception e) {
            e.printStackTrace();
            File src = ss.getScreenshotAs(OutputType.FILE);
            File dest = new File("screenshots/error_screenshot.png");
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
