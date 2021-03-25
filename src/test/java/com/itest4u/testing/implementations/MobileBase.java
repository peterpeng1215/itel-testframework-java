package com.itest4u.testing.implementations;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Attachment;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MobileBase implements com.itest4u.testing.ResourceInterfaces.IMobileBase {
    private boolean inited = false;
    private static AndroidDriver<MobileElement> driver;

    public MobileBase() throws MalformedURLException {
        System.out.println("init mobile base");
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("skipDeviceInitialization", true);
//        capabilities.setCapability("skipServerInstallation", true);
//        capabilities.setCapability("ignoreUnimportantViews", true);
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.sec.android.app.popupcalculator");
        capabilities.setCapability("appActivity", ".Calculator");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Override
    public void unlock() {
        driver.unlockDevice();
    }

    @Override
    public void reset() {
        if (inited) {
            driver.resetApp();
        }
        driver.unlockDevice();
        inited = true;

    }


    @Override
    public void click(By by) {
        driver.findElement(by).click();
        takeScreenshot(by.toString());
    }

    @Override
    public String getText(By by) {
        takeScreenshot(by.toString());
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return driver.findElement(by).getText();
    }

    @Override
    @Attachment(value = "Screenshot: {name}", type = "image/png")
    public byte[] takeScreenshot(String name) {
        // Take a screenshot as byte array and return
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    @Attachment(value = "Source: {name}", type = "application/xml")
    public byte[] getSource(String name) {
        // Take a screenshot as byte array and return
        return driver.getPageSource().getBytes();
    }


}
