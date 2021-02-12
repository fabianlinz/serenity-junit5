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
        junit5.shouldHaveStepResults(ERROR);
    }

    @SerenityExtensionInnerTest
    static class ManualError {
        
        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = ERROR)
        void manualError() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_have_reason_error_for_unsuccessful() {
        // when
        junit5.executesTestClass(ManualUnsuccessful.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(ERROR, HAS_REASON);
        junit5.shouldHaveStepResults(ERROR);
    }

    @SerenityExtensionInnerTest
    static class ManualUnsuccessful {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = UNSUCCESSFUL)
        void manualUnsuccessful() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_have_reason_error_for_compromised() {
        // when
        junit5.executesTestClass(ManualCompromised.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(COMPROMISED, HAS_REASON);
        junit5.shouldHaveStepResults(COMPROMISED);
    }

    @Test
    void the_outcome_should_have_reason_failure() {
        // when
        junit5.executesTestClass(ManualFailure.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, HAS_REASON);
        junit5.shouldHaveStepResults(FAILURE);
    }

    @SerenityExtensionInnerTest
    static class ManualFailure {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = FAILURE)
        void manualFailure() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class ManualCompromised {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = COMPROMISED)
        void manualCompromised() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_default_of_pending() {
        // when
        junit5.executesTestClass(ManualDefault.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(PENDING, REASON_IS_IGNORED);
        junit5.shouldHaveStepResults(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualDefault {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason")
        void manualDefault() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_success() {
        // when
        junit5.executesTestClass(ManualSuccess.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(SUCCESS, REASON_IS_IGNORED);
        junit5.shouldHaveStepResults(SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class ManualSuccess {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = SUCCESS)
        void manualSuccess() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_ignored() {
        // when
        junit5.executesTestClass(ManualIgnored.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(IGNORED, REASON_IS_IGNORED);
        junit5.shouldHaveStepResults(IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualIgnored {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = IGNORED)
        void manualIgnored() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for__pending() {
        // when
        junit5.executesTestClass(ManualPending.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(PENDING, REASON_IS_IGNORED);
        junit5.shouldHaveStepResults(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualPending {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = PENDING)
        void manualPending() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_skipped() {
        // when
        junit5.executesTestClass(ManualSkipped.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(SKIPPED, REASON_IS_IGNORED);
        junit5.shouldHaveStepResults(SKIPPED);
    }

    @SerenityExtensionInnerTest
    static class ManualSkipped {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = SKIPPED)
        void manualSkipped() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_ignore_reason_for_pending_for_undefined() {
        // when
        junit5.executesTestClass(ManualUndefined.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(PENDING, REASON_IS_IGNORED);
        junit5.shouldHaveStepResults(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualUndefined {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(reason = "Test reason", result = UNDEFINED)
        void manualUndefined() {
            innerSteps.succeedingStepNumber(1);
        }
    }
}
