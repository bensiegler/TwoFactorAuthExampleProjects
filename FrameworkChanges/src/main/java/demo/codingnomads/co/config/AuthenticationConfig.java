package demo.codingnomads.co.config;

import demo.codingnomads.co.security.CustomUsernameAndPasswordAuthenticationProvider;
import demo.codingnomads.co.services.CustomUserDetailService;
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

    //TODO MAKE THIS BEAN ADDITION AUTOMATIC
    @Bean
    public CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider() {
        CustomUsernameAndPasswordAuthenticationProvider authenticationProvider = new CustomUsernameAndPasswordAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailService);
        return authenticationProvider;
    }
}
