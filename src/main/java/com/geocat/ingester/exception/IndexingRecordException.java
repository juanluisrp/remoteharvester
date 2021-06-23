package com.geocat.ingester.exception;

public class IndexingRecordException extends Exception {
    private static final long serialVersionUID = -2705465334589313787L;

    public IndexingRecordException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public IndexingRecordException(String errorMessage) {
        super(errorMessage);
    }
}

