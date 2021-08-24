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

package net.geocat.database.linkchecker.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "HttpResultCache"
        ,indexes = {
//                @Index(
//                        name = "idx_httpresultcache_url",
//                        columnList = "URL",
//                        unique = false
//                ),
        @Index(
                name = "idx_httpresultcache_job_url",
                columnList = "linkCheckJobId,URL",
                unique = true
        ),
} )
public class HttpResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long httpResultId;

    @Column(columnDefinition = "varchar(40)")
    String linkCheckJobId;

    @Column(columnDefinition = "bytea")
    byte[] data;

    boolean fullyRead;
    Integer httpCode;

    @Column(columnDefinition = "text")
    String contentType;

    @Column(columnDefinition = "text")
    String specialToSendCookie;

    boolean errorOccurred;
    boolean isHTTPS;


    @Column(columnDefinition = "text")
    String URL;

    @Column(columnDefinition = "text")
    String finalURL;

    @Column(columnDefinition = "text")
    String sentCookie;

    @Column(columnDefinition = "text")
    String  receivedCookie;

    boolean sslTrusted;

    @Column(columnDefinition = "text")
    String sslUnTrustedReason;

    public HttpResult() {

    }

    public HttpResult(byte[] data) {
        this.data = data;
    }

    public long getHttpResultId() {
        return httpResultId;
    }

    public void setHttpResultId(long httpResultId) {
        this.httpResultId = httpResultId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public Boolean getFullyRead() {
        return fullyRead;
    }

    public void setFullyRead(Boolean fullyRead) {
        this.fullyRead = fullyRead;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Boolean getErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(Boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public Boolean getHTTPS() {
        return isHTTPS;
    }

    public void setHTTPS(Boolean HTTPS) {
        isHTTPS = HTTPS;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Boolean getSslTrusted() {
        return sslTrusted;
    }

    public void setSslTrusted(Boolean sslTrusted) {
        this.sslTrusted = sslTrusted;
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

    public  String  getReceivedCookie() {
        return receivedCookie;
    }

    public void setReceivedCookie( String receivedCookie) {
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
