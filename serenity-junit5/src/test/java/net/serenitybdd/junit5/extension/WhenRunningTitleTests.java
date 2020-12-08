package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.jupiter.api.Test;

@SerenityExtensionTest
class WhenRunningTitleTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_honor_the_declared_title() {
        // when
        junit5.executesTestClass(TestWithTitle.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.outcomeShouldHaveTitle("explicit test title");
    }

    @SerenityExtensionInnerTest
    static class TestWithTitle {
        @Test
        @Title("explicit test title")
        void testWithTitle() {
        }
    }
}
