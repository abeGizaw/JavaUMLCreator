package domain.diagramconverter.ClassUmlMockTestClasses.SpecialClassTesting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyOtherAnnotation {
    String name();
    int age() default 1;
}
