package com.itest4u.testing;

import com.itest4u.itel.implementations.Execute;
import com.itest4u.itel.annotations.Resource;
import com.itest4u.testing.ResourceInterfaces.IPuppeteer;
import com.itest4u.testing.ResourceInterfaces.IPuppeteerElement;
import com.itest4u.testing.ResourceInterfaces.IPuppeteerElementCollection;
import com.itest4u.testing.ResourceInterfaces.IScreenshotOption;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertEquals;


public class Testcase_RestAPISample2 {
    @Resource
    IPuppeteer puppeteer;


    @Attachment(value = "Page screenshot for {url}", type = "image/png")
    public byte[] saveScreenshot(String url) {
        return Base64.decodeBase64(puppeteer.screenshot(new IScreenshotOption()));
    }

    @Step("Visit {url}")
    public void visit(String url) {
        new Execute("visit "+url).run(() -> {
            puppeteer.visit(url);
                }

        ).restore(() -> {
            puppeteer.setCallback(null);
            puppeteer.close();
        }  );
    }

    @Test
    public void mytestcase() {
        puppeteer.setCallback((m,r) -> {
            if (m.equals("click")) {
                saveScreenshot(m);
            }
//            saveScreenshot(m);
        });

        visit(DS.URL);
//        puppeteer.waitForSelector("#ac-gn-link-search");
//        puppeteer.click("#ac-gn-link-search");
        IPuppeteerElement element = puppeteer.$("#ac-gn-link-search");
//
//        assert  element!=null;
////        element.setCallback((m,r) -> {
////            saveScreenshot(m);
////        });
        element.click();
        puppeteer.type("#ac-gn-searchform-input","iphone 12\n");
        puppeteer.waitForSelector(".as-search-results-count");
        List<IPuppeteerElement> pes =  puppeteer.findElements(".as-explore-product");

//        pes.forEach(e -> System.out.println(e.innerHTML()));
//
//
//        IPuppeteerElement[] pes3 =  puppeteer.findElements3(".as-explore-product");
//        IPuppeteerElement pe = pes3[0];
//        pe.click();
//        Arrays.stream(pes3).forEach(IPuppeteerElement::click);
//        IPuppeteerElementCollection pes2 =  puppeteer.findElements2(".as-explore-product");
//        pes2.get(0).click();
//        puppeteer.click("#ac-gn-link-search");
//        puppeteer.waitForSelector("#ac-gn-searchform-input");
//        saveScreenshot("show search");
//        puppeteer.type("#ac-gn-searchform-input","iphone 12\n");
//
//        puppeteer.waitForTimeout(1000);
//        saveScreenshot("search");
//        puppeteer.waitForTimeout(1000);
//
//        puppeteer.mouseMove(100,100);
//        puppeteer.waitForTimeout(1000);
//        puppeteer.mouseMove(1000,1000);
//        puppeteer.waitForTimeout(1000);
//        puppeteer.mouseMove(100,100);
//        puppeteer.waitForTimeout(1000);
//        puppeteer.mouseMove(1000,1000);
//        puppeteer.waitForTimeout(1000);
//
//        IHistoryItem[] histories = puppeteer.getHistory();

    }
}
