package com.itest4u.itel.implementations;

import com.itest4u.itel.Configuration;
import com.itest4u.itel.annotations.API;
import com.itest4u.itel.interfaces.ICallback4Callbackable;
import com.itest4u.itel.utils.APIFormatter;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Set;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DynamicInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            DynamicInvocationHandler.class);
    private final String resourceName;
    private Set<Class<?>> classes;
    private final Object instance;
    private boolean isRealInstance = false;
    private ICallback4Callbackable callbackable;

    private String getRemoteURL(Class<?> clazz) {
        String propUrl =  clazz.getSimpleName() + ".baseurl";
        String result = null;
        result = Configuration.getProperty(propUrl);
        if (result != null) {
            return result;
        } else {
            return Configuration.getProperty("baseurl");
        }
    }

    public DynamicInvocationHandler(Field field, Set<Class<?>> classes, Class<?> clazz) throws IllegalAccessException, InstantiationException, MalformedURLException {
        resourceName = field.getName();
        if (!classes.isEmpty()) {
            Class clz = classes.stream().findFirst().get();

            instance = clz.newInstance();
            if (Arrays.stream(clz.getMethods()).anyMatch(m -> m.getName().equals("invoke"))) {

            } else {
                isRealInstance = true;
            }

        } else {
            instance = new GenericResourceAgent(resourceName, getRemoteURL(clazz));
        }
    }

    public DynamicInvocationHandler(String resourceName, String remoteObj, String urlStr) throws IllegalAccessException, InstantiationException, MalformedURLException {

        this.resourceName = resourceName;
        instance = new GenericResourceAgent(resourceName, urlStr,remoteObj);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.getName().equals("setCallback")) {
            callbackable = (ICallback4Callbackable) args[0];
            return null;
        }
        boolean ifGetter = false;
        API apiAnnotation = method.getAnnotation(API.class);
        if (apiAnnotation!=null) {
            ifGetter = !apiAnnotation.ifDisplay();
        }
//        if (method.getName().startsWith("get")) {
//            Class<?> cls = method.getDeclaringClass();
//            ifGetter = true;
//        }
        String stepName = APIFormatter.methodTemplate(resourceName,method,args);
        assert instance != null;
        AtomicReference<Object> result = new AtomicReference<>();
        if (isRealInstance) {
            if (ifGetter) {
                result.set(method.invoke(instance, args));
            } else {
                Allure.step(stepName, () -> {
                    result.set(method.invoke(instance, args));
                    if (callbackable != null) {
                        callbackable.call(method.getName(),result.get());
                    }

                });

            }

        } else {
            if (ifGetter) {
                result.set(((GenericResourceAgent) instance).invoke(method, args));

            } else {
                Allure.step(stepName, () -> {

                    result.set(((GenericResourceAgent) instance).invoke(method, args));
                    if (callbackable != null) {
                        callbackable.call(method.getName(),result.get());
                    }

                });

            }

        }
        return result.get();

    }
}
