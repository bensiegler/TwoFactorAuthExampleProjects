package com.bensiegler.security;

import com.bensiegler.services.CustomUserDetailService;
import com.bensiegler.models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomUsernameAndPasswordAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    CustomUserDetailService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication auth = super.authenticate(authentication);
        CustomUserDetails customUserDetails = getUserFromDb(authentication.getName());

        //Create new User to be passed around with the security context
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), customUserDetails.getAuthorities());
        updateLastSignIn(customUserDetails);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if(UsernamePasswordAuthenticationToken.class.equals(authentication)) {
            return true;
        }

        return false;
    }

    private CustomUserDetails getUserFromDb(String usernameOrEmail) {
        usernameOrEmail = usernameOrEmail.trim();
        return (CustomUserDetails) userDetailService.loadUserByUsername(usernameOrEmail);
    }

    private void updateLastSignIn(CustomUserDetails databaseCustomUserDetails) {
        //set last sign in time
        databaseCustomUserDetails.setLastSignIn(System.currentTimeMillis());

        //persist user to update last sign in
        userDetailService.updateUser(databaseCustomUserDetails);
    }

}
