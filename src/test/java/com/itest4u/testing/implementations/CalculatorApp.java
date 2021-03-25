package com.itest4u.testing.implementations;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;

public class CalculatorApp extends MobileBase implements com.itest4u.testing.ResourceInterfaces.ICalculatorApp {
    HashMap<String, By> IDs = new HashMap<>();

    public CalculatorApp() throws MalformedURLException {
        IDs.put("+", new MobileBy.ByAccessibilityId("Plus"));
        IDs.put("=", new MobileBy.ByAccessibilityId("Equal"));
        IDs.put("-", new MobileBy.ByAccessibilityId("Minus"));
        IDs.put("/", new MobileBy.ByAccessibilityId("Divide"));
        IDs.put("%", new MobileBy.ByAccessibilityId("Percentage"));
        IDs.put("(", new MobileBy.ByAccessibilityId("Brackets"));
        IDs.put(")", new MobileBy.ByAccessibilityId("Brackets"));
        IDs.put(".", new MobileBy.ByAccessibilityId("Dot"));
        IDs.put("~", new MobileBy.ByAccessibilityId("Plus/minus"));

        IDs.put("*", new MobileBy.ByAccessibilityId("Multiply"));
        IDs.put("x", new MobileBy.ByAccessibilityId("Multiply"));
        IDs.put("X", new MobileBy.ByAccessibilityId("Multiply"));
        IDs.put("toast", new By.ByXPath("//android.widget.Toast") {
        });
        IDs.put("result", new By.ById("com.sec.android.app.popupcalculator:id/txtCalc"));

        for (int i=0;i<9;i++) {
            IDs.put(String.valueOf(i), new MobileBy.ByAccessibilityId(String.valueOf(i)));
        }

    }
    @Override
    public void click(String c) {
        click(IDs.get(c));
    }
    @Override
    public void input(String inputStr) {
        Arrays.stream(inputStr.split("")).forEach(this::click);
    }

    @Override
    public String getResult() {
        click(IDs.get("="));
        return getText(IDs.get("result")).replace("−","-");
    }

    @Override
    public String getToast() {
        getSource("check");
        return getText(IDs.get("toast"));
    }

}
