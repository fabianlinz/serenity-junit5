package starter.webdriver;

import net.serenitybdd.junit5.AbstractSerenityTestDetectionWorkaround;
import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

@SerenityTest
class WebdriverTestScenario extends AbstractSerenityTestDetectionWorkaround {

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