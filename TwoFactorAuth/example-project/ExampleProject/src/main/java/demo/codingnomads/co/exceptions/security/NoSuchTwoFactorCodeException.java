package demo.codingnomads.co.exceptions.security;


import org.springframework.security.core.AuthenticationException;

public class NoSuchTwoFactorCodeException extends AuthenticationException {

    public NoSuchTwoFactorCodeException(String message) {
        super(message);
    }
}
