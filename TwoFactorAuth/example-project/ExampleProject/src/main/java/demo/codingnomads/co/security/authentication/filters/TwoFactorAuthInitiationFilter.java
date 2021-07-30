package demo.codingnomads.co.security.authentication.filters;


import demo.codingnomads.co.models.securitymodels.CustomUserDetails;
import demo.codingnomads.co.models.securitymodels.TwoFactorCode;
import demo.codingnomads.co.security.authentication.TwoFactorAuthToken;
import demo.codingnomads.co.services.TwoFactorAuthCodeService;
import demo.codingnomads.co.services.userservices.CustomUserDetailService;
import demo.codingnomads.co.util.CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class TwoFactorAuthInitiationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    TwoFactorAuthCodeService authService;

    @Autowired
    CustomUserDetailService userDetailService;


    @Value("${security.two-FA.cookie.maxAge}")
    private int maxAge;

    public TwoFactorAuthInitiationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    public TwoFactorAuthInitiationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String username = getRequestUsername(request);
        CustomUserDetails user = getUserFromDb(username);

        if (user.isTwoFactorAuthEnabled()) {

            //confirm username and password
            TwoFactorAuthToken token =
                    new TwoFactorAuthToken(getRequestUsername(request), getRequestPassword(request), null);

            super.getAuthenticationManager().authenticate(token);

            handleNewTwoFactorRequest(response, user);
            return null;
        }else{
            throw new ProviderNotFoundException("2FA is not enabled!");
        }


    }


    ////////////// HELPERS //////////////
    private String getRequestUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    private String getRequestPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }

    private CustomUserDetails getUserFromDb(String usernameOrEmail) {
        usernameOrEmail = usernameOrEmail.trim();
        return (CustomUserDetails) userDetailService.loadUserByUsername(usernameOrEmail);
    }

    private void handleNewTwoFactorRequest(HttpServletResponse httpServletResponse,
                                           CustomUserDetails userDetails) throws IOException {

        Cookie twoFACookie = CookieHelper.assignCookie(httpServletResponse, "MONSTER", 30, maxAge);

        String code =  authService.generateTwoFactorAuthCode();

        try {
            authService.send2FAEmail(userDetails, code);
        } catch (MessagingException messagingException) {
            //shouldn't happen
        }

        TwoFactorCode twoFactorCode = TwoFactorCode.builder()
                .userId(userDetails.getId())
                .cookie(twoFACookie.getValue())
                .code(code)
                .timeCreated(new Date())
                .build();

        authService.saveTwoFactorCode(twoFactorCode);

        httpServletResponse.sendRedirect("/2FA");
    }
}
