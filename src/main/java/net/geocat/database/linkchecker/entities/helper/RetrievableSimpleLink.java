/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.database.linkchecker.entities.helper;

import net.geocat.database.linkchecker.entities2.IndicatorStatus;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@MappedSuperclass
public class RetrievableSimpleLink {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private LinkState linkState;

    @Column(columnDefinition = "text")
    String rawURL;

    @Column(columnDefinition = "text")
    String fixedURL;

    @Column(columnDefinition = "text")
    String finalURL;
    Boolean urlFullyRead;
    @Transient
    byte[] fullData;
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

    //---------------------------------------------------------------------------


    public LinkState getLinkState() {
        return linkState;
    }

    public void setLinkState(LinkState linkState) {
        this.linkState = linkState;
    }

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

        result += "      linkstate: " + linkState + "\n";
        result += "\n";

        if ((partialDownloadHint == null))
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
            try {
                String info = "     +  Initial Data from request (text): " + new String(Arrays.copyOf(getLinkContentHead(), Math.min(100, getLinkContentHead().length)),"UTF-8") + "\n";
                info = info.replaceAll("\u0000",""); // bad UTF-8 chars
                result += info;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (getLinkIsXML() != null) {
            result += "     +  Link is XML: " + getLinkIsXML() + "\n";
        }
        if ((sha2 != null) && (!sha2.isEmpty()))
            result += "     +  SHA2: " + getSha2() + "\n";

        return result;


    }
}
