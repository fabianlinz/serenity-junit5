package starter;

import net.serenitybdd.junit5.SerenityTest;
import net.thucydides.core.annotations.WithTag;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@SerenityTest
@Tag("tagOnClassLevel")
class Junit5TagTest {

    @Test
    @Tag("tag1")
    void shouldHaveASimpleTag() {
    }

    @Test
    @Tag("myType:myName1")
    @Tag("myType=myName2")
    void shouldAllowSerenitySyntaxForTagType() {
    }

    @Test
    @MetaAnnotation
    void shouldConsiderTagFromMetaAnnotation() {
    }

    @Test
    @Tags({
            @Tag("tag1"),
            @Tag("tag2")
    })
    void shouldHaveTwoSimpleTagsViaGroup() {
    }

    @Test
    @Tag("junit5Tag")
    @WithTag("serenityTag")
    // TODO until https://github.com/fabianlinz/serenity-junit5/issues/21 is fixed Serenity tag annotation are only considered on public methods
    public void shouldAllowUsageOfSerenityAndJunit5TagsTogether() {
        // does not make sense though
    }

    @Test
    @Tag("feature:junit5FeatureTag")
    void junit5FeatureTag() {
        // does not make sense though
    }

    @Test
    @WithTag(name = "serenityFeatureTag")
    public void serenityFeatureTag() {
        // does not make sense though
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @Tag("tagOnMetaAnnotation")
    public @interface MetaAnnotation {
    }
}
