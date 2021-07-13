package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class BasicHooks {

    public static WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BasicHooks.class);

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(Browser.CHROME);
        logger.info("Driver is initialised");
        if (driver !=null) {
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            driver.manage().window().maximize();
        }
    }

    @After
    public void shutDownDriver() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser is closed");
        }
    }
}


