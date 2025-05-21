package testRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		// features = {".\\Features\\Login.feature", ".\\Features\\Registration.feature"},
		// features = {".\\Features"},
		features = {".\\Features\\Login.feature"},
		//features = {".\\Features\\Registration.feature"},
		// features = {"@target/rerun.txt"},
		glue = {"stepDefinations", "hooks"},
		plugin = {
		        "pretty",                                          // Clean console output
		        "html:target/cucumber-html-report.html",           // Built-in HTML report
		        "json:target/cucumber.json",                       // JSON report for other integrations
		        "rerun:target/rerun.txt",                          // Stores failed scenarios
		        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", // Extent report (Inside test-output)
		        "com.aventstack.chaintest.plugins.ChainTestCucumberListener:"  // Chain test report (Inside target)
		    },
		dryRun = false,      // Set to true to check mapping between steps and step definitions without executing tests
		monochrome = true,   // Removes unnecessary characters from console output for readability
		publish = true    // Publishes test results on Cucumber Reports (https://reports.cucumber.io/)
		// tags = "@sanity"  // Run all scenarios with @sanity tag
		// tags = "@sanity or @regression",  // Run all scenarios with either @sanity or @regression tag
		// tags = "@sanity and @regression",  // Run all scenarios with both @sanity and @regression tag
		// tags = "@sanity and not @regression",  // Run all scenarios with @sanity but not with @regression tag
		)
public class Run {

}
