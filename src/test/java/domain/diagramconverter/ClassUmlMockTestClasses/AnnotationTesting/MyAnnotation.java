package domain.diagramconverter.ClassUmlMockTestClasses.AnnotationTesting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // This is correct for applying to classes, interfaces, etc.
public @interface MyAnnotation {
    String value();
    int priority() default 1;
    String[] tags() default {};
}
