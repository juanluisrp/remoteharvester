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

package geocat.csw.http;

import javax.net.ssl.*;
import java.security.KeyStore;

public class VerifyingTrustingX509TrustManagerFactory {

    public static geocat.csw.http.VerifyingTrustingX509TrustManager createVerifyingTrustingX509TrustManager() {
        try {
            TrustManagerFactory defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            defaultTrustManagerFactory.init((KeyStore) null);
            X509TrustManager underlyingX509TrustManager = (X509TrustManager) defaultTrustManagerFactory.getTrustManagers()[0];
            return new geocat.csw.http.VerifyingTrustingX509TrustManager(underlyingX509TrustManager);
        } catch (Exception e) {
            return null;
        }
    }

    public static SSLSocketFactory getIndiscriminateSSLSocketFactory(geocat.csw.http.VerifyingTrustingX509TrustManager verifyingTrustingX509TrustManager) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null,
                    new TrustManager[]{verifyingTrustingX509TrustManager},
                    new java.security.SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) {
            return null;
        }
    }
}
