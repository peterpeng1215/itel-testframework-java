package com.itest4u.testing;

import com.itest4u.testing.ResourceInterfaces.IGroup;
import com.itest4u.testing.ResourceInterfaces.IUser;
import com.itest4u.testing.implementations.RestTest;
import com.itest4u.testing.ResourceInterfaces.IPhone;
import com.itest4u.itel.annotations.Resource;
import com.itest4u.itel.annotations.Testcase;
import com.itest4u.itel.implementations.Execute;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;


@Testcase(
        id = "test"

)
public class Testcase1 {
    @Resource
    IPhone phone1;

    @Resource
    IPhone phone2;

    @Resource
    IGroup group;

    @Resource
    IUser user;

//    @Resource
//    ICalculatorApp calculatorApp;

    @Resource
    RestTest restTest;

    @BeforeMethod
    public void setup_method() {
        new Execute("test").run(() -> {
//            given().baseUri("http://192.168.1.143:8888/test/a/b").header("Content-Type", "application/json").body("").post();

//            restTest.test();
//            restTest.test2();
//            restTest.test3("1231");
//            phone1.offHook();
            System.out.println("===============");


        });
    }
//
//    @Step("When I input {inputStr}")
//    public void input(String inputStr) {
//        calculatorApp.input(inputStr);
//    }
//
//    @Step("I should get the result as : {result}")
//    public void checkResult(String result) {
//        calculatorApp.getSource("source");
//        String r = calculatorApp.getResult();
//        Allure.step(String.format("get result: %s ??? %s",r,result));
//        assert r.equals(result);
//    }
//    @BeforeMethod
//    public void setupMethod() {
//        calculatorApp.reset();
//
//    }


//    @Test(dataProvider = "exception", dataProviderClass = DP.class)
//    public void testCheckException(String input, String toast) {
//
//        input(input + "=");
//
//        assertEquals(calculatorApp.getToast(),toast, String.format("Toast message should be %s",toast));
////        assertEquals mobileBase.getToast().equals(toast);
//    }

//    @Test(dataProvider = "provider", dataProviderClass = DP.class,description = "test {0} == {output}")
//    public void testSearchAppium(String input, String output) {
//            calculatorApp.unlock();
//        input(input);
//        checkResult(output);
//
//
//    }

//    @Test
    public void test_name() throws Exception {

        new Execute("test").parallel(() -> {
            phone1.offHook();
            phone1.offHook();
        }, () -> {
            phone2.offHook();
            phone2.offHook();

        }).restore(() -> {
            user.remove("222","4545645");
        });
        new Execute("attempt").attempt((i) -> {
            if (i<4) {
                user.add("123","2342");
            } else {

                phone2.offHook();
                phone2.offHook();
            }

        },5);
        phone1.offHook();
        phone2.offHook();
        new Execute("test").parallel(() -> {
            phone1.offHook();
            phone1.offHook();
        }, () -> {
            phone2.offHook();
            phone2.offHook();
            user.add("123","2342");

        });
        phone1.dial(phone2.getNumber());
        phone1.onHook();
        phone2.onHook();
    }



//    @Test(dataProvider = "provider", dataProviderClass = DP.class,description = "test {0} == {output}")
//        public void test_value(String input, String output) {
////        test1.setValue("123");
//        new Execute("Add group").restore(() -> {
//            group.remove("group1");
//        }).run(() -> {
//            group.add("group1");
//        });
//
//
//        new Execute("Add user").restore(() -> {
//            user.remove("group1", "user1");
//        }).run(() -> {
//            user.add("group1","user1");
//        });
//    }
//
//    @Test
//    public void test_value2() {
////        test1.setValue("123");
//        new Execute("Add group").run(() -> {
//            group.add("group1");
//        }).restore(() -> {
//            group.remove("group1");
//
//        });
//
//
//        new Execute("Add user").run(() -> {
//            user.add("group1","user1");
//        }).restore(() -> {
//            user.remove("group1", "user1");
//        });
//    }
}
