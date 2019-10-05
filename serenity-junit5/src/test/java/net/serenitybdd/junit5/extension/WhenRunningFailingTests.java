package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.thucydides.core.model.TestResult.ERROR;
import static net.thucydides.core.model.TestResult.FAILURE;
import static net.thucydides.core.model.TestResult.SKIPPED;
import static net.thucydides.core.model.TestResult.SUCCESS;

@SerenityExtensionTest
class WhenRunningFailingTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_be_a_failure_if_test_fails_by_assertion() {
        // when
        junit5.executesTestClass(FailingByAssertionTest.class);

        // then
        junit5.shouldHaveNumberOfTestOutcomes(2);
        junit5.shouldHaveOnlyTestOutcomesWithResult(FAILURE);
    }

    @Test
    void the_outcome_should_be_an_error_if_test_fails_by_exception() {
        // when
        junit5.executesTestClass(FailingByExceptionTest.class);

        // then
        junit5.shouldHaveNumberOfTestOutcomes(2);
        junit5.shouldHaveOnlyTestOutcomesWithResult(ERROR);
    }

    @Test
    void steps_after_failing_step_should_be_skipped() {
        // when
        junit5.executesTestClass(HavingAdditionalStepsAfterStepFailureTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(FAILURE);
        junit5.shouldHaveStepResults(SUCCESS, SUCCESS, FAILURE, SKIPPED);
    }

    @Test
    void steps_after_direct_failures_should_not_be_recorded() {
        // when
        junit5.executesTestClass(HavingAdditionalStepsAfterFailureTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(FAILURE);
        junit5.shouldHaveStepResults(SUCCESS, SUCCESS);
    }

    @Test
    void fails_with_first_failing_step_on_subsequent_assertion_error() {
        // when
        junit5.executesTestClass(HavingAdditionalAssertionFailureAfterStepFailureTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, "Failing in step number 2!");
        junit5.shouldHaveStepResults(SUCCESS, FAILURE, SKIPPED);
    }

    @Test
    void before_each_lifecycle_fails_by_assertion() {
        // when
        junit5.executesTestClass(BeforeEachFailsWithAssertionErrorTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, "BeforeEach failed");
        junit5.shouldHaveNoStepResults();
    }

    @Test
    void before_each_lifecycle_fails_by_exception() {
        // when
        junit5.executesTestClass(BeforeEachFailsWithExceptionTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(ERROR, "Unexpected exception in beforeEach!");
        junit5.shouldHaveNoStepResults();
    }

    @Test
    void after_each_lifecycle_fails_by_assertion() {
        // when
        junit5.executesTestClass(AfterEachFailsWithAssertionErrorTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, "AfterEach failed");
        junit5.shouldHaveStepResults(SUCCESS);
    }

    @Test
    void after_each_lifecycle_fails_by_exception() {
        // when
        junit5.executesTestClass(AfterEachFailsWithExceptionTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(ERROR, "Unexpected exception in afterEach!");
        junit5.shouldHaveStepResults(SUCCESS);
    }

    @Test
    void before_all_lifecycle_fails_by_assertion() {
        // when
        junit5.executesTestClass(BeforeAllFailsWithAssertionErrorTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(FAILURE, "BeforeAll failed");
        junit5.outcomeShouldHaveTitle("Initialization");
        junit5.shouldHaveNoStepResults();
    }

    @Test
    void before_all_lifecycle_fails_by_exception() {
        // when
        junit5.executesTestClass(BeforeAllFailsWithExceptionTest.class);

        // then
        junit5.shouldHaveExactlyOneTestResultXMatchingY(ERROR, "Unexpected exception in beforeAll!");
        junit5.outcomeShouldHaveTitle("Initialization");
        junit5.shouldHaveNoStepResults();
    }

    @Test
    void after_all_lifecycle_fails_by_assertion() {
        // when
        junit5.executesTestClass(AfterAllFailsWithAssertionErrorTest.class);

        // then
        junit5.shouldHaveNumberOfTestOutcomes(2);
        junit5.shouldHaveTestResultXMatchingY(0, SUCCESS);
        junit5.shouldHaveTestResultXMatchingY(1, FAILURE, "AfterAll failed");
        junit5.outcomeShouldHaveTitle(1, "Tear down");
        junit5.shouldHaveNoStepResults(1);
    }

    @Test
    void after_all_lifecycle_fails_by_exception() {
        // when
        junit5.executesTestClass(AfterAllFailsWithExceptionTest.class);

        // then
        junit5.shouldHaveNumberOfTestOutcomes(2);
        junit5.shouldHaveTestResultXMatchingY(0, SUCCESS);
        junit5.shouldHaveTestResultXMatchingY(1, ERROR, "Unexpected exception in afterAll!");
        junit5.outcomeShouldHaveTitle(1, "Tear down");
        junit5.shouldHaveNoStepResults(1);
    }

    @SerenityExtensionInnerTest
    static class FailingByAssertionTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldFailByAssertion() {
            Assertions.fail("Failing directly by assertion!");
        }

        @Test
        void shouldFailByAssertionInStep() {
            steps.failingByAssertion();
        }
    }

    @SerenityExtensionInnerTest
    static class FailingByExceptionTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldFailByException() {
            throw new RuntimeException("Unexpected exception in test!");
        }

        @Test
        void shouldFailByExceptionInStep() {
            steps.failingByException();
        }

    }

    @SerenityExtensionInnerTest
    static class HavingAdditionalStepsAfterStepFailureTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldFailInStep3() {
            steps.succeedingStepNumber(1);
            steps.succeedingStepNumber(2);
            steps.failingStepNumber(3);
            steps.succeedingStepNumber(4);
        }
    }

    @SerenityExtensionInnerTest
    static class HavingAdditionalStepsAfterFailureTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldFailInStep3() {
            steps.succeedingStepNumber(1);
            steps.succeedingStepNumber(2);
            Assertions.fail("Failing directly here!");
            steps.failingStepNumber(3);
            steps.succeedingStepNumber(4);
        }
    }

    @SerenityExtensionInnerTest
    static class HavingAdditionalAssertionFailureAfterStepFailureTest {

        @Steps
        private InnerSteps steps;

        @Test
        void shouldFailInStep2() {
            steps.succeedingStepNumber(1);
            steps.failingStepNumber(2);
            steps.succeedingStepNumber(3);
            Assertions.fail("Failing after failing step");
            steps.succeedingStepNumber(4);
        }
    }

    @SerenityExtensionInnerTest
    static class BeforeEachFailsWithAssertionErrorTest {

        @Steps
        private InnerSteps steps;

        @BeforeEach
        void beforeEach() {
            Assertions.fail("BeforeEach failed");
        }
        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class BeforeEachFailsWithExceptionTest {

        @Steps
        private InnerSteps steps;

        @BeforeEach
        void beforeEach() {
            throw new RuntimeException("Unexpected exception in beforeEach!");
        }
        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class AfterEachFailsWithAssertionErrorTest {

        @Steps
        private InnerSteps steps;

        @AfterEach
        void afterEach() {
            Assertions.fail("AfterEach failed");
        }
        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class AfterEachFailsWithExceptionTest {

        @Steps
        private InnerSteps steps;

        @AfterEach
        void afterEach() {
            throw new RuntimeException("Unexpected exception in afterEach!");
        }
        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class BeforeAllFailsWithAssertionErrorTest {

        @Steps
        private InnerSteps steps;

        @BeforeAll
        static void beforeAll() {
            Assertions.fail("BeforeAll failed");
        }

        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class BeforeAllFailsWithExceptionTest {

        @Steps
        private InnerSteps steps;

        @BeforeAll
        static void beforeAll() {
            throw new RuntimeException("Unexpected exception in beforeAll!");
        }

        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class AfterAllFailsWithAssertionErrorTest {

        @Steps
        private InnerSteps steps;

        @AfterAll
        static void afterAll() {
            Assertions.fail("AfterAll failed");
        }

        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }

    @SerenityExtensionInnerTest
    static class AfterAllFailsWithExceptionTest {

        @Steps
        private InnerSteps steps;

        @AfterAll
        static void afterAll() {
            throw new RuntimeException("Unexpected exception in afterAll!");
        }

        @Test
        void shouldSucceed() {
            steps.succeedingStepNumber(1);
        }
    }
}
