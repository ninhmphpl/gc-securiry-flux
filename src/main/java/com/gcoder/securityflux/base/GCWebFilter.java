package com.gcoder.securityflux.base;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface GCWebFilter {
    Mono<Void> filter(ServerWebExchange exchange);

}
