package HotelResultPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HotelResultsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public HotelResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void sortByRating() {

        WebElement sortBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@data-testid='sorters-dropdown-trigger']")
                )
        );
        sortBtn.click();

        WebElement highRatingOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(text(), 'Property rating (high to low)')]")
                )
        );
        highRatingOption.click();
    }

    public void getTopHotels(int count) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@data-testid='title']")
                )
        );

        List<WebElement> hotelNames = driver.findElements(
                By.xpath("//div[@data-testid='title']")
        );

        List<WebElement> hotelPrices = driver.findElements(
                By.xpath("//span[@data-testid='price-and-discounted-price']")
        );

        System.out.println("\n--- Top Scraped Hotels ---");

        int limit = Math.min(count, Math.min(hotelNames.size(), hotelPrices.size()));

        for (int i = 0; i < limit; i++) {
            System.out.println(
                    "Hotel: " + hotelNames.get(i).getText()
                            + " | Price: " + hotelPrices.get(i).getText()
            );
        }
    }
}
