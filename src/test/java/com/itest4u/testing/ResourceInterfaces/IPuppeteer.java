package com.itest4u.testing.ResourceInterfaces;


import com.itest4u.itel.annotations.API;
import com.itest4u.itel.annotations.ResourceInterface;
import com.itest4u.itel.interfaces.ICallbackable;

import java.util.Collection;
import java.util.List;

@ResourceInterface
public interface IPuppeteer extends ICallbackable {

    @API(apiName = "goto")
    public void visit(String url);
    public IHistoryItem[] getHistory();

    @API(ifDisplay = false)
    public String screenshot(IScreenshotOption option);

    @API(apiName = "keyboard.press")
    public void keyPress(String key);

    @API(apiName = "mouse.move")
    public void mouseMove(int x, int y);


    public void type(String selector, String inputStr);
    public void click(String selector);
    public void waitForSelector(String selector);
    public void waitForTimeout(long milliseconds);

    public IPuppeteerElement $(String selector);

    @API(apiName = "$")
    public IPuppeteerElement findElement(String selector);

    @API(apiName = "$$")
    public List<IPuppeteerElement> findElements(String selector);
    @API(apiName = "$$")
    public IPuppeteerElementCollection findElements2(String selector);
    @API(apiName = "$$")
    public IPuppeteerElement[] findElements3(String selector);

    public IPuppeteerElement[] $$(String selector);
    public void close();
}

