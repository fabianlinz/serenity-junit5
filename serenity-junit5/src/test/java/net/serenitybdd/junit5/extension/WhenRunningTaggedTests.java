package net.serenitybdd.junit5.extension;

import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionInnerTest;
import net.serenitybdd.junit5.extension.testsupport.SerenityExtensionTest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import net.thucydides.core.model.TestTag;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static net.thucydides.core.model.TestTag.DEFAULT_TAG_TYPE;

@SerenityExtensionTest
class WhenRunningTaggedTests {

    @Steps
    private JUnit5Steps junit5;

    @Test
    void the_outcome_should_have_tag_with_name_from_junit_annotation() {
        // when
        junit5.executesTestClass(SimpleTagWithName.class);

        // then
        junit5.shouldHaveTags(TestTag.withName("tagName").andType(DEFAULT_TAG_TYPE));
    }

    @SerenityExtensionInnerTest
    static class SimpleTagWithName {
        @Test
        @Tag("tagName")
        void test() {
        }
    }

    @Test
    void the_outcome_should_have_tag_with_name_and_type_from_junit_annotation() {
        // when
        junit5.executesTestClass(SerenitySyntaxForTagType.class);

        // then
        junit5.shouldHaveTags(
                TestTag.withName("myName1").andType("myType"),
                TestTag.withName("myName2").andType("myType"));
    }

    @SerenityExtensionInnerTest
    static class SerenitySyntaxForTagType {
        @Test
        @Tag("myType:myName1")
        @Tag("myType=myName2")
        void test() {
        }
    }

    @Test
    void the_outcome_should_have_tag_from_junit_annotation_on_test_class() {
        // when
        junit5.executesTestClass(TagOnClass.class);

        // then
        junit5.shouldHaveTags(
                TestTag.withName("tagNameOnClass").andType(DEFAULT_TAG_TYPE),
                TestTag.withName("tagNameOnClass2").andType("feature"));
    }

    @SerenityExtensionInnerTest
    @Tag("tagNameOnClass")
    @Tag("feature:tagNameOnClass2")
    static class TagOnClass {
        @Test
        void test() {
        }
    }

    @Test
    void the_outcome_should_have_tag_from_junit_annotation_in_meta_annotation() {
        // when
        junit5.executesTestClass(TagOnMetaAnnotation.class);

        // then
        junit5.shouldHaveTags(TestTag.withName("tagOnMetaAnnotation").andType(DEFAULT_TAG_TYPE));
    }

    @SerenityExtensionInnerTest
    @MetaAnnotation
    static class TagOnMetaAnnotation {
        @Test
        void test() {
        }
    }

    @Test
    void the_outcome_should_have_tags_from_junit_and_serenity_annotations() {
        // when
        junit5.executesTestClass(UsageOfSerenityAndJunit5TagsTogether.class);

        // then
        junit5.shouldHaveTags(
                TestTag.withName("junit5Tag").andType(DEFAULT_TAG_TYPE),
                TestTag.withName("junit5FeatureTag").andType("feature"),
                TestTag.withName("serenityTag").andType(DEFAULT_TAG_TYPE),
                TestTag.withName("serenityFeatureTag").andType("feature"));
    }

    @SerenityExtensionInnerTest
    static class UsageOfSerenityAndJunit5TagsTogether {
        @Test
        @Tag("junit5Tag")
        @Tag("feature:junit5FeatureTag")
        @WithTags({
                @WithTag("serenityTag"),
                @WithTag(name = "serenityFeatureTag")
        })
        // TODO until https://github.com/fabianlinz/serenity-junit5/issues/21 is fixed Serenity tag annotation are only considered on public methods
        public void test() {
        }
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @Tag("tagOnMetaAnnotation")
    public @interface MetaAnnotation {
    }
}
