package starter.math;

import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.Narrative;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import starter.steps.MathWizSteps;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SerenityTest
@Narrative(text = {"Maths is important."})
class WhenAddingNumbers {

    @Steps
    MathWizSteps michael;

    @Test
    @Pending
    void pending() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }

    @Test
    @Disabled
    void disabled() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }

    @Test
    @DisplayName("fabians spezieller namens test XXXXXXXXXX")
    void addingSumsSuccess() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }

    @Test
    void addingSumsFailedStep() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(4);
    }

    @Test
    @Title("my Serenity specific title")
    void addingSumsFailedAssertion() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        then(michael).as("should fail").isNull();
    }

    @Test
    void throwingExceptionFailure() {
        // When
        throw new RuntimeException("should fail the test");
    }


    @Test
    void abortedTest() {
        assumeTrue("abc".contains("Z"), "abc does not contain Z");
        // aborted ...
    }

    @Test
    public void abortedTestInStep() {
        michael.failingAssumption();
        michael.adds(2);
    }


    @Test
    public void failureAfterPreviouslyFailingStepIsIgnored() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.justFail();
        michael.shouldHave(5);
        Assertions.fail("Fail after failing step");
    }

}
