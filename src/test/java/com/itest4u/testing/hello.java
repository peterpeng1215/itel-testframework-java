package com.itest4u.testing;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

class PersonService {
    public String sayHello(String name) {
        return "Hello " + name;
    }

    public Integer lengthOfName(String name) {
        return name.length();
    }
}

public class hello {
    public static void main(String[] a) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        PersonService personService = new PersonService();

        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            System.out.println(method.getName());
            Arrays.stream(args).forEach(x -> System.out.println(x));

            return proxy.invokeSuper(obj, args);
        });
        PersonService proxy = (PersonService) enhancer.create();

        String res = proxy.sayHello(null);

        System.out.println(res);
    }
}
