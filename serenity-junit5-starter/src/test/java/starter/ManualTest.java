package starter;

import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.model.TestResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import starter.steps.ExampleSteps;

@SerenityTest
public class ManualTest {

    @Steps
    private ExampleSteps steps;
    
    @Test
    @Pending
    void pending() {
        steps.succeedingStep();
    }

    @Test
    @Disabled
    void disabled() {
        steps.succeedingStep();
    }

    @Test
    @Manual
    void manualDefault() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.SUCCESS)
    void manualSuccess() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.COMPROMISED)
    void manualCompromised() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.ERROR)
    void manualError() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.ERROR, reason = "A reason for the error")
    void manualErrorWithReason() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.FAILURE)
    void manualFailure() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.IGNORED)
    void manualIgnored() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.PENDING)
    void manualPending() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.SKIPPED)
    void manualSkipped() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.UNDEFINED)
    void manualUndefined() {
        steps.succeedingStep();
    }

    @Test
    @Manual(result = TestResult.UNSUCCESSFUL)
    void manualUnsuccessful() {
        steps.succeedingStep();
    }
}
