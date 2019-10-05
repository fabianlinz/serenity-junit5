package net.serenitybdd.junit5.extension.testsupport;

import net.thucydides.core.steps.StepEventBus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class QuietSerenityReportExtension implements AfterAllCallback {

    @Override
    public void afterAll(@NotNull ExtensionContext context) {
        StepEventBus.getEventBus().reset();
    }

}
