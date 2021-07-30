package demo.codingnomads.co.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.port}")
    private String port;

    @Value("${mail.smtp.tls}")
    private boolean smtpTLS;

    @Value("${mail.smtp.auth}")
    private boolean smtpAuth;

    @Bean
    public Session startMailSession() {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", smtpTLS);
        props.put("mail.smtp.auth", smtpAuth);

        return Session.getDefaultInstance(props);
    }

}
