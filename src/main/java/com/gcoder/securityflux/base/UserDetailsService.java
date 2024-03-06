package com.gcoder.securityflux.base;

import reactor.core.publisher.Mono;

public interface UserDetailsService {
    Mono<UserDetails> getUserByUsername(String username);
}
