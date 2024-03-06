package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {
    private final int gensalt;

    public BCryptPasswordEncoder() {
        gensalt = 10;
    }

    public BCryptPasswordEncoder(int gensalt) {
        this.gensalt = gensalt;
    }

    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(gensalt));
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword,encodedPassword);
    }
}
