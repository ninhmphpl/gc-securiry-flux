package com.gcoder.securityflux.base;

import java.util.Set;

public interface UserDetails {
    String getUserName();
    String getPassword();
    Set<String> getRole();
    boolean getActive();
}
