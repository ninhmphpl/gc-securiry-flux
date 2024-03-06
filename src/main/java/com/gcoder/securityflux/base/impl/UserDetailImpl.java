package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.UserDetails;
import lombok.AllArgsConstructor;

import java.util.Set;
@AllArgsConstructor
public class UserDetailImpl implements UserDetails {
    private String username;
    private String password;
    private Set<String> role;
    private boolean active;

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Set<String> getRole() {
        return role;
    }

    @Override
    public boolean getActive() {
        return active;
    }
}
