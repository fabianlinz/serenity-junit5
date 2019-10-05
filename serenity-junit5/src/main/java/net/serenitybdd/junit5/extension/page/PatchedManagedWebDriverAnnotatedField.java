package net.serenitybdd.junit5.extension.page;

import net.thucydides.core.annotations.ClearCookiesPolicy;
import net.thucydides.core.annotations.Fields;
import net.thucydides.core.annotations.InvalidManagedWebDriverFieldException;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.webdriver.WebDriverFacade;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public class PatchedManagedWebDriverAnnotatedField {

    private static final String NO_ANNOTATED_FIELD_ERROR 
                                    = "No WebDriver field annotated with @Managed was found in the test case.";

    private final Field field;

    /**
     * Find the first field in the class annotated with the <b>Managed</b> annotation.
     */
    static Optional<PatchedManagedWebDriverAnnotatedField> findOptionalAnnotatedField(final Class<?> testClass) {

        try {
            return fieldsIn(testClass)
                    .stream()
                    .filter(PatchedManagedWebDriverAnnotatedField::isFieldAnnotated)
                    .map(PatchedManagedWebDriverAnnotatedField::new)
                    .findFirst();
        } catch(NoSuchElementException e) {
            return Optional.empty();
        }
    }

    /**
     * Find the first field in the class annotated with the <b>Managed</b> annotation.
     */
    public static PatchedManagedWebDriverAnnotatedField findFirstAnnotatedField(final Class<?> testClass) {

        Optional<PatchedManagedWebDriverAnnotatedField> optionalField = findOptionalAnnotatedField(testClass);
        if (optionalField.isPresent()) {
            return optionalField.get();
        } else {
            throw new InvalidManagedWebDriverFieldException(NO_ANNOTATED_FIELD_ERROR);
        }
    }

    public static List<PatchedManagedWebDriverAnnotatedField> findAnnotatedFields(final Class<?> testClass) {

        return Fields.of(testClass).allFields()
                .stream()
                .filter(PatchedManagedWebDriverAnnotatedField::isFieldAnnotated)
                .map(PatchedManagedWebDriverAnnotatedField::new)
                .collect(Collectors.toList());
    }

    public static boolean hasManagedWebdriverField(final Class<?> testClass) {

        try {
            return fieldsIn(testClass)
                    .stream()
                    .anyMatch(PatchedManagedWebDriverAnnotatedField::isFieldAnnotated);
        } catch(NoSuchElementException e) {
            return false;
        }
    }

    private static boolean isFieldAnnotated(final Field field) {
        return (fieldIsAnnotatedCorrectly(field) && fieldIsRightType(field));
    }

    private static boolean fieldIsRightType(final Field field) {
        return (WebDriverFacade.class.isAssignableFrom(field.getType()) ||
        		field.getType().isAssignableFrom(WebDriver.class));
    }

    private static boolean fieldIsAnnotatedCorrectly(final Field field) {
        return (field.getAnnotation(Managed.class) != null);
    }

    private PatchedManagedWebDriverAnnotatedField(final Field field) {
        this.field = field;
    }

    public void setValue(final Object testCase, final WebDriver manageDriver) {
        try {
            field.setAccessible(true);
            field.set(testCase, manageDriver);
        } catch (IllegalAccessException e) {
            throw new InvalidManagedWebDriverFieldException("Could not access or set web driver field: "
                         + field 
                         + " - is this field public?", e);
        }
    }

    public WebDriver getValue(final Object testCase) {
        try {
            field.setAccessible(true);
            return (WebDriver) field.get(testCase);
        } catch (IllegalAccessException e) {
            throw new InvalidManagedWebDriverFieldException("Could not access or set web driver field: "
                    + field
                    + " - is this field public?", e);
        }
    }

    private static Set<Field> fieldsIn(Class clazz) {
        return Fields.of(clazz).allFields();
    }

    public boolean isUniqueSession() {
        return field.getAnnotation(Managed.class).uniqueSession();
    }

    ClearCookiesPolicy getClearCookiesPolicy() {
        return field.getAnnotation(Managed.class).clearCookies();
    }

    public String getDriver() {
        return field.getAnnotation(Managed.class).driver();
    }

    public String getOptions() {
        return field.getAnnotation(Managed.class).options();
    }

    public String getName() { return field.getName(); }
}
