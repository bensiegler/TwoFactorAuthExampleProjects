package demo.codingnomads.co.security;

import demo.codingnomads.co.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.TwoFactorAuthenticationFilter;
import org.springframework.security.web.authentication.twofa.stategies.codegeneration.SixDigitAuthCodeGenerationStrategy;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultTwoFactorChoiceGeneratingFilter;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    FailedAuthenticationHandler authenticationHandler;

//    @Autowired
//    TwoFactorAuthInitiationFilter twoFactorAuthInitiationFilter;
//
//    @Autowired
//    TwoFactorAuthConfirmationFilter twoFactorAuthConfirmationFilter;
//
//    @Autowired
//    TwoFactorAuthenticationFilter authenticationFilter;

    @Autowired
    CustomUserDetailService userDetailService;


    Cache cache = new ConcurrentMapCache("TwoFactorAuthCodeCache", false);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()

                .twoFactorLogin()
                .sendStrategy(new AwsEmailSendStrategy())
                .loginPage("/login").permitAll()
                .codeService()
                    .inMemoryRepository(cache)
                    .generationStrategy(new SixDigitAuthCodeGenerationStrategy())
                    .expirationTime(45000)
                    .and()
                .twoFactorRedirectUrl("/2FA")
                .twoFactorProcessingUrl("/2FA/authenticate")
                .failureUrl("/login?error=true")
                .twoFactorFailureUrl("/2FA?error=true")
                .defaultSuccessUrl("/")
                .userDetailsService(userDetailService)

                .and()
                .logout().permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();


//                .and().authenticationProvider(customUsernameAndPasswordAuthenticationProvider);

//        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(twoFactorAuthInitiationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(twoFactorAuthConfirmationFilter, TwoFactorAuthInitiationFilter.class);
    }

    @Autowired
    CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.authenticationProvider(customUsernameAndPasswordAuthenticationProvider);
    }


}
