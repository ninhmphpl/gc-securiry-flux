package com.gcoder.securityflux.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
@Log4j2
public abstract class AbstractFluxGlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Class<? extends Throwable>, Integer> exceptionHttpStatusMap = new HashMap<>();

    protected AbstractFluxGlobalExceptionHandler addException(Class<? extends Throwable> exception, int code){
        exceptionHttpStatusMap.put(exception, code);
        return this;
    }

    @Override
    public @NonNull Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        try{
            Integer statusCode = exceptionHttpStatusMap.get(ex.getClass());
            if (statusCode == null) statusCode = 500;
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(statusCode));
            String body = objectMapper.writeValueAsString(makeBodyString(ex));
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(body.getBytes())));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public abstract Object makeBodyString(Throwable ex);
}
