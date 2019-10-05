package starter.webdriver;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import static org.assertj.core.api.Assertions.assertThat;

public class WebdriverTestSteps extends ScenarioSteps {

    WikipediaPage page;

    @Step
    public void stepThatSucceeds() {
        assertThat(page.getTitle()).isEqualTo("Wikipedia");
    }

    @Step
    public void stepThatOpensWikipedia() {
        page.open();
    }

}
