package net.serenitybdd.junit5.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Somewhere to hold the Guice injector.
 * There might be a better way to do this.
 */
public class JUnitInjectors {

    private static Injector injector;

    public static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new JUnit5Module());
        }
        return injector;
    }
}
