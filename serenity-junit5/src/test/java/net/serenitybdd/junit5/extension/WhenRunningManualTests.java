package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.annotations.Pending;
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
class WhenRunningManualTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_be_defined_by_manual_annotation_and_ignore_pending_annotation() {
        // when
        junit5.executesTestClass(ManualTakesPrecedenceOverPending.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(COMPROMISED);
        junit5.shouldHaveStepResults(COMPROMISED);
    }

    @SerenityExtensionInnerTest
    static class ManualTakesPrecedenceOverPending {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = COMPROMISED)
        @Pending
        void manualDefault() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_default_to_pending() {
        // when
        junit5.executesTestClass(ManualDefault.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(PENDING);
        junit5.shouldHaveStepResults(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualDefault {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual
        void manualDefault() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_success() {
        // when
        junit5.executesTestClass(ManualSuccess.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(SUCCESS);
        junit5.shouldHaveStepResults(SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class ManualSuccess {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = SUCCESS)
        void manualSuccess() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_failure() {
        // when
        junit5.executesTestClass(ManualFailure.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(FAILURE);
        junit5.shouldHaveStepResults(FAILURE);
    }

    @SerenityExtensionInnerTest
    static class ManualFailure {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = FAILURE)
        void manualFailure() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_ignored() {
        // when
        junit5.executesTestClass(ManualIgnored.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(IGNORED);
        junit5.shouldHaveStepResults(IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualIgnored {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = IGNORED)
        void manualIgnored() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_pending() {
        // when
        junit5.executesTestClass(ManualPending.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(PENDING);
        junit5.shouldHaveStepResults(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualPending {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = PENDING)
        void manualPending() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_skipped() {
        // when
        junit5.executesTestClass(ManualSkipped.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(SKIPPED);
        junit5.shouldHaveStepResults(SKIPPED);
    }

    @SerenityExtensionInnerTest
    static class ManualSkipped {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = SKIPPED)
        void manualSkipped() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_pending_for_undefined() {
        // when
        junit5.executesTestClass(ManualUndefined.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(PENDING);
        junit5.shouldHaveStepResults(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualUndefined {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = UNDEFINED)
        void manualUndefined() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_error() {
        // when
        junit5.executesTestClass(ManualError.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(ERROR);
        junit5.shouldHaveStepResults(ERROR);
    }

    @SerenityExtensionInnerTest
    static class ManualError {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = ERROR)
        void manualError() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_error_for_unsuccessful() {
        // when
        junit5.executesTestClass(ManualUnsuccessful.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(ERROR);
        junit5.shouldHaveStepResults(ERROR);
    }

    @SerenityExtensionInnerTest
    static class ManualUnsuccessful {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = UNSUCCESSFUL)
        void manualUnsuccessful() {
            innerSteps.succeedingStepNumber(1);
        }
    }

    @Test
    void the_outcome_should_be_error_for_compromised() {
        // when
        junit5.executesTestClass(ManualCompromised.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(COMPROMISED);
        junit5.shouldHaveStepResults(COMPROMISED);
    }

    @SerenityExtensionInnerTest
    static class ManualCompromised {

        @Steps
        private InnerSteps innerSteps;

        @Test
        @Manual(result = COMPROMISED)
        void manualCompromised() {
            innerSteps.succeedingStepNumber(1);
        }
    }
}
