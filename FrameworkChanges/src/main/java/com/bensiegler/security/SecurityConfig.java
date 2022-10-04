package com.bensiegler.security;

import com.bensiegler.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.twofa.stategies.codegeneration.TwoFactorAuthCodeGenerationStrategyImpl;
import org.springframework.security.web.authentication.twofa.stategies.sendattemp.ConsolePrintTwoFactorAuthCodeSendStrategy;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()

                .twoFactorLogin()
                .sendStrategy(new ConsolePrintTwoFactorAuthCodeSendStrategy())
                .loginPage("/login").permitAll()//need to find out how the original configurer works compared to this & how the LoginGeneratingFilter plays with the 2FAFilter
                .codeService()
                    .inMemoryRepository()
                    .generationStrategy(new TwoFactorAuthCodeGenerationStrategyImpl())
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
    }

    @Autowired
    CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.authenticationProvider(customUsernameAndPasswordAuthenticationProvider);
    }


}
