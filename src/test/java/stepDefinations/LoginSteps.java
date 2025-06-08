package stepDefinations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import base.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilities.ExcelDataReader;

public class LoginSteps {
	LoginPage lp;
	HomePage hp;
	MyAccountPage map;
	WebDriver driver;
	Logger logger;

	public LoginSteps() {
		this.driver = BaseTest.getDriver();
		this.logger = BaseTest.getLogger();
	}

	@Given("the user navigates to login page")
	public void the_user_navigates_to_login_page() throws IOException {
		logger.info("Navigating to login page");
		hp = new HomePage(driver);
		hp.clickMyAccount();
		hp.clickLogin();
	}

	@When("user enters email as {string} and password as {string}")
	public void user_enters_email_as_and_password_as(String email, String password) throws IOException {
		lp = new LoginPage(driver);
		logger.info("Entering email");
		lp.setEmail(email);
		logger.info("Entering password");
		lp.setPassword(password);
	}

	@When("the user clicks on login button")
	public void the_user_clicks_on_login_button() throws IOException {
		logger.info("Clicking login button");
		lp = new LoginPage(driver);
		lp.clickLogin();
	}

	@Then("the user should redirect to my account page")
	public void the_user_should_redirect_to_my_account_page() throws IOException {
		map = new MyAccountPage(driver);
		boolean targetPage = map.isMyAccountPageExists();
		Assert.assertTrue(targetPage);

	}

	/* ------Data driven using excel------- */
	@Then("the user should be redirected to MyAccount Page by passing email and password with excel row {string}")
	public void the_user_should_be_redirected_to_my_account_page_by_passing_email_and_password_with_excel_row(
			String row) {
		List<HashMap<String, String>> datamap = ExcelDataReader
				.data(System.getProperty("user.dir") + "\\testData\\LoginData.xlsx", "Sheet1");
		int index = Integer.parseInt(row) - 1;
		String email = datamap.get(index).get("username");
		String password = datamap.get(index).get("password");
		String expRes = datamap.get(index).get("result");
		lp = new LoginPage(driver);
		logger.info("Entering email");
		lp.setEmail(email);
		logger.info("Entering password");
		lp.setPassword(password);
		logger.info("Clicking login button");
		lp.clickLogin();

		map = new MyAccountPage(driver);
		try {
			boolean targetPage = map.isMyAccountPageExists();
			System.out.println("Target page: " + targetPage);
			if (expRes.equalsIgnoreCase("valid")) {
				if (targetPage == true) {
					hp.clickMyAccount();
					map.clickLogoutLink();
					Assert.assertTrue(true);
				} else {
					Assert.assertTrue(false);
				}
			} else if (expRes.equalsIgnoreCase("invalid")) {
				if (targetPage == true) {
					Assert.assertTrue(false);
					hp.clickMyAccount();
					map.clickLogoutLink();
				} else {
					Assert.assertTrue(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* ------Data driven------- */
	@When("the user enters {string} and {string}")
	public void the_user_enters_and(String user, String pwd) {
		lp = new LoginPage(driver);
		logger.info("Entering email");
		lp.setEmail(user);
		logger.info("Entering password");
		lp.setPassword(pwd);
	}

	@When("clicks login button")
	public void clicks_login_button() {
		logger.info("Clicking login button");
		lp = new LoginPage(driver);
		lp.clickLogin();
	}

	@Then("the user should be redirected to MyAccount Page")
	public void the_user_should_be_redirected_to_my_account_page_if_credentials_are() {
		map = new MyAccountPage(driver);
		boolean targetPage = map.isMyAccountPageExists();
		Assert.assertTrue(targetPage);
	}

}
