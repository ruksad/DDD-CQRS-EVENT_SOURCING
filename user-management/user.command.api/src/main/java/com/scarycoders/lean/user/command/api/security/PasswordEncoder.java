package com.scarycoders.lean.user.command.api.security;

public interface PasswordEncoder {
    String hashPassword(String password);
}
