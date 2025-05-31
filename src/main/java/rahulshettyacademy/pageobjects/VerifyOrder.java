package rahulshettyacademy.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import rahulshettyacademy.abstractComponents.AbstractComponent;

import java.util.List;

public class VerifyOrder extends AbstractComponent {

    WebDriver driver;

    public VerifyOrder(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".hero-primary")
    WebElement confirmMessage;

    @FindBy(css = "label[class='ng-star-inserted']")
    WebElement orderId;

    @FindBy(css = "tbody th")
    List<WebElement> finalOrderIds;

    @FindBy(css = "tr td:nth-child(3)")
    List<WebElement> productNames;

    public String verifyOrderConfirmation() {
        return confirmMessage.getText();
    }

    public String captureOrderId() {
        return orderId.getText().split(" ")[1];
    }

    public String verifyOrderId(String orderId) {
        return finalOrderIds.stream().filter(s->s.getText().equals(orderId)).findFirst().orElse(null).getText();
    }

    public Boolean verifyOrderDisplay(String productName) {
        return productNames.stream().anyMatch(product->product.getText().equalsIgnoreCase(productName));
    }

}
