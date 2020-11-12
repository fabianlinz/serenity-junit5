package net.serenitybdd.junit5.extension.testsupport;

import net.thucydides.core.guice.Injectors;
import net.thucydides.core.logging.LoggingLevel;
import net.thucydides.core.util.EnvironmentVariables;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;

public class QuietSerenityLoggingExtension implements InvocationInterceptor {

    private static final String SERENITY_LOGGING = "serenity.logging";

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        final EnvironmentVariables
                environmentVariables = Injectors.getInjector().getProvider(EnvironmentVariables.class).get();
        final String originalValue = environmentVariables.getProperty("serenity.logging");
        try {
            environmentVariables.setProperty(SERENITY_LOGGING, LoggingLevel.NONE.name());
            invocation.proceed();
        } finally {
            if (originalValue != null) {
                environmentVariables.setProperty(SERENITY_LOGGING, originalValue);
            } else {
                environmentVariables.clearProperty(SERENITY_LOGGING);
            }
        }
    }

}
