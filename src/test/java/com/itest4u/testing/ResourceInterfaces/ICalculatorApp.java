package com.itest4u.testing.ResourceInterfaces;

public interface ICalculatorApp extends IMobileBase {
    void click(String c);

    void input(String inputStr);

    String getResult();

    String getToast();
}
