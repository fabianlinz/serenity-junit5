package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.thucydides.core.model.TestResult.FAILURE;
import static net.thucydides.core.model.TestResult.IGNORED;
import static net.thucydides.core.model.TestResult.SKIPPED;
import static net.thucydides.core.model.TestResult.SUCCESS;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SerenityExtensionTest
class WhenRunningWithInvalidAssumptionsTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_be_a_ignored_test_if_an_assumption_outside_a_step_invalid() {
        // when
        junit5.executesTestClass(InvalidAssumptionsOutsideAStepTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(IGNORED);
    }

    @Test
    void the_outcome_should_be_a_ignored_test_if_an_assumption_inside_a_step_is_invalid() {
        // when
        junit5.executesTestClass(InvalidAssumptionInsideAStepTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(IGNORED);
    }

    @Test
    void steps_after_ignored_step_should_be_ignored() {
        // when
        junit5.executesTestClass(AdditionalStepAfterStepWithInvalidAssumptionTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(IGNORED);
        junit5.shouldHaveStepResults(SUCCESS, SUCCESS, IGNORED, IGNORED);
    }

    @Test
    void steps_after_direct_invalid_assumption_should_not_be_recorded() {
        // when
        junit5.executesTestClass(AdditionalStepAfterInvalidAssumptionTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(IGNORED);
        junit5.shouldHaveStepResults(SUCCESS, SUCCESS);
    }

    @Test
    void test_fails_with_direct_failure_after_step_with_invalid_assumption() {
        // when
        junit5.executesTestClass(HavingAdditionalAssertionFailureAfterStepWithInvalidAssumptionTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, "Failing after invalid assumption!");
        junit5.shouldHaveStepResults(SUCCESS, IGNORED, IGNORED);
    }

    @SerenityExtensionInnerTest
    static class InvalidAssumptionsOutsideAStepTest {

        @Test
        void shouldHaveInvalidAssumptionOutsideStep() {
            assumeTrue(false,  "Assumption outside step is invalid!");
        }
    }

    @SerenityExtensionInnerTest
    static class InvalidAssumptionInsideAStepTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldHaveInvalidAssumptionInsideStep() {
            steps.invalidAssumption();
        }
    }

    @SerenityExtensionInnerTest
    static class AdditionalStepAfterStepWithInvalidAssumptionTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldHaveInvalidAssumptionInsideStep() {
            steps.succeedingStepNumber(1);
            steps.succeedingStepNumber(2);
            steps.invalidAssumption();
            steps.succeedingStepNumber(4);
        }
    }

    @SerenityExtensionInnerTest
    static class AdditionalStepAfterInvalidAssumptionTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldHaveInvalidAssumptionOutsideStep() {
            steps.succeedingStepNumber(1);
            steps.succeedingStepNumber(2);
            assumeTrue(false,  "Assumption outside step is invalid!");
            steps.succeedingStepNumber(4);
        }
    }

    @SerenityExtensionInnerTest
    static class HavingAdditionalAssertionFailureAfterStepWithInvalidAssumptionTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldFailInStep2() {
            steps.succeedingStepNumber(1);
            steps.invalidAssumption();
            steps.succeedingStepNumber(3);
            Assertions.fail("Failing after invalid assumption!");
            steps.succeedingStepNumber(4);
        }
    }

}
