package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class VideoGamesPage {
    WebDriver driver;

    public VideoGamesPage(WebDriver driver) {
        this.driver = driver;
    }

    public void applyFilters() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//span[text()='Free Shipping']")));
        driver.findElement(By.xpath("//span[text()='New']")).click();
    }

    public void sortByHighToLow() {
        WebElement sortDropdown = driver.findElement(By.id("s-result-sort-select"));
        new Select(sortDropdown).selectByVisibleText("Price: High to Low");
    }

    public int addProductsBelowPrice(double maxPrice) {
        int added = 0;

        while (true) {
            List<WebElement> products = driver.findElements(By.xpath("//div[@data-component-type='s-search-result']"));

            for (int i = 0; i < products.size(); i++) {
                try {
                    WebElement product = products.get(i);

                    // Get product price
                    String priceWhole = product.findElement(By.cssSelector("span.a-price-whole")).getText().replace(",", "").trim();
                    String priceFraction = product.findElement(By.cssSelector("span.a-price-fraction")).getText().trim();
                    double price = Double.parseDouble(priceWhole + "." + priceFraction);
                    System.out.println("Product price: " + price);

                    if (price >= maxPrice) {
                        continue; // Skip products above max price
                    }

                    // Get product URL and open in new tab
                    WebElement titleLink = product.findElement(By.cssSelector("a.a-link-normal.s-no-outline"));
                    String productUrl = titleLink.getAttribute("href");
                    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0]);", productUrl);

                    // Switch to new tab
                    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(1));

                    try {
                        // Add to cart process
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
                        addToCartBtn.click();
                        Thread.sleep(1000);

                        // Handle 'No Thanks' button if present
                        try {
                            WebElement noThanksBtn = driver.findElement(By.id("attachSiNoCoverage"));
                            noThanksBtn.click();
                            System.out.println("'No Thanks' button clicked.");
                        } catch (NoSuchElementException e) {
                            System.out.println("'No Thanks' button not found, continuing...");
                        }

                        // Verify item was added to cart
                        try {
                            WebElement addedToCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//*[@id=\"NATC_SMART_WAGON_CONF_MSG_SUCCESS\"]/h1")));
                            Assert.assertEquals(addedToCartMessage.getText(), "Added to cart");
                        } catch (Exception e) {
                            System.out.println("Failed to verify cart addition: " + e.getMessage());
                            throw e;
                        }

                        added++; // Increment success counter
                        System.out.println("Successfully added product to cart");

                    } finally {
                        // Always close the tab and switch back to main window
                        driver.close();
                        driver.switchTo().window(tabs.get(0));
                        Thread.sleep(2000);
                    }

                } catch (Exception e) {
                    System.out.println("Skipping product due to error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            try {
                WebElement nextBtn = driver.findElement(By.cssSelector("a.s-pagination-next:not([aria-disabled='true'])"));
                nextBtn.click();
                Thread.sleep(4000);
            } catch (Exception e) {
                break; // No more pages
            }
        }

        return added;
    }

}
