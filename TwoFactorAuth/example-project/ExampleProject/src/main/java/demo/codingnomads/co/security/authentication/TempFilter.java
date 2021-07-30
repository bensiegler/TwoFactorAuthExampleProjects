package demo.codingnomads.co.security.authentication;

import demo.codingnomads.co.security.authentication.authenticationproviders.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@Order(1)
public class TempFilter extends WebSecurityConfigurerAdapter {

    @Autowired
    UsernamePasswordAuthenticationProvider provider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/recipes")
//                .authorizeRequests().anyRequest().authenticated()
//                .and().authenticationProvider(provider);

        http
                //indicate you want users to login using a form. No loginPage() is specified so Spring will provide a default
                .formLogin()

                //indicate that a logout page is required
                .and().logout()

                //gain access to the session management methods
                .and().sessionManagement()
                    //indicate a user can only have 2 sessions.
                    .maximumSessions(2)
                    //tell Spring Security that instead of quietly invalidating one of the other sessions when the maximum is reached
                    //the user should be prevented from logging in until one of the other sessions is invalidated.
                    .maxSessionsPreventsLogin(true)
                    //where should the user be redirected to when they try to make a request using an expired session
                    .expiredUrl("/login?session-expired");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().usersByUsernameQuery("query").

    }
}
