package com.itest4u.itel.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyAnnotations.class)
public @interface MyAnnotation {
    String name() default "undefined";

}
