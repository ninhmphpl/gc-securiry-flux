package com.gcoder.securityflux.base;

import io.jsonwebtoken.JwtException;

public interface JwtService {
    String parseToken(String credential) throws JwtException;
    String createJwt(String subject);
}