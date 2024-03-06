package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.*;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@AllArgsConstructor
public class UserAuthenticationToken implements UserAuthentication {
    private UserDetailsService userDetailsService;
    private JwtService jwtService;
    @Override
    public Mono<UserDetails> getUserDetail(ServerWebExchange exchange) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String username = jwtService.parseToken(authorizationHeader.substring(7));
            return userDetailsService.getUserByUsername(username);
        }
        return Mono.error(new JwtException("Invalid token"));
    }
}
