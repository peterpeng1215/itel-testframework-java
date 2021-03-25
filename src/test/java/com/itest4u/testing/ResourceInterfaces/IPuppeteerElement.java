package com.itest4u.testing.ResourceInterfaces;


import com.itest4u.itel.annotations.ResourceInterface;
import com.itest4u.itel.interfaces.ICallbackable;

import java.util.Map;

@ResourceInterface
public interface IPuppeteerElement extends ICallbackable {
    public void type(String inputStr);
    public void click();
    public Map<String,String> getProperties();
    public String getProperty(String prop);
    public String innerHTML();
}
