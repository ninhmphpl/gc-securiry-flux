package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.AuthorizeExchangeSpec;
import com.gcoder.securityflux.base.PermitSecurity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorizeExchangeSpecImpl implements AuthorizeExchangeSpec {
    private final ServerHttpSecurityImpl httpSecurity;
    @Override
    public PermitSecurity pathMatchers(String... antPatterns) {
        return new PermitSecurityImpl(httpSecurity, antPatterns);
    }
}
