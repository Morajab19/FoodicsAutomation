package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void openMenu() {
        driver.findElement(By.id("nav-hamburger-menu")).click();
    }

    public void goToVideoGames() throws InterruptedException {
        driver.findElement(By.xpath("//div[text()='See all']")).click();
        WebElement shopByCategoryDiv = driver.findElement(By.xpath("//div[contains(text(), 'Shop by Category')]"));
        driver.findElement(By.xpath("//div[text()='Video Games']")).click();
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", driver.findElement(By.linkText("All Video Games")));
    }
}
