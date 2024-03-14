package com.gcoder.securityflux.api;

import com.gcoder.securityflux.base.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("*")
public class HelloApi {
    @GetMapping("/hello")
    public Mono<String> hello(){
        return Mono.just("hello");
    }

    @GetMapping("/hello/{path}")
    public Mono<String> path(@PathVariable String path, ServerWebExchange exchange){
        UserDetails userDetails = exchange.getAttribute("userDetails");
        return Mono.just(path);
    }
}
