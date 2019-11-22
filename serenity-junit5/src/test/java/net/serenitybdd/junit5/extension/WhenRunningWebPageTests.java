package net.serenitybdd.junit5.extension;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.serenitybdd.junit5.extension.testsupport.TestClassExecutionHelper;
import net.serenitybdd.junit5.extension.testsupport.Assertions;
import net.thucydides.core.annotations.ClearCookiesPolicy;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.DriverOptions;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.WithDriver;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.assertj.core.api.ObjectAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static net.serenitybdd.junit5.extension.testsupport.Assertions.assertThat;

// TODO restructure tests by setup type (each with multiple verifications) (see comment below)
/**
 * Inject
 * - I1) WebDriver into test @Managed WebDriver fields
 * - I2) WebDriver into Step
 * - I3) Page into test
 * - I4) Page factory into test
 *
 * cases for tests with one @Managed WebDriver field:
 * - test class level config
 *      - T1: @Managed => default
 *      - T2: @Managed(driver = "xx")
 *      - T3: @Managed(driver = "xx", options = "yy")
 *      - T4: @Managed(options = "yy")
 * - test method level
 *      - T1: none
 *      - T2: @WithDriver
 *      - T3: @WithDriver + @DriverOptions
 *      - (note that just @DriverOptions is not possible)
 *
 * => 12 cases with three verifications with tests for I1 - I4.
 *
 * For test case with multiple web driver fields this gets even more complicated.
 */
@SerenityExtensionTest
@Testcontainers
class WhenRunningWebPageTests {

    @Container
    public static BrowserWebDriverContainer seleniumContainer = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withSharedMemorySize(1000L * 1024 * 1024) // 1GB shared memory to avoid seleniumContainer crash, see https://github.com/SeleniumHQ/docker-selenium#running-the-images
            .withEnv("NODE_MAX_INSTANCES", "3").withEnv("NODE_MAX_SESSION", "3"); // more than one seleniumContainer needed for net.serenitybdd.junit.extension.WhenRunningWebPageTests.conditional_clearing_of_web_driver

    @Steps
    private JUnit5Steps junit5;

    @Test
    void it_should_be_correctly_wired_and_actually_open_the_page() {
        // when
        junit5.executesTestClass(SettingsTest.class, inDockerSeleniumHub());

        // then
        junit5.shouldHaveExactlyOneTestOutcome();
        junit5.shouldHaveOnlyTestOutcomesWithResult(TestResult.FAILURE);
        junit5.shouldHaveStepResults(TestResult.SUCCESS, TestResult.SUCCESS, TestResult.FAILURE);
    }

    @Test
    void inject_default_web_driver_into_test() {
        executeWithSuccessfulTests(InjectDefaultWebDriverIntoTestTest.class, 1); // TODO improve error message in case this fails ... => somehow print the output in case it is not successfull
    }

    @Test
    void inject_test_class_specific_web_driver_into_test() {
        executeWithSuccessfulTests(InjectTestClassSpecificWebDriverIntoTestTest.class, 1);
    }

    @Test
    void inject_test_class_specific_web_driver_into_steps() {
        executeWithSuccessfulTests(InjectTestClassSpecificWebDriverIntoStepsTest.class, 1);
    }

    @Test
    void inject_test_class_specific_web_driver_into_steps_without_clearing_cookies() {
        executeWithSuccessfulTests(InjectTestClassSpecificWebDriverIntoStepsWithoutClearingCookiesTest.class, 1);
    }

    @Test
    void inject_test_method_specific_web_driver_into_test() {
        executeWithSuccessfulTests(InjectTestMethodSpecificWebDriverIntoTestTest.class, 2);
    }

    @Test
    void inject_test_method_specific_web_driver_into_steps() {
        executeWithSuccessfulTests(InjectTestMethodSpecificWebDriverIntoStepsTest.class, 2);
    }

    @Test
    void inject_multiple_web_drivers() {
        executeWithSuccessfulTests(InjectMultipleWebDriversTest.class, 4);
    }

    @Test
    void inject_multiple_web_drivers_in_combination_with_test_method_level_web_driver_configuration() {
        executeWithSuccessfulTests(InjectMultipleWebDriversInCombinationWithTestMethodLevelWebDriverConfigurationTest.class, 3);
    }

    @Test
    void inject_default_web_driver_into_steps() {
        executeWithSuccessfulTests(InjectDefaultWebDriverIntoStepsTest.class, 1);
    }

    @Test
    void inject_default_web_driver_with_configured_options_into_test() {
        executeWithSuccessfulTests(InjectDefaultWebDriverWithConfiguredOptionsIntoTestTest.class, 1);
    }

    @Test
    void inject_default_web_driver_with_configured_options_into_steps() {
        executeWithSuccessfulTests(InjectDefaultWebDriverWithConfiguredOptionsIntoStepTest.class, 1);
    }

