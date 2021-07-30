package demo.codingnomads.co.security.authentication.filters;

import demo.codingnomads.co.exceptions.NoSuchUserException;
import demo.codingnomads.co.models.securitymodels.CustomUserDetails;
import demo.codingnomads.co.models.securitymodels.TwoFactorCode;
import demo.codingnomads.co.services.GrantedAuthorityService;
import demo.codingnomads.co.services.TwoFactorAuthCodeService;
import demo.codingnomads.co.services.userservices.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TwoFactorAuthConfirmationFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    GrantedAuthorityService grantedAuthorityService;

    @Autowired
    TwoFactorAuthCodeService twoFactorAuthService;

    @Autowired
    TwoFactorAuthCodeService authService;

    @Autowired
    CustomUserDetailService userDetailService;


    public TwoFactorAuthConfirmationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    public TwoFactorAuthConfirmationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //extract cookie MONSTER from request
        Cookie[] cookies = request.getCookies();
        Cookie twoFACookie = null;
        for (Cookie c : cookies) {
            if (c.getName().equals("MONSTER")) {
                twoFACookie = c;
            }
        }

        //if cookie not present redirect 2FA code not assigned or expired. redirect to login
        if (twoFACookie == null) {
            response.sendRedirect("/login");
            return null;
        } else {
            //get reader to read code from body of request
            String codeToCheck = request.getReader().readLine();

            TwoFactorCode twoFactorCode;
            CustomUserDetails user;

            try {
                twoFactorCode = twoFactorAuthService.getCodeByCookieValue(twoFACookie.getValue());
                user = userDetailService.getByUserId(twoFactorCode.getUserId());
                authService.authenticateTwoFactorAuthCode(codeToCheck, twoFactorCode);
            } catch (NoSuchUserException e) {
                //should not happen unless build broken
                throw new BadCredentialsException("Could not find username");
            }

            //authentication success! create and assign token to sec context
            UsernamePasswordAuthenticationToken auth
                    = new UsernamePasswordAuthenticationToken(user, null, grantedAuthorityService.getGrantedAuthoritiesByUserId(user.getId()));
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            return auth;
        }
    }


}
