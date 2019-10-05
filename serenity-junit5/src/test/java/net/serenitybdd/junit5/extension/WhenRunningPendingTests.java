package net.serenitybdd.junit5.extension;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;

import static net.thucydides.core.model.TestResult.IGNORED;
import static net.thucydides.core.model.TestResult.PENDING;
import static net.thucydides.core.model.TestResult.SUCCESS;

@SerenityExtensionTest
class WhenRunningPendingTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_be_pending_if_test_is_pending() {
        // when
        junit5.executesTestClass(PendingTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(PENDING);
    }

    @Test
    void the_outcome_should_be_pending_if_step_is_pending() {
        // when
        junit5.executesTestClass(PendingStepTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(PENDING);
    }

    @Test
    void steps_after_pending_step_should_be_ignored() {
        // when
        junit5.executesTestClass(HavingStepAfterPendingStepTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(PENDING);
        junit5.shouldHaveStepResults(SUCCESS, PENDING, IGNORED);
    }

    @SerenityExtensionInnerTest
    static class PendingTest {

        @Test
        @Pending
        void shouldBePending() {
            Assertions.fail("Failing directly by assertion!");
        }
    }

    @SerenityExtensionInnerTest
    static class PendingStepTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldBePending() {
            steps.pending();
        }
    }

    @SerenityExtensionInnerTest
    static class HavingStepAfterPendingStepTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldBePending() {
            steps.succeedingStepNumber(1);
            steps.pending();
            steps.succeedingStepNumber(3);
        }
    }

}
