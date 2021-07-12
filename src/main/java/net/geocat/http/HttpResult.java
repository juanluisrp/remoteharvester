package net.geocat.http;

public class HttpResult {
    byte[] data;
    boolean fullyRead;
    int httpCode;
    String contentType;
    String cookie;
    boolean errorOccurred;


    public    HttpResult(byte[] data){
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isFullyRead() {
        return fullyRead;
    }

    public void setFullyRead(boolean fullyRead) {
        this.fullyRead = fullyRead;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }
}
