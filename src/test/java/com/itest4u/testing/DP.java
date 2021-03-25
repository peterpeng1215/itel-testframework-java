package com.itest4u.testing;
import org.testng.annotations.DataProvider;

public class DP {

    @DataProvider(name = "provider")
    public Object[][] provide() throws Exception {

        return new Object[][] { { "1+2","3"},{ "5*6","30"} ,{ "5*7","35"}, {"2*(2+3)","10"},{"1.2/0.4","3"},{"1.2+~1.3","-0.1"}};
    }

    @DataProvider(name = "exception")
    public Object[][] exception() throws Exception {
        
        return new Object[][] { { "1/0","Cannot divide by zero."}};
    }

}