    @Test
    void inject_test_method_specific_web_driver_with_configured_options_into_test() {
        executeWithSuccessfulTests(InjectTestMethodSpecificWebDriverWithConfiguredOptionsIntoTestTest.class, 2);
    }

    @Test
    void inject_page_with_default_web_driver_into_test() {
        executeWithSuccessfulTests(InjectPageWithDefaultWebDriverIntoTestTest.class, 1);
    }

    @Test
    void inject_page_with_test_case_specific_web_driver_into_test() {
        executeWithSuccessfulTests(InjectPageWithTestCaseSpecificWebDriverIntoTest.class, 1);
    }

    @Test
    void inject_page_factory() {
        executeWithSuccessfulTests(InjectPageFactoryTest.class, 1);
    }

    @Test
    void conditional_clearing_of_web_driver() {
        executeWithSuccessfulTests(ConditionalClearingOfWebDriverTest.class, 2, inDockerSeleniumHub());
    }

    private void executeWithSuccessfulTests(final Class<?> clazz, final int expectedNumber, final TestClassExecutionHelper.EnvironmentConfigurationOverride... environmentConfigurationOverrides) {
        // when
        junit5.executesTestClass(clazz, environmentConfigurationOverrides);

        // then
        junit5.shouldHaveNumberOfTestOutcomes(expectedNumber);
        junit5.shouldHaveOnlyTestOutcomesWithResult(TestResult.SUCCESS);
    }

    @NotNull
    private TestClassExecutionHelper.EnvironmentConfigurationOverride[] inDockerSeleniumHub() {
        System.out.println("VNC:" + seleniumContainer.getVncAddress());
        System.out.println("Selenium HUB:" + seleniumContainer.getSeleniumAddress());
        return new TestClassExecutionHelper.EnvironmentConfigurationOverride[]{
                new TestClassExecutionHelper.EnvironmentConfigurationOverride("webdriver.driver", "chrome"),
                new TestClassExecutionHelper.EnvironmentConfigurationOverride("webdriver.remote.url", seleniumContainer.getSeleniumAddress().toString()),
                new TestClassExecutionHelper.EnvironmentConfigurationOverride("webdriver.remote.driver", "chrome")};
    }

    @SerenityExtensionInnerTest
    static class InjectDefaultWebDriverIntoTestTest {

