package demo.codingnomads.co.config;

import demo.codingnomads.co.security.authentication.authenticationproviders.CustomUsernameAndPasswordAuthenticationProvider;
import demo.codingnomads.co.services.userservices.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    @Autowired
    ApplicationContext context;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUsernameAndPasswordAuthenticationProvider customUsernameAndPasswordAuthenticationProvider() {
        CustomUsernameAndPasswordAuthenticationProvider authenticationProvider = new CustomUsernameAndPasswordAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailService);
        return authenticationProvider;
    }




//    @Bean
//    public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider() {
//        TwoFactorAuthenticationProvider provider = new TwoFactorAuthenticationProvider();
//        provider.setCodeService(twoFactorAuthService());
//        provider.setUserDetailsService(customUserDetailService);
//        return provider;
//    }
//
//    @Autowired
//    DataSource dataSource;
//
//    @Autowired
//    CustomUserDetailService customUserDetailService;

//    @Bean
//    public TwoFactorCodeService twoFactorAuthService() {
//        DatabaseTwoFactorCodeRepository codeRepository = new DatabaseTwoFactorCodeRepository();
//        codeRepository.setDataSource(dataSource);
//        return new DefaultTwoFactorCodeService(codeRepository);
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        ObjectPostProcessor<Object> objectPostProcessor = context.getBean(ObjectPostProcessor.class);
//        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
//
//        builder.authenticationProvider(customUsernameAndPasswordAuthenticationProvider());
//        builder.authenticationProvider(twoFactorAuthenticationProvider());
//
//        return builder.build();
//    }
    //    @Bean

//    public TwoFactorAuthInitiationFilter twoFactorAuthInitiationFilter() throws Exception {
//        AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/login", "POST");
//        TwoFactorAuthInitiationFilter twoFactorAuthInitiationFilter = new TwoFactorAuthInitiationFilter(antPathRequestMatcher, authenticationManager());
//
//        //configure success and failure behavior
//
//        return twoFactorAuthInitiationFilter;
//    }

//    @Bean
//    public TwoFactorAuthConfirmationFilter twoFactorAuthConfirmationFilter() throws Exception {
//        AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/2FA/authenticate", "POST");
//        TwoFactorAuthConfirmationFilter twoFactorAuthConfirmationFilter = new TwoFactorAuthConfirmationFilter(antPathRequestMatcher, authenticationManager());
//
//        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
//        successHandler.setDefaultTargetUrl("/recipes");
//        twoFactorAuthConfirmationFilter.setAuthenticationSuccessHandler(successHandler);
//
//        //configure success and failure behavior
//        return twoFactorAuthConfirmationFilter;
//    }

//    @Bean
//    public TwoFactorAuthenticationFilter twoFactorAuthenticationFilter() throws Exception {
//        TwoFactorAuthenticationFilter factorAuthenticationFilter = new TwoFactorAuthenticationFilter(new AntPathRequestMatcher("/login"), authenticationManager());
//        factorAuthenticationFilter.setTwoFactorProcessingUrl("/2FA/authenticate");
//        factorAuthenticationFilter.setTwoFactorRedirectUrl("/2FA");
//        factorAuthenticationFilter.setTwoFactorAuthCodeFormKey("code");
//        factorAuthenticationFilter.setSendStrategy(new AwsEmailSendStrategy());
//        factorAuthenticationFilter.setSendFailureStrategy(new NullSendFailureStrategy());
//
//        DatabaseTwoFactorCodeRepository codeRepository = new DatabaseTwoFactorCodeRepository();
//        codeRepository.setDataSource(dataSource);
//
//        DefaultTwoFactorCodeService codeService = new DefaultTwoFactorCodeService(codeRepository);
//        factorAuthenticationFilter.setCodeService(codeService);
//
//        return factorAuthenticationFilter;
//    }
}
