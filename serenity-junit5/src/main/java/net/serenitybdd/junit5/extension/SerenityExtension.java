package net.serenitybdd.junit5.extension;

import com.google.inject.Key;
import net.serenitybdd.core.injectors.EnvironmentDependencyInjector;
import net.serenitybdd.junit5.counter.TestCounter;
import net.serenitybdd.junit5.guice.JUnitInjectors;
import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.Listeners;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepListener;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static net.serenitybdd.core.environment.ConfiguredEnvironment.getConfiguration;
import static net.thucydides.core.steps.StepEventBus.getEventBus;

// Junit4: net.serenitybdd.junit.runners.SerenityRunner.initStepEventBus
// Junit4: net.serenitybdd.junit.runners.SerenityRunner.initListeners
// (no separate net.serenitybdd.junit.runners.SerenityRunner.initListenersUsing as pages will be configured via net.serenitybdd.junit.extension.page.SerenityPageExtension)
public class SerenityExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

    @Override
    public void beforeAll(final ExtensionContext extensionContext) {
        getEventBus().clear();

        registerListenersOnEventBus(
                createBaseStepListener(),
                Listeners.getLoggingListener(),
                testCountListener());
    }

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        injectEnvironmentVariablesInto(extensionContext.getRequiredTestInstance());
    }

    @Override
    // JUnit4: net.serenitybdd.junit.runners.SerenityRunner.run
    public void afterAll(final ExtensionContext extensionContext) {
        StepEventBus.getEventBus().dropAllListeners();
    }

    private BaseStepListener createBaseStepListener() {
        return Listeners.getBaseStepListener().withOutputDirectory(getConfiguration().getOutputDirectory());
    }

    private void registerListenersOnEventBus(final StepListener... stepListeners) {
        for (StepListener currentStepListener : stepListeners) {
            getEventBus().registerListener(currentStepListener);
        }
    }

    private StepListener testCountListener() {
        return JUnitInjectors.getInjector().getInstance(Key.get(StepListener.class, TestCounter.class));
    }

    private void injectEnvironmentVariablesInto(final Object testCase) {
        new EnvironmentDependencyInjector().injectDependenciesInto(testCase);
    }
}
