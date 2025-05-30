package rahulshettyacademy.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import rahulshettyacademy.abstractComponents.AbstractComponent;

import java.util.List;

public class ProductCatalog extends AbstractComponent {

    WebDriver driver;

    public ProductCatalog(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //PageFactory
    @FindBy(className = "card-body")
    List<WebElement> itemCards;

    @FindBy(css = ".ngx-spinner-overlay")
    WebElement spinner;

    // PageFactory is available only for driver elements.
    // so need to use By for elements referencing to parent element
    By addToCart = By.xpath(".//button[contains(text(),'Add To Cart')]");

    By toastMessage = By.cssSelector("#toast-container");

    public List<WebElement> getProductList() {
        return itemCards;

    }

    public WebElement extractProductFromList(String productName) {
        return getProductList().stream()
                        .filter(s->s.getText().contains(productName))
                        .findFirst().orElse(null);
    }

    public void addToCart(String productName) {
        WebElement item = extractProductFromList(productName);
        item.findElement(addToCart).click();
        waitForElementToAppear(toastMessage);
        waitForElementToDisappear(toastMessage);
    }

}
