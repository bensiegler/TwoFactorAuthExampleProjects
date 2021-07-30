package demo.codingnomads.co.exceptions.security;

import demo.codingnomads.co.models.securitymodels.CustomUserDetails;
import org.springframework.security.core.AuthenticationException;


public class TwoFactorAuthRequiredException extends AuthenticationException {
    private final CustomUserDetails customUserDetails;


    public TwoFactorAuthRequiredException(CustomUserDetails customUserDetails) {
        super("forced redirect to /twofa");
        this.customUserDetails = customUserDetails;
    }

    public CustomUserDetails getCustomUserDetails() {
        return customUserDetails;
    }

}
