package starter;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.model.TestResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class ManualTest {

    @Test
    @Pending
    public void pending() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    public void ignored() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual
    public void manualDefault() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.COMPROMISED)
    public void manualCompromised() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.ERROR)
    public void manualError() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.ERROR, reason = "A reason for the error")
    public void manualErrorWithReason() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.FAILURE)
    public void manualFailure() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.IGNORED)
    public void manualIgnored() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.PENDING)
    public void manualPending() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.SKIPPED)
    public void manualSkipped() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.SUCCESS)
    public void manualSuccess() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.UNDEFINED)
    public void manualUndefined() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Manual(result = TestResult.UNSUCCESSFUL)
    public void manualUnsuccessful() {
        throw new UnsupportedOperationException();
    }
}
