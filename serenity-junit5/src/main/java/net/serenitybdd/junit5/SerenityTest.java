package net.serenitybdd.junit5;

import net.serenitybdd.junit5.extension.SerenityReportExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SerenityTestWithoutReporting
@ExtendWith(SerenityReportExtension.class)
public @interface SerenityTest {
}
