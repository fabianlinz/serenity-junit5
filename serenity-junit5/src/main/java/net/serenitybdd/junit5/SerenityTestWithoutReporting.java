package net.serenitybdd.junit5;

import net.serenitybdd.junit5.extension.SerenityExtension;
import net.serenitybdd.junit5.extension.SerenityJUnitLifecycleAdapterExtension;
import net.serenitybdd.junit5.extension.SerenityStepExtension;
import net.serenitybdd.junit5.extension.page.SerenityPageExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Only purpose: simplify testing
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith({
        SerenityExtension.class,
        SerenityJUnitLifecycleAdapterExtension.class,
        SerenityPageExtension.class,
        SerenityStepExtension.class})
public @interface SerenityTestWithoutReporting {
}
