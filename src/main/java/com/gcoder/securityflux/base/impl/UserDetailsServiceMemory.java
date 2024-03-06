package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.UserDetails;
import com.gcoder.securityflux.base.UserDetailsService;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsServiceMemory implements UserDetailsService {
    private final Map<String, UserDetails> userDetailsMap = new HashMap<>();
    @Override
    public Mono<UserDetails> getUserByUsername(String username) {
        return Mono.just(userDetailsMap.get(username));
    }
    public void addUserDetails (UserDetails userDetails){
        userDetailsMap.put(userDetails.getUserName(), userDetails);
    }

}
