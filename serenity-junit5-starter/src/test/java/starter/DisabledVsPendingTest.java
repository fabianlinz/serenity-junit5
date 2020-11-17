package starter;

import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import starter.steps.ExampleSteps;

@SerenityTest
class DisabledVsPendingTest {

    @Steps
    private ExampleSteps exampleSteps;

    @Test
    @Pending
    void test_should_be_pending() {
        exampleSteps.succeedingStep();
    }

    @Test
    @Disabled
    void test_should_be_ignored() {
        exampleSteps.succeedingStep();
    }

    @Test
    void test_should_be_pending_due_to_pending_step() {
        exampleSteps.succeedingStep();
        exampleSteps.pendingStep();
        exampleSteps.succeedingStep();
    }

    @Test
    void test_should_be_ignored_due_to_disabled_step() {
        exampleSteps.succeedingStep();
        exampleSteps.disabledStep();
        exampleSteps.succeedingStep();
    }
}
