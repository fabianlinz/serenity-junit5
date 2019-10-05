package net.serenitybdd.junit5.extension.testsupport;

import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta annotation for integrationtests for the serenity extensions.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SerenityTest
// to not have logs from the inner tests
@ExtendWith(QuietSerenityLoggingExtension.class)
public @interface SerenityExtensionTest {

}
