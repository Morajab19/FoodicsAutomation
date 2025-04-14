package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
    WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

//        This step won't be applicable unless it is first time
//    public void enterNewAddress() {
////        driver.findElement(By.id("address-ui-widgets-enterAddressFullName")).sendKeys("Mohammed Rajab");
////        driver.findElement(By.id("address-ui-widgets-enterAddressPhoneNumber")).sendKeys("01559950214");
////        driver.findElement(By.id("address-ui-widgets-enterAddressLine1")).sendKeys("Street 123");
////        driver.findElement(By.id("address-ui-widgets-enterAddressCity")).sendKeys("Cairo");
//
////        driver.findElement(By.cssSelector(".a-button-input")).click();
//    }

    public void chooseCashOnDelivery() {
        driver.findElement(By.id("pp-klPfUp-125")).click();
    }

    public double getFinalTotal() {
        String totalText = driver.findElement(By.id("subtotals-marketplace-table")).getText();
        return Double.parseDouble(totalText.replace("EGP", "").replace(",", "").trim());
    }
}
