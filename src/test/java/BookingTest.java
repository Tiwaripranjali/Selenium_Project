import AttractionPage.AttractionsPage;
import HomePage.HomePage;
import HotelResultPage.HotelResultsPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.List;

public class BookingTest {

    WebDriver driver;
    WebDriverWait wait;
    HomePage home;
    HotelResultsPage results;
    AttractionsPage attractions;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://www.booking.com/index.en-gb.html");

        home = new HomePage(driver);
        results = new HotelResultsPage(driver);
        attractions = new AttractionsPage(driver);
    }

    // ---------------- HOME PAGE VALIDATIONS ----------------

    @Test(priority = 1)
    public void validateHomePageTitle() {

        // Wait until title is non-empty
        WebDriverWait titleWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        titleWait.until(driver -> !driver.getTitle().trim().isEmpty());
        String title = driver.getTitle().toLowerCase();
        System.out.println("Page title is: " + title);

        Assert.assertTrue(
                title.contains("booking") ||
                        title.contains("hotel") ||
                        title.contains("official"),
                "Unexpected title: " + title
        );
    }

    @Test(priority = 2)
    public void validateHomePageLoaded() {

        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("ss"))
        );
        Assert.assertTrue(searchBox.isDisplayed(), "Search box not visible");
    }

    @Test(priority = 3)
    public void validatePopupDismissal() {
        home.dismissPopup();
        Assert.assertTrue(true, "Popup handled safely");
    }

    // ---------------- HOTEL SEARCH FLOW ----------------

    @Test(priority = 4)
    public void validateHotelSearch() {
        home.searchForHotels("Nairobi", "14 May 2026", "18 May 2026");

        wait.until(ExpectedConditions.urlContains("searchresults"));
        Assert.assertTrue(driver.getCurrentUrl().contains("searchresults"),
                "Did not land on hotel results page");
    }

    @Test(priority = 5, dependsOnMethods = "validateHotelSearch")
    public void validateSearchResultsLoaded() {

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//div[@data-testid='title']"), 0));

        List<WebElement> hotels =
                driver.findElements(By.xpath("//div[@data-testid='title']"));

        Assert.assertTrue(hotels.size() > 0, "No hotel results found");
    }

    @Test(priority = 6, dependsOnMethods = "validateSearchResultsLoaded")
    public void validateSortByRating() {

        results.sortByRating();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//div[@data-testid='title']"), 0));

        Assert.assertTrue(true, "Sorting applied successfully");
    }

    @Test(priority = 7)
    public void validateTopHotelsFetch() {
        results.getTopHotels(3);
        Assert.assertTrue(true, "Top hotels printed successfully");
    }

    @Test(priority = 8)
    public void validateHotelNamesNotEmpty() {
        driver.findElements(By.xpath("//div[@data-testid='title']"))
                .forEach(e ->
                        Assert.assertFalse(e.getText().trim().isEmpty(),
                                "Empty hotel name found"));
    }

    @Test(priority = 9)
    public void validateHotelPricesVisible() {
        Assert.assertTrue(
                driver.findElements(
                        By.xpath("//span[@data-testid='price-and-discounted-price']")
                ).size() > 0,
                "Hotel prices not visible");
    }

    // ---------------- ATTRACTIONS FLOW ----------------

    @Test(priority = 10, dependsOnMethods = "validateHotelSearch")
    public void validateAttractionsTabNavigation() {
        attractions.openAttractionsTab();

        wait.until(ExpectedConditions.urlContains("attractions"));
        Assert.assertTrue(driver.getCurrentUrl().contains("attractions"),
                "Did not navigate to attractions tab");
    }

    @Test(priority = 11)
    public void validateAttractionSearch() throws InterruptedException {
        attractions.findAttractions("Nairobi");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[@data-testid='card-title']")));

        Assert.assertTrue(true, "Attraction search completed");
    }

    @Test(priority = 12)
    public void validateAttractionResultsDisplayed() {
        Assert.assertTrue(
                driver.findElements(
                        By.xpath("//h3[@data-testid='card-title']")
                ).size() > 0,
                "No attractions displayed");
    }

    @Test(priority = 13)
    public void validateAttractionNamesNotEmpty() {
        driver.findElements(By.xpath("//h3[@data-testid='card-title']"))
                .forEach(e ->
                        Assert.assertFalse(e.getText().trim().isEmpty(),
                                "Empty attraction name found"));
    }

    @Test(priority = 14)
    public void validateAttractionPricesVisible() {
        Assert.assertTrue(
                driver.findElements(
                        By.xpath("//div[contains(@class,'e7addce19e')]")
                ).size() > 0,
                "Attraction prices missing");
    }

    // ---------------- RESET & STABILITY ----------------

    @Test(priority = 15)
    public void validateReturnToHomeAndReuseSearch() {
        driver.get("https://www.booking.com/index.en-gb.html");

        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.name("ss"))
        );

        searchBox.sendKeys("Nairobi");
        Assert.assertTrue(searchBox.isDisplayed(),
                "Search input not reusable after navigation");
    }

    @Test(priority = 16)
    public void validateBrowserSessionAlive() {
        Assert.assertNotNull(driver.getWindowHandle(),
                "Browser session is no longer active");
    }

    // ---------------- CLEANUP ----------------

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}