        @Managed
        WebDriver driver;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(driver).hasDriverName("chrome");
        }
    }

    @SerenityExtensionInnerTest
    static class InjectDefaultWebDriverIntoStepsTest {

        @Managed
        WebDriver driver;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("chrome")
                    .isSameAs(driver);
        }
    }

    @SerenityExtensionInnerTest
    static class InjectTestClassSpecificWebDriverIntoTestTest {

        @Managed(driver = "firefox")
        WebDriver driver;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(driver).hasDriverName("firefox");
        }
    }

    @SerenityExtensionInnerTest
    static class InjectTestClassSpecificWebDriverIntoStepsTest {

        @Managed(driver = "firefox")
        WebDriver driver;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("firefox")
                    .isSameAs(driver);
        }
    }

    @SerenityExtensionInnerTest
    static class InjectTestClassSpecificWebDriverIntoStepsWithoutClearingCookiesTest {

        @Managed(driver = "firefox", clearCookies = ClearCookiesPolicy.Never)
        WebDriver driver;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("firefox")
                    .isSameAs(driver);
        }
    }

    @SerenityExtensionInnerTest
    static class InjectTestMethodSpecificWebDriverIntoTestTest {

        @Managed
        WebDriver driver;

        @Test
        void shouldInjectDefaultDriver() {
            Assertions.assertThat(driver).hasDriverName("chrome");
        }

        @Test
        @WithDriver("firefox")
        void shouldInjectMethodLevelDriver() {
            Assertions.assertThat(driver).hasDriverName("firefox");
        }

    }

    @SerenityExtensionInnerTest
    static class InjectTestMethodSpecificWebDriverIntoStepsTest {

        @Managed
        WebDriver driver;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        void shouldInjectDefaultDriverIntoSteps() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("chrome")
                    .isSameAs(driver);
        }

        @Test
        @WithDriver("firefox")
        void shouldInjectMethodLevelDriverIntoSteps() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("firefox")
                    .isSameAs(driver);
        }
    }

    @SerenityExtensionInnerTest
    static class InjectMultipleWebDriversTest {

        @Managed(driver = "firefox")
        WebDriver driverFirefox;

        @Managed(driver = "chrome")
        WebDriver driverChrome;

        @Managed
        WebDriver driverWithDefaultTypeButNotNecessarilyTheDefaultDriverInstanceUsedForSteps;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        void shouldInjectExplicitlyRequestedFirefox() {
            Assertions.assertThat(driverFirefox).hasDriverName("firefox");
        }

        @Test
        void shouldInjectExplicitlyRequestedChrome() {
            Assertions.assertThat(driverChrome).hasDriverName("chrome");
        }

        @Test
        void shouldInjectDriveWithArbitraryDefaultType() {
            // CAUTION: currently this could be type name could be firefox or chrome depending on which of the three @Manager field is considered the "first" field
            // see also net.serenitybdd.junit.extension.page.SerenityPageExtension.explicitWebDriverConfiguration(org.junit.jupiter.api.extension.ExtensionContext)
            Assertions.assertThat(driverWithDefaultTypeButNotNecessarilyTheDefaultDriverInstanceUsedForSteps).as("default driver")
                    .hasSameDriverNameAs(ThucydidesWebDriverSupport.getDriver()) // ^1 NOT necessarily: isSameAs(ThucydidesWebDriverSupport.getDriver())
                    .isNotIn(driverFirefox, driverChrome);

            /*
             * ^1: One could assume that if no explicit type is given the default web driver is used => .
             * This is only the case for the usual test setup with one @Managed WebDriver field. If there are multiple
             * @Managed WebDriver fields this is not necessarily the case.
             *
             * net.thucydides.core.annotations.TestCaseAnnotations#injectDrivers only uses the defaultDriver if no name
             * is given but this can never happen. So all three @Managed fields get a separate WebDriver instance.
             * depending on the arbitrary reflection field order the "first" @Managed field defines the default web driver
             * which is used for the steps.
             */
        }

        @Test
        void shouldUseWebDriverWithDefaultTypeForSteps() {
            Assertions.assertThat(wikipedia.getDriver()).as("step driver")
                    .isSameAs(ThucydidesWebDriverSupport.getDriver())
                    .hasSameDriverNameAs(driverWithDefaultTypeButNotNecessarilyTheDefaultDriverInstanceUsedForSteps)
                    .isIn(driverChrome, driverFirefox, driverWithDefaultTypeButNotNecessarilyTheDefaultDriverInstanceUsedForSteps); // ^1 (see above) NOT necessarily: isSameAs(driverWithDefaultTypeButNotNecessarilyTheDefaultDriverInstanceUsedForSteps)
        }
    }

    @SerenityExtensionInnerTest
    static class InjectMultipleWebDriversInCombinationWithTestMethodLevelWebDriverConfigurationTest {

        @Managed(driver = "firefox")
        WebDriver driverFirefox;

        @Managed(driver = "chrome")
        WebDriver driverChrome;

        @Managed
        WebDriver driverWithDefaultType;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        @WithDriver("firefox")
        void shouldInjectMethodLevelDriverIntoTest() {
            Assertions.assertThat(driverWithDefaultType).hasDriverName("firefox");
        }

        @Test
        @WithDriver("firefox")
        void shouldInjectMethodLevelDriverIntoSteps() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("firefox");
        }

        @Test
        @WithDriver("firefox")
        void shouldNotInjectMethodLevelDriver() {
            Assertions.assertThat(driverFirefox).as("@Managed(driver = \"firefox\")").hasDriverName("firefox");
            Assertions.assertThat(driverChrome).as("@Managed(driver = \"chrome\")").hasDriverName("chrome");
        }


    }

    @SerenityExtensionInnerTest
    static class InjectDefaultWebDriverWithConfiguredOptionsIntoTestTest {

        @Managed(options = "testOption")
        WebDriver driver;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(driver).hasOptions("testOption");
        }
    }

    @SerenityExtensionInnerTest
    static class InjectDefaultWebDriverWithConfiguredOptionsIntoStepTest {

        @Managed(options = "testOption")
        WebDriver driver;

        @Steps
        SeleniumHubSteps wikipedia;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(wikipedia.getDriver()).as("steps web driver")
                    .hasDriverName("chrome")
                    .hasOptions("testOption")
                    .isSameAs(driver);
        }
    }

    @SerenityExtensionInnerTest
    static class InjectTestClassSpecificWebDriverWithConfiguredOptionsIntoTestTest {

        @Managed(driver = "firefox", options = "testOption")
        WebDriver driver;

        @Test
        void shouldSucceed() {
            Assertions.assertThat(driver)
                    .hasDriverName("firefox")
                    .hasOptions("testOption");
        }
    }

    @SerenityExtensionInnerTest
    static class InjectTestMethodSpecificWebDriverWithConfiguredOptionsIntoTestTest {

        @Managed
        WebDriver driver;

        @Test
        void shouldInjectDefaultWebDriver() {
            Assertions.assertThat(driver).hasDriverName("chrome");
        }

        @Test
        @WithDriver("firefox")
        @DriverOptions("methodDriverOptions")
        void shouldInjectMethodSpecificWebDriver() {
            Assertions.assertThat(driver)
                    .hasDriverName("firefox")
                    .hasOptions(""); // CAUTION: this is not what one would expect BUT is consistent with Junit4
            // could be considered a bug in TestCaseAnnotations#injectDrivers (driver name and options are not handled the same way although they belong together)
        }
    }

    @SerenityExtensionInnerTest
    static class InjectPageWithDefaultWebDriverIntoTestTest {

        @Managed
        WebDriver driver;

        SeleniumHubPage page;

        @Test
        void shouldSucceed() {
            org.assertj.core.api.Assertions.assertThat(page).as("page")
                    .extracting(PageObject::getDriver).as("page driver")
                    .isSameAs(driver);
        }
    }

    @SerenityExtensionInnerTest
    static class InjectPageWithTestCaseSpecificWebDriverIntoTest {

        @Managed(driver = "firefox")
        WebDriver driver;

        SeleniumHubPage page;

        @Test
        void shouldSucceed() {
            org.assertj.core.api.Assertions.assertThat(page).as("page")
                    .extracting(PageObject::getDriver).as("page driver")
                    .satisfies(webDriverOfPage ->
                            Assertions.assertThat(webDriverOfPage)
                                    .hasDriverName("firefox")
                                    .isSameAs(driver));
        }
    }


    @SerenityExtensionInnerTest
    static class InjectPageFactoryTest {

        @Managed
        WebDriver driver;

        @ManagedPages
        Pages pages;

        @Test
        void shouldSucceed() {
            org.assertj.core.api.Assertions.assertThat(pages).isSameAs(ThucydidesWebDriverSupport.getPages());
        }
    }

    @SerenityExtensionInnerTest
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    static class ConditionalClearingOfWebDriverTest {

        @Managed(clearCookies = ClearCookiesPolicy.Never) // and requires "serenity.restart.browser.for.each=FEATURE"
        WebDriver notClearingWebDriver;

        @Managed
        WebDriver clearingWebDriver;

        @BeforeEach
        void openPageForWebDrivers() throws InterruptedException {
            openExamplePage(notClearingWebDriver);
            openExamplePage(clearingWebDriver);
        }

        @Test
        @Order(1)
        void step1() {
            setExampleCookie(notClearingWebDriver);
            setExampleCookie(clearingWebDriver);
        }

        @Test
        @Order(2)
        void step2() {
            assertThatExampleCookieIn(this.notClearingWebDriver).as("cookie for notClearingWebDriver")
                    .isNotNull()
                    .extracting(Cookie::getValue).isEqualTo("cookieValue");
            assertThatExampleCookieIn(clearingWebDriver).as("cookie for clearingWebDriver ")
                    .isNull();
        }

        @NotNull
        private ObjectAssert<Cookie> assertThatExampleCookieIn(final WebDriver webDriver) {
            return org.assertj.core.api.Assertions.assertThat(webDriver.manage().getCookieNamed("cookieName"));
        }

        private void openExamplePage(final WebDriver webDriver) throws InterruptedException {
            final SeleniumHubPage seleniumHubPage = new Pages(webDriver).getPage(SeleniumHubPage.class);
            seleniumHubPage.open();
            Thread.sleep(100);
        }

        private void setExampleCookie(final WebDriver webDriver) {
            webDriver.manage().addCookie(new Cookie("cookieName", "cookieValue"));
        }
    }

    @SerenityExtensionInnerTest
    static class SettingsTest {

        @Managed
        WebDriver driver;

        @Steps
        SeleniumHubSteps seleniumHubSteps;

        @Test
        void shouldFirstSucceedThenFail() {
            seleniumHubSteps.stepThatOpensWikipedia();
            seleniumHubSteps.stepThatSucceeds();
            seleniumHubSteps.stepThatFails();
        }

    }

    static class SeleniumHubSteps extends ScenarioSteps {

        SeleniumHubPage page;

        @Step
        void stepThatOpensWikipedia() {
            page.open();
        }

        @Step
        void stepThatSucceeds() {
            org.assertj.core.api.Assertions.assertThat(page.getTitle()).isEqualTo(SeleniumHubPage.EXPECTED_TITLE);
        }

        @Step
        void stepThatFails() {
            org.assertj.core.api.Assertions.assertThat(page.getTitle()).isEqualTo("SomethingElse");
        }
    }

    @DefaultUrl("http://localhost:4444/wd/hub/static/resource/hub.html")
    static class SeleniumHubPage extends PageObject {

        static final String EXPECTED_TITLE = "WebDriver Hub";

        @WhenPageOpens
        public void waitUntilTitleAppears() {
            super.waitForTitleToAppear(EXPECTED_TITLE);
        }
    }
}
