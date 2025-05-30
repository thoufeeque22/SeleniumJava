package rahulshettyacademy.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import rahulshettyacademy.abstractComponents.AbstractComponent;

import java.util.List;

public class PlaceOrder extends AbstractComponent {

    WebDriver driver;

    public PlaceOrder(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".item__title")
    WebElement placeOrderText;

    @FindBy(css = "[placeholder='Select Country']")
    WebElement countryField;

    @FindBy(css = ".list-group-item")
    List<WebElement> countryList;

    @FindBy(css = ".action__submit")
    WebElement placeOrder;

    public String verifyTitle(String productName) {
        return placeOrderText.getText();
    }

    public void submitOrder(String country) {
        countryField.sendKeys(country);
        WebElement listCountry = countryList.stream()
                .peek(name -> System.out.println("Original: " + name.getText()))        // peek for debugging purposes
                .filter(s->s.getText().equals(country))
                .peek(name -> System.out.println("Original: " + name.getText()))
                .findFirst()
                .orElse(null);
        assert listCountry != null;
        listCountry.click();
        placeOrder.click();
    }

}
