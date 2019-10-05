package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.webdriver.Configuration;
import net.thucydides.core.webdriver.DriverConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SerenityExtensionTest
class WhenRunningSerenityTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_be_available_and_correct() {
        // when
        junit5.executesTestClass(SucceedingTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(TestResult.SUCCESS);
        junit5.outcomeShouldHaveUserStoryAndTitle("Succeeding test", "Successful test");
    }

    @SerenityExtensionInnerTest
    static class SucceedingTest {

        @Test
        void successfulTest() {
            assumeTrue(true);
        }
    }

    @Test
    void environment_variables_should_be_injected() {
        // when
        junit5.executesTestClass(InjectEnvironmentVariablesTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(TestResult.SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class InjectEnvironmentVariablesTest {

        EnvironmentVariables environmentVariables;

        @Test
        void successfulTest() {
            assertThat(environmentVariables).as("environment variables").isNotNull();
        }
    }

    @Test
    void driver_configuration_should_be_injected() {
        // when
        junit5.executesTestClass(InjectDriverConfigurationTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(TestResult.SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class InjectDriverConfigurationTest {

        DriverConfiguration driverConfiguration;

        @Test
        void successfulTest() {
            assertThat(driverConfiguration).as("driver configuration").isNotNull();
        }
    }

    @Test
    void configuration_should_be_injected() {
        // when
        junit5.executesTestClass(InjectConfigurationTest.class);

        // then
        junit5.shouldHaveExactlyOneTestOutcomeWithResult(TestResult.SUCCESS);
    }

    @SerenityExtensionInnerTest
    static class InjectConfigurationTest {

        Configuration configuration;

        @Test
        void successfulTest() {
            assertThat(configuration).as("configuration").isNotNull();
        }
    }
}
