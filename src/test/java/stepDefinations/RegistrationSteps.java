package stepDefinations;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.github.javafaker.Faker;

import base.BaseTest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

public class RegistrationSteps {
	LoginPage lp;
	HomePage hp;
	AccountRegistrationPage arp;
	WebDriver driver;
	Logger logger;
	Faker faker;

	public RegistrationSteps() {
		this.driver = BaseTest.getDriver();
		this.logger = BaseTest.getLogger();
	}

	@Given("the user navigates to Register account page")
	public void the_user_navigates_to_register_account_page() {
		hp = new HomePage(driver);
		hp.clickMyAccount();
		hp.clickRegister();
	}

	@When("user enters the details into below fields")
	public void user_enters_the_details_into_below_fields(DataTable dataTable) {
		faker = new Faker();
		Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
		arp = new AccountRegistrationPage(driver);
		arp.setFirstName(dataMap.get("firstName"));
		arp.setLastName(dataMap.get("lastName"));
		arp.setEmail(faker.internet().emailAddress());
		arp.setTelephone(dataMap.get("telephone"));
		arp.setPassword(dataMap.get("password"));
		arp.setConfirmPassword(dataMap.get("password"));

	}

	@When("the user selects privacy policy")
	public void the_user_selects_privacy_policy() {
		arp = new AccountRegistrationPage(driver);
		arp.clickPrivacyPolicyCheckBox();
	}

	@When("the user clicks on continue button")
	public void the_user_clicks_on_continue_button() {
		arp = new AccountRegistrationPage(driver);
		arp.clickContinueBtn();
	}

	@Then("the user account should created successfully")
	public void the_user_account_should_created_successfully() {
		arp = new AccountRegistrationPage(driver);
		String confirmMsg = arp.getMsgConfirmation();
		Assert.assertEquals(confirmMsg, "Your Account Has Been Created!");
	}
}
