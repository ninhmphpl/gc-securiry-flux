package com.gcoder.securityflux.base;

public interface PermitSecurity {
    AuthorizeExchangeSpec permitAll();
    AuthorizeExchangeSpec authenticated();
    AuthorizeExchangeSpec hasAnyRole(String... role);
}
