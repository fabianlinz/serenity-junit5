package starter.steps;

import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Step;
import org.junit.jupiter.api.Disabled;

public class ExampleSteps {

    @Step
    public void succeedingStep() {
        // do nothing
    }

    @Step
    @Pending
    public void pendingStep() {
        throw new UnsupportedOperationException("pending step should not be executed");
    }

    @Step
    @Disabled
    public void disabledStep() {
        throw new UnsupportedOperationException("disabled step should not be executed");
    }
}
