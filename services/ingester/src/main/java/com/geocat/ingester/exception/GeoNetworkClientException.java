package com.geocat.ingester.exception;

public class GeoNetworkClientException extends Exception {
    private static final long serialVersionUID = 1425454476959741933L;

    public GeoNetworkClientException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public GeoNetworkClientException(String errorMessage) {
        super(errorMessage);
    }
}

