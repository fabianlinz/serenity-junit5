package starter.steps;

import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Step;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.then;

public class MathWizStepsJUnit4 {

    int actor;

    @Step("#actor starts with {0}")
    public void startsWith(int amount) {
        actor = amount;
    }

    @Step("#actor adds {0}")
    public void adds(int amount) {
        actor = actor + amount;
    }

    @Step("#actor should have {0}")
    public void shouldHave(int expectedTotal) {
        then(actor).isEqualTo(expectedTotal);
    }

    @Step
    public void failingAssumption() {
        assumeThat(true).as("some assumption").isFalse();
    }

    @Step
    @Pending
    public void pending() {
    }
}
