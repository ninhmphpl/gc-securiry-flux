package com.gcoder.securityflux.throwable;

public class TooManyRequest extends Exception{
    public TooManyRequest(String message) {
        super(message);
    }
}
