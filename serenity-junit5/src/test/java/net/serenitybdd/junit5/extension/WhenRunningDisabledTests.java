package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static net.thucydides.core.model.TestResult.IGNORED;

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

    @SerenityExtensionInnerTest
    static class DisabledTest {
        @Test
        @Disabled
        void shouldBeDisabled() {
            Assertions.fail("Failing directly by assertion!");
        }

    }


}
