package com.gcoder.securityflux.base;


public interface ServerHttpSecurity {
    ServerHttpSecurity authorizeExchange(Customizer<AuthorizeExchangeSpec> authorizeExchangeCustomizer);
    ServerHttpSecurity addFilterAt(GCWebFilter webFilter);
    ServerHttpSecurity addUserAuthenticationAt(UserAuthentication userAuthentication);
    SecurityWebFilterChain build();
}
