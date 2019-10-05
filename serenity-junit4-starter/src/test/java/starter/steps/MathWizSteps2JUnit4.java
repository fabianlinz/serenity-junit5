package starter.steps;

import net.thucydides.core.annotations.Step;

import static org.assertj.core.api.BDDAssertions.then;

public class MathWizSteps2JUnit4 {


    @Step("just failing")
    public void justFail() {
        then(this).isNull();
    }
}
