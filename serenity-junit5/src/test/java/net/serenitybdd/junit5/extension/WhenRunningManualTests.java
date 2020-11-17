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
    }

    @SerenityExtensionInnerTest
    static class ManualTakesPrecedenceOverPending {
        @Test
        @Manual(result = COMPROMISED)
        @Pending
        void manualDefault() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_default_to_pending() {
        // when
        junit5.executesTestClass(ManualDefault.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualDefault {
        @Test
        @Manual
        void manualDefault() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_success() {
        // when
        junit5.executesTestClass(ManualSuccess.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class ManualSuccess {
        @Test
        @Manual(result = SUCCESS)
        void manualSuccess() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_failure() {
        // when
        junit5.executesTestClass(ManualFailure.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(FAILURE);
    }

    @SerenityExtensionInnerTest
    static class ManualFailure {
        @Test
        @Manual(result = FAILURE)
        void manualFailure() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_ignored() {
        // when
        junit5.executesTestClass(ManualIgnored.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(IGNORED);
    }

    @SerenityExtensionInnerTest
    static class ManualIgnored {
        @Test
        @Manual(result = IGNORED)
        void manualIgnored() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_pending() {
        // when
        junit5.executesTestClass(ManualPending.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualPending {
        @Test
        @Manual(result = PENDING)
        void manualPending() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_skipped() {
        // when
        junit5.executesTestClass(ManualSkipped.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(SKIPPED);
    }

    @SerenityExtensionInnerTest
    static class ManualSkipped {
        @Test
        @Manual(result = SKIPPED)
        void manualSkipped() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_pending_for_undefined() {
        // when
        junit5.executesTestClass(ManualUndefined.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(PENDING);
    }

    @SerenityExtensionInnerTest
    static class ManualUndefined {
        @Test
        @Manual(result = UNDEFINED)
        void manualUndefined() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_error() {
        // when
        junit5.executesTestClass(ManualError.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(ERROR);
    }

    @SerenityExtensionInnerTest
    static class ManualError {
        @Test
        @Manual(result = ERROR)
        void manualError() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_error_for_unsuccessful() {
        // when
        junit5.executesTestClass(ManualUnsuccessful.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(ERROR);
    }

    @SerenityExtensionInnerTest
    static class ManualUnsuccessful {
        @Test
        @Manual(result = UNSUCCESSFUL)
        void manualUnsuccessful() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void the_outcome_should_be_error_for_compromised() {
        // when
        junit5.executesTestClass(ManualCompromised.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(COMPROMISED);
    }

    @SerenityExtensionInnerTest
    static class ManualCompromised {
        @Test
        @Manual(result = COMPROMISED)
        void manualCompromised() {
            throw new UnsupportedOperationException();
        }
    }
}
