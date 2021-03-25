package com.itest4u.testing.ResourceInterfaces;


import com.itest4u.itel.annotations.ResourceInterface;

@ResourceInterface
public interface IGroup {
    public void add(String name);
    public void remove(String name);
}
