package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.TestClassExecutionHelper;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;
import org.assertj.core.api.Assertions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JUnit5Steps {

    private List<TestOutcome> testOutcomes;

    @Step
    void executesTestClass(final Class<?> clazz, final TestClassExecutionHelper.EnvironmentConfigurationOverride... environmentConfigurationOverrides) {
        testOutcomes = TestClassExecutionHelper.runTestClass(clazz, environmentConfigurationOverrides);
    }

    @Step
    void shouldHaveExactlyOneTestOutcome() {
        assertThat(testOutcomes.size()).as("number of test outcomes").isEqualTo(1);
    }

    @Step
    void shouldHaveNumberOfTestOutcomes(final int expectedNumber) {
        assertThat(testOutcomes.size()).isEqualTo(expectedNumber);
    }

    @Step
    void shouldHaveOnlyTestOutcomesWithResult(final TestResult result) {
        assertThat(testOutcomes.stream().map(TestOutcome::getResult)).containsOnly(result);
    }

    @Step
    void shouldHaveExactlyOneTestResultXMatchingY(final TestResult result, final String errorMessage) {
        shouldHaveExactlyOneTestOutcome();
        shouldHaveTestResultXMatchingY(0, result, errorMessage);
    }

    @Step
    void shouldHaveTestResultXMatchingY(final int index, final TestResult result, final String errorMessage) {
        shouldHaveTestResultXMatchingY(index, result);
        assertThat(testOutcomes.get(index).getTestFailureMessage()).as("Concise error message").isEqualTo(errorMessage);
    }

    @Step
    void shouldHaveTestResultXMatchingY(final int index, final TestResult result) {
        assertThat(testOutcomes.get(index).getResult()).as("Result").isEqualTo(result);
    }

    @Step
    void stepNumberXShouldHaveResultY(final int stepNumber, final TestResult expectedResult) {
        assertThat(testOutcomes.get(0).getTestSteps().get((stepNumber - 1)).getResult())
                .describedAs("Result of step number " + stepNumber).isEqualTo(expectedResult);
    }

    @Step
    void shouldHaveNoStepResults() {
        shouldHaveStepResults();
    }

    @Step
    void shouldHaveNoStepResults(int index) {
        shouldHaveStepResults(index);
    }

    @Step
    void shouldHaveStepResults(TestResult... expectedResults) {
        shouldHaveStepResults(0, expectedResults);
    }

    @Step
    void shouldHaveStepResults(int index, TestResult... expectedResults) {
        assertThat(testOutcomes.get(index).getStepCount()).isEqualTo(expectedResults.length);
        for (int i = 0; i < expectedResults.length; i++) {
            stepNumberXShouldHaveResultY(i + 1, expectedResults[i]);
        }
    }

    @Step
    void outcomeShouldHaveUserSory(final String displayName) {
        assertThat(testOutcomes.get(0).getUserStory().getDisplayName()).isEqualTo(displayName);
    }

    @Step
    void outcomeShouldHaveTitle(final String title) {
        outcomeShouldHaveTitle(0, title);
    }

    @Step
    void outcomeShouldHaveTitle(final int index, final String title) {
        assertThat(testOutcomes.get(index).getTitle(true)).isEqualTo(title);
    }

    @Step
    void outcomeShouldHaveUserStoryAndTitle(final String userStoryDisplayName, final String title) {
        outcomeShouldHaveUserSory(userStoryDisplayName);
        outcomeShouldHaveTitle(title);
    }

    @Step
    void failingStep() {
        Assertions.fail("This step always fails!");
    }

    @Step
    void shouldHaveExactlyOneTestOutcomeWithResult(final TestResult result) {
        shouldHaveExactlyOneTestOutcome();
        shouldHaveTestResultXMatchingY(0, result);
    }
}
