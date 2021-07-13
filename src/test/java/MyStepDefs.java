import hooks.BasicHooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class MyStepDefs {

    private static final String URL = "https://market.yandex.ru/";
    private static final Logger logger = LogManager.getLogger(MyStepDefs.class);
    private final WebDriverWait wait;
    private final Actions actions;
    private final WebDriver driver;

    public MyStepDefs() {
        this.driver = BasicHooks.driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, 10);

    }

    @Given("Open Yandex Market and go to smartphones")
    public void openYandexMarketAndGoToSmartphones() {
        logger.info("Test started");
        driver.get(URL);
        WebElement buttonElektronika = driver.findElement(By.xpath("//span[contains(text(), 'Электроника')]"));
        buttonElektronika.click();
        WebElement buttonSmartfony = driver.findElement(By.xpath("//a[contains(@href, '/catalog--smartfony/')]"));
        buttonSmartfony.click();
        logger.info("Url is opened");
    }

    @When("Filter by brand {string} and {string} and sort by price ascending")
    public void filterByBrandAndAndSortByPriceAscending(String brand1, String brand2) {
        List<String> brandNames = new ArrayList<>();
        brandNames.add(brand1);
        brandNames.add(brand2);
        for (String brandName : brandNames) {
            WebElement brandCheckbox = driver.findElement(By.xpath("//input[@name='Производитель " + brandName + "']/following-sibling::div"));
            actions.moveToElement(brandCheckbox).perform();
            brandCheckbox.click();
        }
        By loaderLocator = By.xpath("//div[@data-tid='8bc8e36b']");
        waitForElementToBeGone(loaderLocator);
        WebElement sortPrice = driver.findElement(By.xpath("//button[@data-autotest-id='dprice']"));
        sortPrice.click();
        waitForElementToBeGone(loaderLocator);
        logger.info("Phones are sorted by brand and price");
    }

    @And("Add first phone of brand {string} and {string} to compare list")
    public void addFirstPhoneOfBrandAndToCompareList(String brand1, String brand2) {
        List<String> brandNames = new ArrayList<>();
        brandNames.add(brand1);
        brandNames.add(brand2);
        for (String brandName : brandNames) {
            WebElement firstPhone = driver.findElement(By.xpath("(//a[contains(@title, '" + brandName + "')] )[1]"));
            actions.moveToElement(firstPhone).perform();
            String phoneName = driver.findElement(By.xpath("(//a[contains(@title, '" + brandName + "')] )[1]")).getAttribute("title");
            WebElement addPhone = driver.findElement(By.xpath("(//a[contains(@title, '" + brandName + "')] )[1]/parent::h3/parent::div/parent::div/preceding-sibling::div[2]/div[2]/div"));
            addPhone.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Товар " + phoneName + " добавлен к сравнению')]")));
            logger.info("First {} is added", brandName);
        }
    }

    @Then("Verify compare list has added positions")
    public void verifyCompareListHasAddedPositions() {
        driver.get(URL + "compare");
        String compareListXpath = "//div[@data-tid='412661c']";
        List<WebElement> compareList = driver.findElements(By.xpath(compareListXpath));
        Assert.assertEquals(compareList.size(), 2, "Number of options in compare list is not as expected");
        logger.info("Number of phones in compare list is {}", compareList.size());
    }

    private boolean isElementDisplayed(By elementSelector) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, 1);
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(elementSelector));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForElementToBeGone(By elementSelector) {
        if (isElementDisplayed(elementSelector)) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(elementSelector));
        }
    }
}
