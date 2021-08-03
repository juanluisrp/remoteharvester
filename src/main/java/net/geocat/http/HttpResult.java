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


    public HttpResult(byte[] data) {
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
