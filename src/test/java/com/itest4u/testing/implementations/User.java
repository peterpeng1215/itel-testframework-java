package com.itest4u.testing.implementations;

import com.itest4u.testing.ResourceInterfaces.IUser;
import io.qameta.allure.Allure;

public class User implements IUser {
    @Override
    public void add(String groupName, String name) {
        System.out.println("real implementation1 for user add");
        Allure.addAttachment("test","hello,world");
//        int x=1/0;
    }

    @Override
    public void remove(String groupName, String name) {
        System.out.println("real implementation1 for user remove");
    }
}
