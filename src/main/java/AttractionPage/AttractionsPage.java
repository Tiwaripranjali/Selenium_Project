package AttractionPage;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AttractionsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public AttractionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Opens the Attractions tab from Booking.com home page
    public void openAttractionsTab() {

        WebElement attractionsTab = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("attractions"))
        );
        attractionsTab.click();
    }

    // Searches attractions for a destination and selects date
    public void findAttractions(String destinationName) throws InterruptedException {

        // Destination input
        WebElement destinationInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@data-testid='search-input-field']")
                )
        );
        destinationInput.clear();
        destinationInput.sendKeys("Nairobi");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        List<WebElement> results = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//button[@data-testid='search-bar-result']")
                )
        );
        for (int i = 0; i < results.size(); i++) {


            WebElement result = driver.findElements(
                    By.xpath("//button[@data-testid='search-bar-result']")
            ).get(i);

            String name = result.findElement(By.xpath(".//div[1]"))
                    .getText()
                    .trim();

            if (name.toLowerCase().contains("nairobi national park")) {

                wait.until(ExpectedConditions.elementToBeClickable(result));
                result.click();
                break;
            }
        }
        driver.findElement(By.xpath("//div[text()='Dates']")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//div[text()='Dates']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@aria-label,'Fr 15 May 2026')]")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@aria-label,'We 20 May 2026')]")
        )).click();

        WebElement submitBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@type='submit']")
                )
        );
        submitBtn.click();
        Thread.sleep(5000);
        List<WebElement> AttractionName=driver.findElements(By.xpath("//h3[@data-testid='card-title']"));
        List<WebElement> Attractionprice=driver.findElements(By.xpath("//div[@class='e7addce19e css-g82t4m']"));


        System.out.println("------------------------------------------");
        for (int i = 0; i < Attractionprice.size(); i++) {
            System.out.println(
                    AttractionName.get(i).getText()
                            + " | Price: "
                            + Attractionprice.get(i).getText()
            );
        }

    }
}
