package starter.webdriver;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

@SerenityTest
class WebdriverTestScenario {

    @Managed
    WebDriver driver;

    @Steps
    WebdriverTestSteps steps;

    @Test
    void happy_day_scenario() {
        steps.stepThatOpensWikipedia();
        steps.stepThatSucceeds();
    }

    @Test
    void failure() {
        steps.stepThatOpensWikipedia();
        steps.stepThatFails();
    }

}