package com.itest4u.testing.ResourceInterfaces;

import com.itest4u.itel.annotations.HLF;
import com.itest4u.itel.annotations.ResourceInterface;

@ResourceInterface
public interface IUser {

    public void add(String groupName,String name);
    public void remove(String groupName, String name);
}
