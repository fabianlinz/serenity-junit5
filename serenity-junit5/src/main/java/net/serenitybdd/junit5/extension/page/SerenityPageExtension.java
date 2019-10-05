package net.serenitybdd.junit5.extension.page;

import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.serenitybdd.junit5.extension.SerenityExtension;
import net.serenitybdd.junit5.extension.SerenityStepExtension;
import net.thucydides.core.annotations.ClearCookiesPolicy;
import net.thucydides.core.annotations.ManagedWebDriverAnnotatedField;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.PageObjectDependencyInjector;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import net.thucydides.core.webdriver.WebdriverProxyFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getPages;
import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.initializeFieldsIn;

/**
 * Extension for webtest support in Serenity.
 *
 * Must be used before {{@link SerenityStepExtension}} as step injection/instantiation requires the correct {{@link Pages} to be set}.
 *
 * Note that in contrast to JUnit4 {{@link SerenityExtension}} always creates the
 * {@link net.thucydides.core.steps.BaseStepListener} without pages in order
 * to decouple the general setup from the setup for page support. The later is the task of this extension.
 */
public class SerenityPageExtension implements BeforeEachCallback {

    // Junit4: net.serenitybdd.junit.runners.SerenityRunner.buildAndConfigureListeners via net.serenitybdd.junit.runners.SerenityRunner.run
    // Junit4: net.thucydides.core.steps.BaseStepListener.BaseStepListener(java.io.File, net.thucydides.core.pages.Page
    @Override
    public void beforeEach(final ExtensionContext extensionContext) throws Exception {
        final TestConfiguration testConfiguration = TestConfiguration.forClass(extensionContext.getRequiredTestClass()).withSystemConfiguration(WebDriverConfiguredEnvironment.getDriverConfiguration());
        if (testConfiguration.isAWebTest()) {
            applyTestClassOrTestMethodSpecificWebDriverConfiguration(extensionContext);
            initializeFieldsIn(extensionContext.getRequiredTestInstance());
            injectPageObjectIntoTest(extensionContext.getRequiredTestInstance());

            prepareBrowserForTest(extensionContext);
        }
    }

    private void applyTestClassOrTestMethodSpecificWebDriverConfiguration(final ExtensionContext extensionContext) {
        ThucydidesWebDriverSupport.clearDefaultDriver();

        final Optional<ExplicitWebDriverConfiguration> explicitWebDriverConfiguration = explicitWebDriverConfiguration(extensionContext);
        explicitWebDriverConfiguration.ifPresent(it -> {
            final String value = it.getTestSpecificDriver();
            final Consumer<String> consumer = ThucydidesWebDriverSupport::useDefaultDriver;
            notEmpty(value).ifPresent(consumer);
            notEmpty(it.getDriverOptions()).ifPresent(ThucydidesWebDriverSupport::useDriverOptions);
            workaroundForOtherwiseIgnoredWebDriverOptions();
        });
    }

    private Optional<String> notEmpty(final String value) {
        return ofNullable(value).filter(StringUtils::isNotEmpty);
    }

    /**
     * ThucydidesWebDriverSupport#getDriver creates the web driver used for the page factory (Pages). For the creation of the
     * the web driver options are only considered if a driver name is set. On the other hand TestCaseAnnotations#injectDrivers
     * which injects the driver into @Managed web driver fields of the test class, considers the options the @Managed annotation.
     * So if e.g. @Managed(options = "testOption") is used the web driver injected into the field has the defined options. But
     * the web driver injected into pages and steps does not have this option set. This is also the case in Junit4. Probably this
     * is a corner case that is usually not applied in real life. #
     *
     * options:
     * a) keep it as it is (consistent with Junit4)
     * b) change ThucydidesWebDriverSupport#getDriver to consider options also when no driver is set
     * c) consider this unintended usage  => e.g. fail with an exception
     *
     * This method is basically a workaround for b until the serenity-core implementation is adjusted.
     */
    private void workaroundForOtherwiseIgnoredWebDriverOptions() {
        if (!ThucydidesWebDriverSupport.getDefaultDriverType().isPresent() && ThucydidesWebDriverSupport.getDefaultDriverOptions().isPresent()) {
            ThucydidesWebDriverSupport.useDefaultDriver(WebDriverConfiguredEnvironment.getDriverConfiguration().getDriverType().name()); // analog to net.thucydides.core.annotations.TestCaseAnnotations.configuredDriverType
        }
    }

