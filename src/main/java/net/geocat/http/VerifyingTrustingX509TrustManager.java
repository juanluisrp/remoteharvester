package net.geocat.http;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class VerifyingTrustingX509TrustManager implements X509TrustManager {

    X509TrustManager underlying;

    boolean clientTrusted = true;
    Exception clientTrustedException;

    boolean serverTrusted = true;
    Exception serverTrustedException;

    public VerifyingTrustingX509TrustManager(X509TrustManager underlying){
        this.underlying = underlying;
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        try {
            underlying.checkClientTrusted(certs,authType);
            clientTrusted = true;
        }
        catch(Exception e){
            clientTrusted = false;
            clientTrustedException = e;
        }
    }
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        try {
            underlying.checkServerTrusted(certs,authType);
            serverTrusted = true;
        }
        catch(Exception e){
            serverTrusted = false;
            serverTrustedException = e;
        }
    }

}
