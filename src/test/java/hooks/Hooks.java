package hooks;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import base.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
	WebDriver driver;
	Properties property;

	@Before
	public void setUp() throws IOException, InterruptedException, URISyntaxException {
		property = BaseTest.getProperties();
		driver = BaseTest.initializeBrowser();
		driver.get(property.getProperty("prodUrl"));
		Thread.sleep(5000);
		driver.manage().window().maximize();
	}

	@After
	public void tearDown(Scenario scenario) {
		driver.quit();
	}

	@AfterStep
	public void addScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {
			TakesScreenshot ts = (TakesScreenshot) driver;
			byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", scenario.getName());
		}
	}
}
