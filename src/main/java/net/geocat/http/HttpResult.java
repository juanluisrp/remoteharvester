package net.geocat.http;

import java.util.List;

public class HttpResult {
    byte[] data;
    boolean fullyRead;
    int httpCode;
    String contentType;
    String specialToSendCookie;
    boolean errorOccurred;
    boolean isHTTPS;
    String finalURL;

    String sentCookie;
    List<String> receivedCookie;

    boolean sslTrusted;
    String sslUnTrustedReason;


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

    public String getSpecialToSendCookie() {
        return specialToSendCookie;
    }

    public void setSpecialToSendCookie(String specialToSendCookie) {
        this.specialToSendCookie = specialToSendCookie;
    }

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public boolean isHTTPS() {
        return isHTTPS;
    }

    public void setHTTPS(boolean HTTPS) {
        isHTTPS = HTTPS;
    }

    public String getFinalURL() {
        return finalURL;
    }

    public void setFinalURL(String finalURL) {
        this.finalURL = finalURL;
    }

    public String getSentCookie() {
        return sentCookie;
    }

    public void setSentCookie(String sentCookie) {
        this.sentCookie = sentCookie;
    }

    public List<String> getReceivedCookie() {
        return receivedCookie;
    }

    public void setReceivedCookie(List<String> receivedCookie) {
        this.receivedCookie = receivedCookie;
    }

    public boolean isSslTrusted() {
        return sslTrusted;
    }

    public void setSslTrusted(boolean sslTrusted) {
        this.sslTrusted = sslTrusted;
    }

    public String getSslUnTrustedReason() {
        return sslUnTrustedReason;
    }

    public void setSslUnTrustedReason(String sslUnTrustedReason) {
        this.sslUnTrustedReason = sslUnTrustedReason;
    }
}
