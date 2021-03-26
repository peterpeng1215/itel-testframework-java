package com.itest4u.itel.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itest4u.itel.annotations.API;
import com.itest4u.itel.annotations.ResourceInterface;
import com.itest4u.itel.interfaces.IRPCResult;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import okhttp3.MediaType;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Array;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static io.restassured.RestAssured.given;

public class GenericResourceAgent {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String remoteObj;
    private Object instance;

//    public GenericResourceAgent(Object instance) {
////        this.instance = instance;
//        new GenericResourceAgent(instance,null);
//    }
//
//    public GenericResourceAgent(Object instance, String remoteObj) {
//        this.instance = instance;
//
//        if (remoteObj != null) {
//            try {
//                URL url = new URL(remoteObj);
//                BaseURL = remoteObj;
//                IRPCResult result = base().post().as(IRPCResult.class);
//                this.remoteObj = result.getId();
//            } catch (MalformedURLException e) {
//                this.remoteObj = remoteObj;
//                e.printStackTrace();
//            }
//
//        }
//
//    }

    public GenericResourceAgent(Object instance, String urlStr) {
        this(instance, urlStr, null);
    }

    public GenericResourceAgent(Object instance, String urlStr, String remoteObjStr) {
        this.instance = instance;
        BaseURL = urlStr;
        if (remoteObjStr == null && urlStr != null) {
            try {
                URL url = new URL(BaseURL);
                IRPCResult result = base().post().as(IRPCResult.class);
                this.remoteObj = result.getId();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } else if (remoteObjStr != null && urlStr != null) {
            this.remoteObj = remoteObjStr;
            this.BaseURL = urlStr;

        }
    }


    String BaseURL = null;
    HashMap<String, String> custom_headers = new HashMap<>();

    private RequestSpecification base(boolean... args) {
//        filter(new AllureRestAssured() ).
        RequestSpecification myBase = given().baseUri(BaseURL).header("Content-Type", "application/json");
        if (args.length > 0) {
            for (Map.Entry<String, String> h : custom_headers.entrySet()) {
                myBase.header(h.getKey(), h.getValue());
            }
        }
        return myBase;
    }

    public Object invoke(Method method, Object[] args) throws Exception {
        API apiAnnotation = method.getAnnotation(API.class);
        boolean isArrayType = false;
        boolean isListType = false;
        Class<?> returnType = method.getReturnType();

        if (returnType.isArray()) {
            isArrayType = true;
            returnType = returnType.getComponentType();
        } else if (method.getGenericReturnType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) (method.getGenericReturnType());

            isArrayType = true;
            isListType = true;
            returnType = (Class<?>) pt.getActualTypeArguments()[0];
        } else if (Arrays.stream(returnType.getInterfaces()).anyMatch(x -> x==List.class)) {

            isArrayType = true;
            isListType = true;

            returnType = (Class<?>) ((ParameterizedTypeImpl) (returnType.getGenericInterfaces()[0])).getActualTypeArguments()[0];
        }
        ResourceInterface returnAnnotation = returnType.getAnnotation(ResourceInterface.class);
        String methodName = method.getName();
        if (apiAnnotation != null) {
            if (!apiAnnotation.apiName().isEmpty()) {
                methodName = apiAnnotation.apiName();
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseOptions r;
        if (args == null) {
            args = new ArrayList<Object>().toArray();

        }
        RequestSpecification rs = base().body(args);
        if (returnAnnotation != null) {
            rs = rs.queryParam("isInstance", true);
            if (isArrayType) {
                rs = rs.queryParam("isArray", true);
            }
        }

        r = rs.post(remoteObj + "/actions/" + methodName);
        if (r.getStatusCode() >= 400) {

            Allure.step("Exception Log", () -> {
                Allure.addAttachment("Exception Log", "");
                throw new Exception("Execution failed");
            });

            throw new Exception("Execution failed");

        }
        if (method.getReturnType().equals(void.class)) {
            return null;
        } else if (returnAnnotation != null) {
            List<IRPCResult> results = new ArrayList<>();
            if (isArrayType) {
                results = Arrays.asList(r.getBody().as(IRPCResult[].class).clone());
            } else {
                IRPCResult ret = r.getBody().as(IRPCResult.class);
                results.add(ret);
            }

            String finalMethodName = methodName;
            Class<?> finalReturnType = returnType;

            int length = results.size();
            Object resultsArray = Array.newInstance(returnType, length);

            for (int i = 0; i < length; i++) {
                IRPCResult result =results.get(i);
                if (result.getId() == null) {
                    return null;
                }
                DynamicInvocationHandler invocationHandler;
                try {
                    invocationHandler = new DynamicInvocationHandler(
                            instance + "_" + finalMethodName, result.getId(), this.BaseURL
                    );
                    Array.set(resultsArray, i, Proxy.newProxyInstance(
                            finalReturnType.getClassLoader(),
                            new Class[]{finalReturnType},
                            invocationHandler));
                } catch (IllegalAccessException | InstantiationException | MalformedURLException e) {
                    e.printStackTrace();
                }

            }

            if (isArrayType) {
                if (isListType) {
                    List<Object> aaa = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        aaa.add(Array.get(resultsArray,i));
                    }

                    return aaa;
                } else {
                    return resultsArray;
                }

            } else {
                return Array.get(resultsArray,0);
            }

        }
        try {
            return objectMapper.readValue(r.getBody().asString(), method.getReturnType());
        } catch (Exception e) {
            Allure.addAttachment("exception", e.getMessage());
            return null;
        }

    }
    @Override
    protected void finalize() throws Throwable
    {

        try {
            System.out.println("finalizing...");
            base().delete(remoteObj);

        } catch (Exception e) {
            System.out.println("finalized failed" + e);

            e.printStackTrace();

        }
    }
}
