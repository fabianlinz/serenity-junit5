package starter.webdriver;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class WebdriverTestScenario {

    @Managed
    WebDriver driver;

    @Steps
    WebdriverTestSteps steps;
        
    @Test
    public void happy_day_scenario() {
        steps.stepThatOpensWikipedia();
        steps.stepThatSucceeds();
    }
}