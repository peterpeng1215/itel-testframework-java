package com.itest4u.itel;

import com.itest4u.itel.annotations.Resource;
import com.itest4u.itel.implementations.Callbackable;
import com.itest4u.itel.implementations.DynamicInvocationHandler;
import com.itest4u.itel.implementations.Execute;
import com.itest4u.itel.interfaces.ICallback4Callbackable;
import com.itest4u.itel.utils.APIFormatter;
import io.qameta.allure.Allure;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class TestNGListener implements
        IInvokedMethodListener, ISuiteListener,
        ITestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestNGListener.class);

    public TestNGListener() throws IOException {

    }

    @Override
    public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult) {
        final ITestNGMethod testMethod = method.getTestMethod();
        final ITestContext context = testResult.getTestContext();


        Object obj = testMethod.getInstance();
        Class realClass = testMethod.getRealClass();

        for (Field field : realClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(obj) == null && field.getAnnotation(Resource.class) != null) {
                    if (field.getType().isInterface()) {

                        Reflections reflections = new Reflections(testMethod.getTestClass().getRealClass().getPackage().getName());
                        if (reflections.getSubTypesOf((Class<?>) field.getType()).isEmpty()) {
                            reflections = new Reflections(TestNGListener.class.getPackage().getName());
                        }
                        DynamicInvocationHandler invocationHandler = new DynamicInvocationHandler(
                                field, reflections.getSubTypesOf((Class<Object>) field.getType()), field.getType()
                        );
                        Object proxyInstance = Proxy.newProxyInstance(
                                field.getType().getClassLoader(),
                                new Class[]{field.getType()},
                                invocationHandler);
                        field.set(obj, proxyInstance);

                    } else {
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(field.getType());

                        enhancer.setCallback((MethodInterceptor) (o, m, args, proxy) -> {
                            String stepName = APIFormatter.methodTemplate(field.getName(),m,args);
                            AtomicReference<Object> result = new AtomicReference<>();


                            Allure.step(stepName,() -> {
                                result.set(proxy.invokeSuper(o, args));
                                if (o instanceof Callbackable) {
                                    Field f = Callbackable.class.getDeclaredField("callbackable");
                                    f.setAccessible(true);
                                    ICallback4Callbackable  callbackable=(ICallback4Callbackable)f.get(o);
                                    if (callbackable != null) {
                                        callbackable.call(m.getName(),result.get());
                                    }
                                }
                            });


                            return result.get();
                        });
                        field.set(obj, enhancer.create());
                    }
                }

            } catch (IllegalAccessException | InstantiationException | MalformedURLException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        final ITestNGMethod testMethod = method.getTestMethod();
        final ITestContext context = testResult.getTestContext();
        if (!Execute.isRestoreStackEmpty()) {
            Allure.step("Environment restoration", Execute::popRestore);
        }

    }


    @Override
    public void onStart(final ISuite suite) {

        Configuration.load(Paths.get(suite.getXmlSuite().getFileName()).getParent().toString()+"/configuration.properties");
    }

    @Override
    public void onStart(final ITestContext context) {


    }

}
