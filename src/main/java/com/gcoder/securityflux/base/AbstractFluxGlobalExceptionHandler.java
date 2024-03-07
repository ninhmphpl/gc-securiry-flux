package com.gcoder.securityflux.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFluxGlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Class<? extends Throwable>, Integer> exceptionHttpStatusMap = new HashMap<>();

    protected AbstractFluxGlobalExceptionHandler addException(Class<? extends Throwable> exception, int code){
        exceptionHttpStatusMap.put(exception, code);
        return this;
    }

    @Override
    public @NonNull Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Integer statusCode = exceptionHttpStatusMap.get(ex.getClass());
        if (statusCode == null) statusCode = 500;
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(statusCode));
        String body = makeBodyString(ex);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(body.getBytes())));
    }

    public abstract String makeBodyString(Throwable ex);
}
