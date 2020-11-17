package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Steps;
import org.junit.jupiter.api.Test;

import static net.thucydides.core.model.TestResult.COMPROMISED;
import static net.thucydides.core.model.TestResult.ERROR;
import static net.thucydides.core.model.TestResult.FAILURE;
import static net.thucydides.core.model.TestResult.IGNORED;
import static net.thucydides.core.model.TestResult.PENDING;
import static net.thucydides.core.model.TestResult.SKIPPED;
import static net.thucydides.core.model.TestResult.SUCCESS;
import static net.thucydides.core.model.TestResult.UNDEFINED;
import static net.thucydides.core.model.TestResult.UNSUCCESSFUL;

@SerenityExtensionTest
/**
 * Reason is actually only supported (in Junit4/SerenityRunner) for 
 * * FAILURE
 * * ERROR
 * * COMPROMISED
 * * UNSUCCESSFUL
 */
class WhenRunningManualWithReasonTests {

    public static final String REASON_IS_IGNORED = null;
    public static final String HAS_REASON = "Manual test failure: Test reason";
    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_have_reason_error() {
        // when
        junit5.executesTestClass(ManualError.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(ERROR, HAS_REASON);
    }

    @SerenityExtensionInnerTest
    static class ManualError {
        @Test
        @Manual(reason = "Test reason", result = ERROR)
        void manualError() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_have_reason_error_for_unsuccessful() {
        // when
        junit5.executesTestClass(ManualUnsuccessful.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(ERROR, HAS_REASON);
    }

    @SerenityExtensionInnerTest
    static class ManualUnsuccessful {
        @Test
        @Manual(reason = "Test reason", result = UNSUCCESSFUL)
        void manualUnsuccessful() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_have_reason_error_for_compromised() {
        // when
        junit5.executesTestClass(ManualCompromised.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(COMPROMISED, HAS_REASON);
    }

    @Test
    void the_outcome_should_have_reason_failure() {
        // when
        junit5.executesTestClass(ManualFailure.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, HAS_REASON);
    }

    @SerenityExtensionInnerTest
    static class ManualFailure {
        @Test
        @Manual(reason = "Test reason", result = FAILURE)
        void manualFailure() {
            throw new UnsupportedOperationException();
        }
    }

    @SerenityExtensionInnerTest
    static class ManualCompromised {
        @Test
        @Manual(reason = "Test reason", result = COMPROMISED)
        void manualCompromised() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_default_of_pending() {
        // when
        junit5.executesTestClass(ManualDefault.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(PENDING, REASON_IS_IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualDefault {
        @Test
        @Manual(reason = "Test reason")
        void manualDefault() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_success() {
        // when
        junit5.executesTestClass(ManualSuccess.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(SUCCESS, REASON_IS_IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualSuccess {
        @Test
        @Manual(reason = "Test reason", result = SUCCESS)
        void manualSuccess() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_ignored() {
        // when
        junit5.executesTestClass(ManualIgnored.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(IGNORED, REASON_IS_IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualIgnored {
        @Test
        @Manual(reason = "Test reason", result = IGNORED)
        void manualIgnored() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for__pending() {
        // when
        junit5.executesTestClass(ManualPending.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(PENDING, REASON_IS_IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualPending {
        @Test
        @Manual(reason = "Test reason", result = PENDING)
        void manualPending() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_skipped() {
        // when
        junit5.executesTestClass(ManualSkipped.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(SKIPPED, REASON_IS_IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualSkipped {
        @Test
        @Manual(reason = "Test reason", result = SKIPPED)
        void manualSkipped() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_pending_for_undefined() {
        // when
        junit5.executesTestClass(ManualUndefined.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(PENDING, REASON_IS_IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualUndefined {
        @Test
        @Manual(reason = "Test reason", result = UNDEFINED)
        void manualUndefined() {
            throw new UnsupportedOperationException();
        }
    }
}
