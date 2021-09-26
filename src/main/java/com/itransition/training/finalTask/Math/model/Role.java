package com.itransition.training.finalTask.Math.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
