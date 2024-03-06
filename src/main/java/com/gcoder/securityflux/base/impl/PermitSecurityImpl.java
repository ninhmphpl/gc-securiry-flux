package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.AuthorizeExchangeSpec;
import com.gcoder.securityflux.base.PermitSecurity;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class PermitSecurityImpl implements PermitSecurity {
    private final ServerHttpSecurityImpl httpSecurity;
    private final String[] antPatterns;
    @Override
    public AuthorizeExchangeSpec permitAll() {
        httpSecurity.getPathAll().addAll(Arrays.asList(antPatterns));
        return new AuthorizeExchangeSpecImpl(httpSecurity);
    }

    @Override
    public AuthorizeExchangeSpec authenticated() {
        httpSecurity.getPathAuthentication().addAll(Arrays.asList(antPatterns));
        return new AuthorizeExchangeSpecImpl(httpSecurity);
    }

    @Override
    public AuthorizeExchangeSpec hasAnyRole(String... role) {
        Map<String,Set<String>> pathByRole = httpSecurity.getPathByRole();
        for(var r : role){
            if(pathByRole.containsKey(r)){
                pathByRole.get(r).addAll(Arrays.asList(antPatterns));
            }else {
                Set<String> set = new HashSet<>(Arrays.asList(antPatterns));
                pathByRole.put(r, set);
            }
        }
        return new AuthorizeExchangeSpecImpl(httpSecurity);
    }

}
