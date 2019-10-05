package net.serenitybdd.junit5.extension.page;

import com.google.common.base.Preconditions;
import net.thucydides.core.annotations.DriverOptions;
import net.thucydides.core.annotations.WithDriver;

import java.lang.reflect.Method;
import java.util.Optional;

// Junit4: net.serenitybdd.junit.runners.TestMethodAnnotations
public final class TestMethodAnnotations {

    private final Method method;

    private TestMethodAnnotations(final Method method) {
        this.method = method;
    }

    public static TestMethodAnnotations forTest(final Method method) {
        return new TestMethodAnnotations(method);
    }


    public boolean isDriverSpecified() {
        return (method.getAnnotation(WithDriver.class) != null);
    }

    public String specifiedDriver() {
        Preconditions.checkArgument(isDriverSpecified() == true);
        return (method.getAnnotation(WithDriver.class).value());
    }

    public String driverOptions() {
        Preconditions.checkArgument(isDriverSpecified() == true);
        return Optional.ofNullable(method.getAnnotation(DriverOptions.class)).map(DriverOptions::value).orElse("");
    }

}
