package demo.codingnomads.co.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

@Component
public class EmailHelper {

    @Autowired
    Session session;

    @Value("${mail.host.address}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    public void sendEmail(MimeMessage msg) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(host, username, password);
        transport.sendMessage(msg, msg.getAllRecipients());
    }

    public Session getSession() {
        return session;
    }
}
