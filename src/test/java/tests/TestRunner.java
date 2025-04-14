package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utils.ConfigReader;
import utils.DriverFactory;

import java.util.Properties;

public class TestRunner {
    WebDriver driver;
    Properties prop;

    @BeforeMethod
    public void setUp() {
        prop = ConfigReader.initProp();
        driver = DriverFactory.getDriver();
        driver.get(prop.getProperty("baseUrl"));
    }

    @Test
    public void fullFlowTest() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

        HomePage homePage = new HomePage(driver);
        homePage.openMenu();
        homePage.goToVideoGames();

        VideoGamesPage videoGames = new VideoGamesPage(driver);
        videoGames.applyFilters();
        videoGames.sortByHighToLow();
        int addedCount = videoGames.addProductsBelowPrice(15000.0);

        CartPage cartPage = new CartPage(driver);
        driver.get("https://www.amazon.eg/gp/cart/view.html");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(cartPage.verifyAllItemsInCart(addedCount), "Cart count mismatch!");

        double totalCartAmount = cartPage.getTotalAmountFromCart();
        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.chooseCashOnDelivery();

        double checkoutAmount = checkoutPage.getFinalTotal();

        Assert.assertEquals(checkoutAmount, totalCartAmount, "Total mismatch between cart and checkout!");

        System.out.println("Test finished: All products added and total verified.");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
