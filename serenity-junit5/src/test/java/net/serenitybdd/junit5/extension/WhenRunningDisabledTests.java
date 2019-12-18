package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static net.thucydides.core.model.TestResult.IGNORED;
import static net.thucydides.core.model.TestResult.PENDING;
import static net.thucydides.core.model.TestResult.SUCCESS;

@SerenityExtensionTest
class WhenRunningDisabledTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_be_ignored_if_test_is_disabled() {
        // when
        junit5.executesTestClass(DisabledTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(IGNORED);
    }

    @Test
    void disabled_step_method_is_skipped() {
        // when
        junit5.executesTestClass(DisabledStepTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(IGNORED);
        junit5.shouldHaveStepResults(SUCCESS, IGNORED, SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class DisabledTest {
        @Test
        @Disabled
        void shouldBeDisabled() {
            Assertions.fail("Failing directly by assertion!");
        }

    }

    @SerenityExtensionInnerTest
    static class DisabledStepTest {

        @Steps
        private InnerSteps innerSteps;

        @Test
        void shouldBeDisabled() {
            innerSteps.succeedingStepNumber(1);
            innerSteps.disabled();
            innerSteps.succeedingStepNumber(2);
        }

    }
}
