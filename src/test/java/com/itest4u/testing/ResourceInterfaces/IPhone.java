package com.itest4u.testing.ResourceInterfaces;


import com.itest4u.itel.annotations.API;
import com.itest4u.itel.annotations.ResourceInterface;

@ResourceInterface
public interface IPhone {
    public String number = "12345";

    public String getNumber();

    @API(template = " picks up")
    public void offHook();
    @API(template = " dials ${number}")
    public void dial(String number);
    @API(template = " hangs up")
    public void onHook();
}
