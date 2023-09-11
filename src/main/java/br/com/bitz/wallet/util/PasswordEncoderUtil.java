package br.com.bitz.wallet.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public abstract class PasswordEncoderUtil {

    private PasswordEncoderUtil(){}

    public static String encodePassword(final String password) {
        PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder().encode(password);
    }
}
