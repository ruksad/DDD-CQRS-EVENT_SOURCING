package com.scarycoders.lean.user.command.api.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String hashPassword(String password) {
        var encoder = new BCryptPasswordEncoder(12);
        var hashedPassword = encoder.encode(password);
        return hashedPassword;
    }
}
