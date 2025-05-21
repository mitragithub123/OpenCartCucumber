package stepDefinations;

import java.io.IOException;

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
}
