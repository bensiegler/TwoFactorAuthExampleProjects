package demo.codingnomads.co.security;

import demo.codingnomads.co.security.authentication.authenticationproviders.CustomUsernameAndPasswordAuthenticationProvider;
import demo.codingnomads.co.services.userservices.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.twofa.stategies.codegeneration.SixDigitCodeGenerationStrategy;
import org.springframework.security.web.authentication.twofa.stategies.sendattemp.AwsEmailSendStrategy;

import javax.sql.DataSource;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()

//                .formLogin()
////                .loginPage("/login").permitAll()
////                .failureHandler(authenticationHandler)
//                .defaultSuccessUrl("/")
////                .requiresChannel().anyRequest().requiresSecure()
//                .and()

                .sessionManagement().maximumSessions(2).maxSessionsPreventsLogin(true)
                .and().and()

                .twoFactorLogin()
                .sendStrategy(new AwsEmailSendStrategy())
                .loginPage("/login").permitAll()
                .codeService()
                    .databaseRepository(dataSource)
                    .generationStrategy(new SixDigitCodeGenerationStrategy())
                    .expirationTime(45000)
                    .and()
                .twoFactorRedirectUrl("/2FA")
                .twoFactorProcessingUrl("/2FA/authenticate")
                .failureUrl("/login?error=true")
                .twoFactorFailureUrl("/2FA?error=true")
                .defaultSuccessUrl("/recipes")
                .userDetailsService(userDetailService)
                .and()


                .authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/2FA").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated()
                .and().logout().permitAll();

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
