package com.bensiegler.config;

import com.bensiegler.security.CustomUsernameAndPasswordAuthenticationProvider;
import com.bensiegler.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfig {

    @Autowired
    CustomUserDetailService userDetailService;

    @Autowired
    PasswordEncoder encoder;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider() {
        CustomUsernameAndPasswordAuthenticationProvider authenticationProvider = new CustomUsernameAndPasswordAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailService);
        return authenticationProvider;
    }
}
