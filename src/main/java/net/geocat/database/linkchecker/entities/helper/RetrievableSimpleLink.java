package net.geocat.database.linkchecker.entities.helper;

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities2.IndicatorStatus;

import javax.persistence.*;
import java.util.Arrays;

@MappedSuperclass
public class RetrievableSimpleLink {

    @Column(columnDefinition = "text")
    String rawURL;

    @Column(columnDefinition = "text")
    String fixedURL;

    @Column(columnDefinition = "text")
    String finalURL;

    @Column(columnDefinition = "varchar(64)")
    private String sha2;

    private Boolean linkIsHTTS;
    private Boolean linkSSLTrustedByJava;

    @Column(columnDefinition = "text")
    private String linkSSLUntrustedByJavaReason;


    @Column(columnDefinition = "text")
    // i.e. connecttimeout
    private String LinkHTTPException;

    // (i.e. 200)
    private Integer LinkHTTPStatusCode;


    @Column(columnDefinition = "text")
    // (i.e. application/xml) from HTTP response
    private String LinkMIMEType;

    @Column(columnDefinition = "bytea")
    //(first 1000 bytes of request - might be able to determine what file type from this)
    private byte[] LinkContentHead;

    // (is the link an XML document - i.e. starts with "<?xml")
    private Boolean LinkIsXML;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus Indicator_LinkResolves;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private PartialDownloadHint partialDownloadHint;

    Boolean urlFullyRead;

    @Transient
    byte[] fullData;

    //---------------------------------------------------------------------------

    public String getRawURL() {
        return rawURL;
    }

    public void setRawURL(String rawURL) {
        this.rawURL = rawURL;
    }

    public String getFinalURL() {
        return finalURL;
    }

    public void setFinalURL(String finalURL) {
        this.finalURL = finalURL;
    }

    public Boolean getLinkIsHTTS() {
        return linkIsHTTS;
    }

    public void setLinkIsHTTS(Boolean linkIsHTTS) {
        this.linkIsHTTS = linkIsHTTS;
    }

    public Boolean getLinkSSLTrustedByJava() {
        return linkSSLTrustedByJava;
    }

    public void setLinkSSLTrustedByJava(Boolean linkSSLTrustedByJava) {
        this.linkSSLTrustedByJava = linkSSLTrustedByJava;
    }

    public String getLinkSSLUntrustedByJavaReason() {
        return linkSSLUntrustedByJavaReason;
    }

    public void setLinkSSLUntrustedByJavaReason(String linkSSLUntrustedByJavaReason) {
        this.linkSSLUntrustedByJavaReason = linkSSLUntrustedByJavaReason;
    }

    public String getLinkHTTPException() {
        return LinkHTTPException;
    }

    public void setLinkHTTPException(String linkHTTPException) {
        LinkHTTPException = linkHTTPException;
    }

    public Integer getLinkHTTPStatusCode() {
        return LinkHTTPStatusCode;
    }

    public void setLinkHTTPStatusCode(Integer linkHTTPStatusCode) {
        LinkHTTPStatusCode = linkHTTPStatusCode;
    }

    public String getLinkMIMEType() {
        return LinkMIMEType;
    }

    public void setLinkMIMEType(String linkMIMEType) {
        LinkMIMEType = linkMIMEType;
    }

    public byte[] getLinkContentHead() {
        return LinkContentHead;
    }

    public void setLinkContentHead(byte[] linkContentHead) {
        LinkContentHead = linkContentHead;
    }

    public Boolean getLinkIsXML() {
        return LinkIsXML;
    }

    public void setLinkIsXML(Boolean linkIsXML) {
        LinkIsXML = linkIsXML;
    }

    public IndicatorStatus getIndicator_LinkResolves() {
        return Indicator_LinkResolves;
    }

    public void setIndicator_LinkResolves(IndicatorStatus indicator_LinkResolves) {
        Indicator_LinkResolves = indicator_LinkResolves;
    }

    public PartialDownloadHint getPartialDownloadHint() {
        return partialDownloadHint;
    }

    public void setPartialDownloadHint(PartialDownloadHint partialDownloadHint) {
        this.partialDownloadHint = partialDownloadHint;
    }

    public String getFixedURL() {
        return fixedURL;
    }

    public void setFixedURL(String fixedURL) {
        this.fixedURL = fixedURL;
    }

    public Boolean getUrlFullyRead() {
        return urlFullyRead;
    }

    public void setUrlFullyRead(Boolean urlFullyRead) {
        this.urlFullyRead = urlFullyRead;
    }

    public byte[] getFullData() {
        return fullData;
    }

    public void setFullData(byte[] fullData) {
        this.fullData = fullData;
    }

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        String result = "";

        if ((rawURL != null) && (!rawURL.isEmpty()))
            result += "      rawURL: " + rawURL + "\n";
        if ((fixedURL != null) && (!fixedURL.isEmpty()))
            result += "      fixedURL: " + fixedURL + "\n";
        if ((finalURL != null) && (!finalURL.isEmpty()))
            result += "      finalURL: " + finalURL + "\n";
        result += "\n";

        if ( (partialDownloadHint == null))
            result += "     +  Partial Download: " + PartialDownloadHint.ALWAYS_DOWNLOAD + "\n";
        else
            result += "     +  Partial Download: " + partialDownloadHint + "\n";

        if (urlFullyRead != null)
            result += "     +  fully downloaded: " + urlFullyRead + "\n";

        if (getLinkHTTPException() != null)
            result += "     +  URL threw exception: " + getLinkHTTPException() + "\n";

        if (getLinkIsHTTS() != null)
            result += "     +  link Is HTTPS: " + getLinkIsHTTS() + "\n";
        if (getLinkSSLTrustedByJava() != null)
            result += "     +  link SSL Trusted by java: " + getLinkSSLTrustedByJava() + "\n";
        if (getLinkSSLUntrustedByJavaReason() != null)
            result += "     +  Reason link ssl not trusted by Java: " + getLinkSSLUntrustedByJavaReason() + "\n";

        if (getLinkHTTPStatusCode() != null)
            result += "     +  Status Code of HTTP request getting the link: " + getLinkHTTPStatusCode() + "\n";
        if (getLinkMIMEType() != null)
            result += "     +  ContentType of HTTP request getting the link: " + getLinkMIMEType() + "\n";
        if (getLinkContentHead() != null) {
            result += "     +  Initial Data from request: " + Arrays.copyOf(getLinkContentHead(), 10) + "\n";
            result += "     +  Initial Data from request (text): " + new String(Arrays.copyOf(getLinkContentHead(), Math.min(100, getLinkContentHead().length))) + "\n";
        }
        if (getLinkIsXML() != null) {
            result += "     +  Link is XML: " + getLinkIsXML() + "\n";
        }
        if ( (sha2 != null) && (!sha2.isEmpty()) )
            result += "     +  SHA2: " + getSha2() + "\n";

        return result;


    }
}
