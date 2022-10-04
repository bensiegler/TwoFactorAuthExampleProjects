package com.bensiegler.services;

import com.bensiegler.models.CustomUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;

@Configuration
public class CustomUserPasswordService implements UserDetailsPasswordService {
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return new CustomUserDetails();
    }
}
