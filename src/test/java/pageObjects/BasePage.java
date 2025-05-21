package pageObjects;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.SelectDropdownEnum;

public class BasePage {
	WebDriver driver;
	public Actions actions;
	public WebDriverWait wait;
	public Alert alert;

	BasePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// ===================== Click actions =====================
	// Normal click
	public void clickElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			applyBorder(element, "green");
			element.click();
		} catch (Exception e) {
			applyBorder(element, "red");
			;
			Assert.fail("Test failed due to exception: " + e.getMessage());
		}
	}

	// Click using JavascriptExecutor
	public void clickElementUsingJavascriptExecutor(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			applyBorder(element, "green");
			element.click();
			System.out.println("Element clicked successfully using normal click.");
		} catch (Exception e) {
			System.out.println("Normal click failed, trying JavaScript click. Error: " + e.getMessage());
			applyBorder(element, "red");
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
				System.out.println("Element clicked using JavaScriptExecutor.");
			} catch (Exception jsException) {
				Assert.fail("Both normal and JavaScript clicks failed. Error: " + jsException.getMessage());
			}
		}
	}

	// ===================== Text Input Methods =====================
	public void enterText(WebElement element, String text) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(element));
			applyBorder(element, "green");
			element.sendKeys(text);
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail(e.getMessage());
		}
	}

	public void clearText(WebElement element) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(element));
			applyBorder(element, "green");
			element.clear();
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail(e.getMessage());
		}
	}

	public void enterTextAfterClear(WebElement element, String text) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			applyBorder(element, "green");
			element.sendKeys(text);
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail(e.getMessage());

		}
	}

	public void enterTextUsingJavascriptExecutor(WebElement element, String text) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(element));
			applyBorder(element, "green");

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value='" + text + "';", element);

			System.out.println("Text entered successfully using JavaScriptExecutor.");
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail("Text entry failed using JavaScriptExecutor. Error: " + e.getMessage());
		}
	}

	public void enterTextAfterClearUsingJavascriptExecutor(WebElement element, String text) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(element));

			JavascriptExecutor js = (JavascriptExecutor) driver;

			js.executeScript("arguments[0].value='';", element);
			applyBorder(element, "green");

			js.executeScript("arguments[0].value=arguments[1];", element, text);

			System.out.println("Text cleared and entered successfully using JavaScriptExecutor.");
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail("Text entry failed using JavaScriptExecutor. Error: " + e.getMessage());
		}
	}

	// ===================== Hover and Interaction =====================
	public void hoverOverElement(WebElement element) {
		try {
			actions = new Actions(driver);
			actions.moveToElement(element).build().perform();
			applyBorder(element, "blue");
			wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public void clickAndHoldAndMoveElement(WebElement src, WebElement dst) {
		try {
			actions = new Actions(driver);
			applyBorder(src, "purple");
			Thread.sleep(500);
			actions.clickAndHold(src).moveToElement(dst).release().build().perform();
			applyBorder(src, "cyan");
			Thread.sleep(500);
			wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		} catch (Exception e) {

			Assert.fail("Failed to drag the task: " + e.getMessage());
		}
	}

	// ===================== Alert Handling =====================
	public void acceptAlert() {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.alertIsPresent());
			alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			Assert.fail("No alert present when trying to accept." + e.getMessage());
		}
	}

	public void dismissAlert() {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.alertIsPresent());
			alert = driver.switchTo().alert();
			alert.dismiss();
		} catch (Exception e) {
			Assert.fail("No alert present when trying to dismiss." + e.getMessage());
		}
	}

	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			throw new AssertionError("Failed to get text from alert: " + e.getMessage());
		}
	}

	// ===================== Navigation & Verification =====================
	public void navigateAndVerify(WebElement elementLocator, String expectedUrl) {
		clickElement(elementLocator);
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(expectedUrl, currentUrl, "URL mismatch after navigation");
	}

	// ===================== File upload =====================
	public static void uploadFile(String filePath) throws AWTException, InterruptedException {
		try {
			StringSelection ss = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

			Robot robot = new Robot();
			robot.delay(1000);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);

			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_V);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			Thread.sleep(2000);
		} catch (HeadlessException e) {
			Assert.fail("HeadlessException: Robot class cannot be used in a headless environment: " + e.getMessage());
		} catch (AWTException e) {
			Assert.fail("Error initializing Robot class: " + e.getMessage());
		} catch (InterruptedException e) {
			Assert.fail("Unexpected error during file upload: : " + e.getMessage());
		}
	}

	// ===================== Random data generation =====================
	public static String randomString(int length) {
		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
		return generator.generate(length);
	}

	public static String randomNumber(int length) {
		StringBuilder number = new StringBuilder();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < length; i++) {
			number.append(random.nextInt(0, 10)); // digits 0–9
		}
		return number.toString();
	}

	public static String randomAlphaNumeric(int alphaLength, int numericLength) {
		return randomString(alphaLength) + "@" + randomNumber(numericLength);
	}

	public static String randomEmail() {
		String local = randomString(5).toLowerCase();
		String number = randomNumber(3);
		return local + number + "@yahoo.in";
	}

	// ===================== Element styling & visibility =====================
	// Method to apply a border to a WebElement using JavaScriptExecutor
	public void applyBorder(WebElement element, String color) {
		try {
			String script = "arguments[0].style.border='2px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	public String getText(WebElement element) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(element));
			applyBorder(element, "green");
			String text = element.getText();
			return text;
		} catch (Exception e) {
			applyBorder(element, "red");
			throw new AssertionError("Failed to get text from element: " + e.getMessage());

		}
	}

	public boolean isElementVisible(WebElement element) {
		try {
			applyBorder(element, "green");
			return element.isDisplayed();
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail("Element not visible: " + e.getMessage());
			return false; // This line will never execute, but it's required for compilation
		}
	}

	// ===================== Frame handling =====================
	// Method to switch to an iframe
	public void switchToFrame(WebElement element) {
		try {
			driver.switchTo().frame(element);
			System.out.println("Switched to the frame successfully.");
		} catch (Exception e) {
			Assert.fail("Failed to switch to frame due to an unexpected error: " + e.getMessage());
		}
	}

	// Method to switch back to the default content
	public void switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
			System.out.println("Switched to default content successfully.");
		} catch (Exception e) {
			Assert.fail("No iframe to switch back from: : " + e.getMessage());
		}
	}

	// ===================== Scrolling =====================
	// Method to scroll to the bottom of the page
	public void scrollToBottom() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			System.out.println("Scrolled to the bottom of the page.");
		} catch (Exception e) {
			Assert.fail("Failed to scroll to the bottom: " + e.getMessage());
		}
	}

	// Method to scroll to the top of the page
	public void scrollToTop() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, 0);");
			System.out.println("Scrolled to the top of the page.");
		} catch (Exception e) {
			Assert.fail("Failed to scroll to the top: " + e.getMessage());
		}
	}

	// Method to scroll to the extreme right of the page
	public void scrollToExtremeRight() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(document.body.scrollWidth, 0)");
			System.out.println("Successfully scrolled to the extreme right of the page.");
		} catch (Exception e) {
			Assert.fail("Failed to scroll to the extreme right: " + e.getMessage());
		}
	}

	// Scrolls horizontally to a specific element on the extreme right
	public void scrollToElementRight(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({inline: 'end'});", element);
			System.out.println("Successfully scrolled to the rightmost element: " + element);
			applyBorder(element, "green");
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail("Failed to scroll to the rightmost element: " + e.getMessage());
		}
	}

	// Method to scroll to an element vertically
	public void scrollToElementVertically(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
			System.out.println("Successfully scrolled to the element: " + element);
			applyBorder(element, "green");
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail("Failed to scroll to the element: " + e.getMessage());
		}
	}

	// Smooth scroll method (10px at a time)
	public void smoothVerticalScrollToEnd() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			long lastScrollHeight = (long) js.executeScript("return document.body.scrollHeight");
			int stagnantCount = 0;

			while (true) {
				js.executeScript("window.scrollBy(0, 10)");
				Thread.sleep(50);
				long newScrollHeight = (long) js.executeScript("return document.body.scrollHeight");
				long newScrollY = (long) js.executeScript("return window.scrollY");
				if (newScrollY + 10 >= lastScrollHeight) {
					break;
				}
				if (newScrollHeight == lastScrollHeight) {
					stagnantCount++;
					if (stagnantCount >= 5) {
						break;
					}
				} else {
					stagnantCount = 0;
				}
				lastScrollHeight = newScrollHeight;
			}
		} catch (Exception e) {
			Assert.fail("Error while scrolling: " + e.getMessage());
		}
	}

	// ===================== Select dropdown =====================
	// Method to handle select dropdown
	public void selectDropdown(WebElement element, SelectDropdownEnum visibletext, String value) {
		try {
			Select select = new Select(element);
			applyBorder(element, "green");
			switch (visibletext) {
			case INDEX:
				select.selectByIndex(Integer.parseInt(value));
				break;
			case VALUE:
				select.selectByValue(value);
				break;
			case VISIBLETEXT:
				select.selectByVisibleText(value);
				break;
			default:
				applyBorder(element, "red");
				Assert.fail("Please pass the correct selection criteria");
			}
		} catch (NumberFormatException e) {
			Assert.fail("Invalid index format: " + value);
		} catch (NoSuchElementException e) {
			Assert.fail("Dropdown element not found: " + e.getMessage());
		} catch (Exception e) {
			Assert.fail("Error selecting dropdown option: " + e.getMessage());
		}
	}

	// Method to get all options from a select dropdown
	public List<String> getAllDropdownOptions(WebElement element) {
		List<String> optionsList = new ArrayList<>();
		try {
			Select select = new Select(element);
			List<WebElement> options = select.getOptions();
			for (WebElement option : options) {
				optionsList.add(option.getText());
			}
			applyBorder(element, "green");
		} catch (NoSuchElementException e) {
			applyBorder(element, "red");
			Assert.fail("Dropdown element not found: " + e.getMessage());
		} catch (Exception e) {
			applyBorder(element, "red");
			Assert.fail("Error fetching dropdown options: " + e.getMessage());
		}
		return optionsList;
	}

	// ===================== Switching windows =====================
	// Method to switch between browser windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					System.out.println("Switched to window: " + windowTitle);
					return;
				}
			}
			System.err.println("Window with title " + windowTitle + " not found.");
		} catch (Exception e) {
			Assert.fail("Unable to switch window: " + e.getMessage());
		}
	}

	// ======== Element Identification & Description =========
	// Method to get the description of an element using WebElement
	// Use this method before clicking the element, not after.
	// If directly you want to use its ok.
	public String getElementDescription(WebElement element) {
		try {
			if (element == null) {
				System.err.println("Element is null.");
				return "Element is null.";
			}

			applyBorder(element, "orange");

			String name = element.getDomProperty("name");
			String id = element.getDomProperty("id");
			String text = element.getText();
			String className = element.getDomProperty("class");
			String placeholder = element.getDomProperty("placeholder");

			String description;
			if (isNotEmpty(name)) {
				description = "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				description = "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				description = "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				description = "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				description = "Element with placeholder: " + placeholder;
			} else {
				description = "Element without identifiable attributes.";
			}

			System.out.println("Element Description: " + description);
			return description;

		} catch (Exception e) {
			Assert.fail("Unable to describe element due to error: " + e.getMessage());
			return "Unable to describe element due to error: " + e.getMessage();
		}
	}

	// Utility method to check if a string is not null or empty
	public boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long strings
	// e.g, if a string is too long, cut it off after a certain number of characters
	// and append "..."
	public String truncate(String text, int maxLength) {
		if (text == null || text.length() <= maxLength) {
			return text;
		}
		return text.substring(0, maxLength) + "...";
	}

	// ===================== Datepicker utility (Type-1) =====================
	// ===================== Year and month are in same line =====================
	public void selectMonthAndYear(WebElement monthYearElement, WebElement nextButton, WebElement prevButton,
			String requiredMonth, String requiredYear, boolean flag) {
		try {
			while (true) {
				String monthAndYear = getText(monthYearElement);
				String extractedMonth = monthAndYear.substring(0, monthAndYear.indexOf(" "));
				String extractedYear = monthAndYear.substring(monthAndYear.indexOf(" ") + 1);

				int currentYear = Integer.parseInt(extractedYear);
				int targetYear = Integer.parseInt(requiredYear);

				if (extractedMonth.equalsIgnoreCase(requiredMonth) && extractedYear.equals(requiredYear)) {
					break;
				} else if (targetYear > currentYear
						|| (targetYear == currentYear && isFutureMonth(extractedMonth, requiredMonth))) {
					clickElement(nextButton); // Move forward
				} else {
					clickElement(prevButton); // Move backward
				}
			}
		} catch (Exception e) {
			Assert.fail("Error selecting month and year: " + e.getMessage());
		}
	}

	// Helper method to determine if the required month is in the future
	public boolean isFutureMonth(String currentMonth, String targetMonth) {
		try {
			List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July",
					"August", "September", "October", "November", "December");
			return months.indexOf(targetMonth) > months.indexOf(currentMonth);
		} catch (Exception e) {
			Assert.fail("Error determining future month: " + e.getMessage());
			return false;
		}
	}

	// Select date
	public void selectDate(List<WebElement> dateElements, String requiredDate) {
		try {
			for (WebElement dateElement : dateElements) {
				if (getText(dateElement).equals(requiredDate)) {
					clickElement(dateElement);
					break;
				}
			}
		} catch (Exception e) {
			Assert.fail("Error selecting date: " + e.getMessage());
		}
	}
}
