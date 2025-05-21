package base;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseTest {
	static WebDriver driver;
	static Logger logger;
	static Properties property;

	public static WebDriver getDriver() {
		return driver;
	}

	public static Logger getLogger() {
		logger = LogManager.getLogger();
		return logger;
	}

	public static Properties getProperties() throws IOException {
		FileReader file = new FileReader(".\\src\\test\\resources\\config.properties");
		property = new Properties();
		property.load(file);
		return property;
	}

	public static WebDriver initializeBrowser() throws IOException, URISyntaxException {
		property = getProperties();

		String os = property.getProperty("exeEnv").toLowerCase();
		String browserName = property.getProperty("browser1").toLowerCase();
		if (os.equalsIgnoreCase("remote")) {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// OS
			switch (os) {
			case "Windows":
				capabilities.setPlatform(Platform.WIN10);
				break;
			case "Linux":
				capabilities.setPlatform(Platform.LINUX);
				break;
			case "Mac":
				capabilities.setPlatform(Platform.MAC);
				break;

			default:
				System.out.println("No matching OS found");
				return null;
			}

			// Browser
			switch (browserName) {
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			default:
				System.out.println("No matching browser found");
				return null;

			}
			URI gridUri = new URI("https://localhost:4444/wd/hub");
			URL gridUrl = gridUri.toURL();
			driver = new RemoteWebDriver(gridUrl, capabilities);
		}

		else if (os.equalsIgnoreCase("local")) {
			// Browser
			switch (browserName) {
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			default:
				System.out.println("No matching browser found");
				return null;
			}
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		return driver;
	}

}
