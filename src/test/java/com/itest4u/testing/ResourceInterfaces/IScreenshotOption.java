package com.itest4u.testing.ResourceInterfaces;

public class IScreenshotOption {
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    private String encoding ="base64";

    public Boolean getFullPage() {
        return fullPage;
    }

    public void setFullPage(Boolean fullPage) {
        this.fullPage = fullPage;
    }

    private Boolean fullPage=false;

    public Boolean getOmitBackground() {
        return omitBackground;
    }

    public void setOmitBackground(Boolean omitBackground) {
        this.omitBackground = omitBackground;
    }

    private Boolean omitBackground = true;

}
