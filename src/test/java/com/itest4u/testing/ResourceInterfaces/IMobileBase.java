package com.itest4u.testing.ResourceInterfaces;

import io.qameta.allure.Attachment;
import org.openqa.selenium.By;

public interface IMobileBase {
    void unlock();

    void reset();

    void click(By by);

    String getText(By by);

    @Attachment(value = "Screenshot: {name}", type = "image/png")
    byte[] takeScreenshot(String name);

    @Attachment(value = "Source: {name}", type = "application/xml")
    byte[] getSource(String name);
}
