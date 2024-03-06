package com.gcoder.securityflux.base;

import org.springframework.web.server.ServerWebExchange;

public interface GCoderFilter {
    void filter(ServerWebExchange exchange);
}
