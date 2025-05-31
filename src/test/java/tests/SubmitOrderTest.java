package tests;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rahulshettyacademy.pageobjects.*;
import testcomponents.BaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;


public class SubmitOrderTest extends BaseTest{

    String orderItem = "ZARA COAT 3";

    @Test
            (
                    dataProvider = "getData",
                    groups = {"purchaseOrder"}
            )
    public void submitOrder(HashMap<String, String> input) {

        try {

            ProductCatalog productCatalog = landingPage.loginApplication(input.get("email"), input.get("password"));
            productCatalog.addToCart(input.get("orderItem"));
            Checkout checkout = productCatalog.goToCart();

            Boolean match = checkout.VerifyCart(input.get("orderItem"));
            Assert.assertTrue(match);
            PlaceOrder placeOrder = checkout.checkoutCart();

            String placeOrderText = placeOrder.verifyTitle(input.get("orderItem"));
            Assert.assertTrue(placeOrderText.contains(input.get("orderItem")));
            VerifyOrder verifyOrder = placeOrder.submitOrder(input.get("country"));

            String confirmMessage = verifyOrder.verifyOrderConfirmation();
            Assert.assertEquals(confirmMessage, "THANKYOU FOR THE ORDER.");
            String orderId = verifyOrder.captureOrderId();
            verifyOrder.goToOrders();
            String finalId = verifyOrder.verifyOrderId(orderId);
            Assert.assertEquals(orderId, finalId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(dependsOnMethods = {"submitOrder"})
    public void orderHistoryTest() throws IOException {

        try {

            ProductCatalog productCatalog = landingPage.loginApplication("a@gmail.com", "easypw");
            VerifyOrder verifyOrder = productCatalog.goToOrders();
            Boolean match = verifyOrder.verifyOrderDisplay(orderItem);
            Assert.assertTrue(match);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DataProvider
    public Object[][] getData() throws IOException {

        List<HashMap<String, String>> data = getJsonDataToMap(System.getProperty("user.dir") +  "/src/test/java/data/PurchaseOrder.json");
        return new Object[][] {
                {data.get(0)},
                {data.get(1)}
        };
    }

}