    private Optional<ExplicitWebDriverConfiguration> explicitWebDriverConfiguration(final ExtensionContext extensionContext) {
        final Method testMethod = extensionContext.getRequiredTestMethod();
        final Class<?> requiredTestClass = extensionContext.getRequiredTestClass();
        if (hasExplicitWebDriverConfigurationOnTestMethod(testMethod)) {
            final String testSpecificDriver = TestMethodAnnotations.forTest(testMethod).specifiedDriver();
            final String driverOptions = TestMethodAnnotations.forTest(testMethod).driverOptions();
            return explicitWebDriverConfiguration(testSpecificDriver, driverOptions);
        } else if (hasExplicitWebDriverConfigurationOnTestClass(requiredTestClass)) {
            // CAUTION: unstable behaviour in case of multiple @Managed fields
            // findFirstAnnotatedField seems to be misleading. It finds "a" annotated field, because the ordering is not defined.
            // If there are multiple @Managed fields it is not clear which one should be used to define the default web driver used for the Steps.
            // So either A) this is an invalid use case that should be detected and rejected with an exception OR B) the default that would be used otherwise should be used
            // If net.thucydides.core.annotations.PatchedManagedWebDriverAnnotatedField.findAnnotatedFields would be public this case could at least be detected.
            // Note that even this block would be removed net.thucydides.core.annotations.TestCaseAnnotations#injectDrivers would still set a default but without explicitly
            // updating the PageFactory (which will happen as a side-effect to ThucydidesWebDriverSupport#getDriver calls.
            final ManagedWebDriverAnnotatedField firstAnnotatedField = ManagedWebDriverAnnotatedField.findFirstAnnotatedField(requiredTestClass);
            return explicitWebDriverConfiguration(firstAnnotatedField.getDriver(), firstAnnotatedField.getOptions());
        }

        return empty();
    }

    private void prepareBrowserForTest(final ExtensionContext extensionContext) {
        PatchedManagedWebDriverAnnotatedField.findAnnotatedFields(extensionContext.getRequiredTestClass()).stream()
                .filter(it -> ClearCookiesPolicy.BeforeEachTest.equals(it.getClearCookiesPolicy()))
                .map(it -> it.getValue(extensionContext.getRequiredTestInstance()))
                .forEach(WebdriverProxyFactory::clearBrowserSession);

        /* JUNIT4 analog impl:
        private void prepareBrowserForTest(TestConfiguration theTest) {
            if (theTest.shouldClearTheBrowserSession()) {
                // CAUTION: unstable behaviour in case of multiple @Managed fields
                // What is the expected behaviour in case of multiple @Managed fields? The current implementation picks an arbitrary @Managed field to decide
                // if a web driver instance should be cleared. It seems iterating over all @Manager fields and for those configured to clear the session, do so.
                // If net.thucydides.core.annotations.PatchedManagedWebDriverAnnotatedField.findAnnotatedFields would be public one could iterate easily
                // over the fields.
                WebdriverProxyFactory.clearBrowserSession(ThucydidesWebDriverSupport.getWebdriverManager().getCurrentDriver());
            }
        }
         */
    }

    @NotNull
    private Optional<ExplicitWebDriverConfiguration> explicitWebDriverConfiguration(final String testSpecificDriver, final String driverOptions) {
        return of(new ExplicitWebDriverConfiguration(testSpecificDriver, driverOptions));
    }

    private boolean hasExplicitWebDriverConfigurationOnTestClass(final Class<?> requiredTestClass) {
        return ManagedWebDriverAnnotatedField.hasManagedWebdriverField(requiredTestClass);
    }

    private boolean hasExplicitWebDriverConfigurationOnTestMethod(final Method testMethod) {
        return TestMethodAnnotations.forTest(testMethod).isDriverSpecified();
    }

    private void injectPageObjectIntoTest(final Object testClass) {
        new PageObjectDependencyInjector(getPages()).injectDependenciesInto(testClass);
    }

    public static class ExplicitWebDriverConfiguration {

        private final String testSpecificDriver;
        private final String driverOptions;

        public ExplicitWebDriverConfiguration(final String testSpecificDriver, final String driverOptions) {
            this.testSpecificDriver = testSpecificDriver;
            this.driverOptions = driverOptions;
        }

        public String getTestSpecificDriver() {
            return testSpecificDriver;
        }

        public String getDriverOptions() {
            return driverOptions;
        }

    }

}
