package net.serenitybdd.junit5.extension.testsupport;

import net.thucydides.core.steps.BaseStepListener;
import net.thucydides.core.steps.StepEventBus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Auxiliary extension to provide access to the TestOutcomes for validation.
 */
public class TestOutcomeAccessorExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(final ExtensionContext context) {
        TestOutcomeAccessor.resetTestOutcomes();
    }

    @Override
    public void afterAll(@NotNull ExtensionContext context) {
        final BaseStepListener baseStepListener = StepEventBus.getEventBus().getBaseStepListener();
        TestOutcomeAccessor.registerTestOutcomes(baseStepListener.getTestOutcomes());
    }
}
