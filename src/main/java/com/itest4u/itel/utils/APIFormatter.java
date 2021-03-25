package com.itest4u.itel.utils;

import com.itest4u.itel.annotations.API;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class APIFormatter {
    public static String methodTemplate(String instance, Method method, Object[] args) {
        Method[] methods =method.getDeclaringClass().getMethods();
        API apiAnnotation = method.getDeclaredAnnotation(API.class);
        Map<String, String> valuesMap = new HashMap<>();
        if (args != null) {
            Iterator<Object> itea = Arrays.stream(args).iterator();
            int i =0;
            for (Parameter p : method.getParameters()) {
                String value=String.valueOf(itea.next());
                valuesMap.put(p.getName(), value);
                valuesMap.put(String.valueOf(i), value);
                i++;
            }

        }
        if (apiAnnotation != null && !apiAnnotation.template().isEmpty()) {

            String templateString = apiAnnotation.template();
            StrSubstitutor sub = new StrSubstitutor(valuesMap);
            if (instance != null) {
                return instance + " : " + sub.replace(templateString);
            } else {
                return sub.replace(templateString);
            }

        } else {
            return  String.format("%s : %s %s", instance, method.getName(), Arrays.stream(args != null ? args : new Object[0]).map(Object::toString).collect(Collectors.joining(" ")));
        }
    };


}
