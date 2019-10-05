package starter.math;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Narrative;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import starter.steps.MathWizSteps2JUnit4;
import starter.steps.MathWizStepsJUnit4;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assume.assumeTrue;

@RunWith(SerenityRunner.class)
@Narrative(text={"Maths is important."})
public class WhenAddingNumbersJUnit4 {

    @Steps
    MathWizStepsJUnit4 michael;

    @Steps
    MathWizSteps2JUnit4 jens;

    @Test
    @Pending
    public void pending() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }

    @Test
    public void pendingStep() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);
        michael.pending();

        // Then
        michael.shouldHave(3);
    }

    @Test
    @Ignore("reason to ignore this test")
    public void ignored() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }

    @Test
    public void addingSumsSuccess() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        michael.shouldHave(3);
    }

    @Test
    @Title("my Serenity specific title")
    public void addingSumsFailed() {
        // Given
        michael.startsWith(1);

        // When
        michael.adds(2);

        // Then
        jens.justFail();
        michael.shouldHave(5);
        then(this).withFailMessage("This does not work!!!!").isNull();
        System.out.println("Hello.");
    }

    @Test
   public void abortedTest() {
        assumeTrue("abc".contains("Z"));
        // test ignored ...
    }

    @Test
    public void abortedTestInStep() {
        michael.failingAssumption();
        michael.adds(2);
    }


}
