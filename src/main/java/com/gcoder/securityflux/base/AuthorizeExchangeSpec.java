package com.gcoder.securityflux.base;

public interface AuthorizeExchangeSpec {
    public PermitSecurity pathMatchers(String... antPatterns);

}
