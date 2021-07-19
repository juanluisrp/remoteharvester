package net.geocat.http;

import javax.net.ssl.*;
import java.security.KeyStore;

public class VerifyingTrustingX509TrustManagerFactory {

    public static VerifyingTrustingX509TrustManager createVerifyingTrustingX509TrustManager() {
        try {
             TrustManagerFactory defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            defaultTrustManagerFactory.init((KeyStore) null);
            X509TrustManager underlyingX509TrustManager = (X509TrustManager) defaultTrustManagerFactory.getTrustManagers()[0];
            return new VerifyingTrustingX509TrustManager(underlyingX509TrustManager) ;
        }
        catch(Exception e){
            return null;
        }
    }

    public static SSLSocketFactory getIndiscriminateSSLSocketFactory(VerifyingTrustingX509TrustManager verifyingTrustingX509TrustManager) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null,
                    new TrustManager[] {verifyingTrustingX509TrustManager},
                    new java.security.SecureRandom());
            return sc.getSocketFactory();
        }
        catch(Exception e){
            return null;
        }
    }
}
