package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean verifyAllItemsInCart(int expectedCount) {
        int itemsCount = driver.findElements(By.cssSelector(".sc-list-item")).size();
        return itemsCount == expectedCount;
    }

    public double getTotalAmountFromCart() {
        String totalText = driver.findElement(By.id("sc-subtotal-amount-buybox")).getText(); // may vary
        return Double.parseDouble(totalText.replace("EGP", "").replace(",", "").trim());
    }

    public void proceedToCheckout() {
        driver.findElement(By.name("proceedToRetailCheckout")).click();
    }
}
