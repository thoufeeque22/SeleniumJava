package rahulshettyacademy.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import rahulshettyacademy.abstractComponents.AbstractComponent;

import java.util.List;

public class Checkout extends AbstractComponent {

    WebDriver driver;

    public Checkout(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".infoWrap h3")
    List<WebElement> cartItemTitles;

    @FindBy(css = ".subtotal button")
    WebElement checkoutButton;

    public List<WebElement> getCartItemsList() {
        return cartItemTitles;
    }

    public Boolean VerifyCart(String productName) {
        return getCartItemsList().stream().anyMatch(s->s.getText().equals(productName));
    }

    public void checkoutCart() {
        checkoutButton.click();
    }
}
