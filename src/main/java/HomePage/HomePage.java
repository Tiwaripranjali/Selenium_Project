package HomePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By dismissBtn = By.xpath("//button[contains(@aria-label, 'Dismiss')]");
    private By searchInput = By.name("ss");
    private By dateBox = By.xpath("//button[@data-testid='searchbox-dates-container']");
    private By submitBtn = By.xpath("//button[@type='submit']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void dismissPopup() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dismissBtn)).click();
        } catch (Exception e) {
            System.out.println("Sign-in popup did not appear.");
        }
    }

    public void searchForHotels(String city, String checkIn, String checkOut) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(city);

        driver.findElement(dateBox).click();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement in = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@aria-label, '"+checkIn+"')]")));
        js.executeScript("arguments[0].click();", in);

        WebElement out = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@aria-label, '"+checkOut+"')]")));
        js.executeScript("arguments[0].click();", out);

        driver.findElement(submitBtn).click();
    }
}