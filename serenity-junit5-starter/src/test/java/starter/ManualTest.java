package starter;

import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.model.TestResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SerenityTest
public class ManualTest {

    @Test
    @Pending
    void pending() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    void disabled() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual
    void manualDefault() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.SUCCESS)
    void manualSuccess() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.COMPROMISED)
    void manualCompromised() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.ERROR)
    void manualError() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.ERROR, reason = "A reason for the error")
    void manualErrorWithReason() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.FAILURE)
    void manualFailure() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.IGNORED)
    void manualIgnored() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.PENDING)
    void manualPending() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.SKIPPED)
    void manualSkipped() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.UNDEFINED)
    void manualUndefined() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.UNSUCCESSFUL)
    void manualUnsuccessful() {
        throw new UnsupportedOperationException();
    }
}
