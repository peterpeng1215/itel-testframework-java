package com.itest4u.itel.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
public @interface Testcase {

    Map<String, String> attributes = null;
    String id();

}
