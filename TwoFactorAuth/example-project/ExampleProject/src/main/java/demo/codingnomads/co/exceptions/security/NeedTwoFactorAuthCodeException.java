package demo.codingnomads.co.exceptions.security;


import org.springframework.security.core.AuthenticationException;

public class NeedTwoFactorAuthCodeException extends AuthenticationException {

    public NeedTwoFactorAuthCodeException(String explanation) {
        super(explanation);
    }
}
