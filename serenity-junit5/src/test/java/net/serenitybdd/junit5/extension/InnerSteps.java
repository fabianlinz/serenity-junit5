package net.serenitybdd.junit5.extension;

import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class InnerSteps {

    @Step
    void succeedingStepNumber(final int number) {
        // do nothing
    }

    @Step
    void failingStepNumber(final int number) {
        Assertions.fail("Failing in step number "+ number+ "!");
    }

    @Step
    void failingByAssertion() {
        Assertions.fail("Failing by assertion!");
    }

    @Step
    void failingByException() {
        throw new RuntimeException("Failing by exception!");
    }

    @Step
    void invalidAssumption() {
        assumeTrue(false,  "Assumption in step is invalid!");
        // can't use AssertJ because as long as Junit4 is on the classpath the Junit4 AssumptionViolatedException
        // is thrown instead of the intended TestAbortedException
    }


    @Step
    @Disabled
    void disabled() {
        throw new UnsupportedOperationException("Disabled step should not be called");
    }

    @Step
    @Pending
    void pending() {
        throw new UnsupportedOperationException("Pending step should not be called");
    }
}
