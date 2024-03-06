package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.*;
import lombok.Data;
import org.springframework.stereotype.Service;


import java.util.*;

@Data
public class ServerHttpSecurityImpl implements ServerHttpSecurity {
    private final Set<String> pathAll = new HashSet<>();
    private final Set<String> pathAuthentication = new HashSet<>();
    private final Map<String, Set<String>> pathByRole = new HashMap<>();
    private final List<GCWebFilter> gcWebFilters = new ArrayList<>();
    private UserAuthentication userAuthentications;


    @Override
    public ServerHttpSecurity authorizeExchange(Customizer<AuthorizeExchangeSpec> authorizeExchangeCustomizer) {
        authorizeExchangeCustomizer.customize(new AuthorizeExchangeSpecImpl(this));
        return this;
    }

    @Override
    public ServerHttpSecurity addFilterAt(GCWebFilter webFilter) {
        gcWebFilters.add(webFilter);
        return this;
    }

    @Override
    public ServerHttpSecurity addUserAuthenticationAt(UserAuthentication userAuthentication) {
        userAuthentications = (userAuthentication);
        return this;
    }


    @Override
    public SecurityWebFilterChain build() {
        return new SecurityWebFilterChain(pathAll, pathAuthentication, pathByRole, gcWebFilters, userAuthentications);
    }

}
