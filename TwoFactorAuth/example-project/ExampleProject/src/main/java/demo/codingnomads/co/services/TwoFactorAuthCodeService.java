package demo.codingnomads.co.services;

import demo.codingnomads.co.exceptions.security.NoSuchTwoFactorCodeException;
import demo.codingnomads.co.models.securitymodels.CustomUserDetails;
import demo.codingnomads.co.models.securitymodels.TwoFactorCode;
import demo.codingnomads.co.repositories.TwoFactorCodeRepo;
import demo.codingnomads.co.services.userservices.CustomUserDetailService;
import demo.codingnomads.co.util.EmailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;

@Service
public class TwoFactorAuthCodeService {

    @Autowired
    TwoFactorCodeRepo repo;

    @Autowired
    CustomUserDetailService userDetailsService;

    @Autowired
    GrantedAuthorityService grantedAuthorityService;

    @Autowired
    EmailHelper emailHelper;

    public TwoFactorCode saveTwoFactorCode(TwoFactorCode twoFactorCode) {
        return repo.save(twoFactorCode);
    }

    public TwoFactorCode getCodeByCookieValue(String value) throws NoSuchTwoFactorCodeException {
        Optional<TwoFactorCode> twoFactorCode = repo.findByCookie(value);

        if(twoFactorCode.isEmpty()) {
            throw new NoSuchTwoFactorCodeException("No 2FA code could be found for that cookie! Someone messing with cookies...");
        }

        return twoFactorCode.get();
    }

    public void deleteCodeById(Long id) {
        repo.deleteById(id);
    }

    public boolean isAwaitingCode(String cookieValue) {
        try {
            getCodeByCookieValue(cookieValue);
            return true;
        }catch (NoSuchTwoFactorCodeException e) {
            return false;
        }
    }

    public void authenticateTwoFactorAuthCode(String codeToCheck, TwoFactorCode realCode) {
        //pull code from String (code=589358 to 589583)
        codeToCheck = codeToCheck.substring(codeToCheck.indexOf("=") + 1);

        //if codes don't match throw exception
        if(!realCode.getCode().equals(codeToCheck)) {
            throw new BadCredentialsException("The code you entered is invalid");
        }

        long timeCodeGenerated = realCode.getTimeCreated().getTime();

        //if current time is greater than 15 minutes after generated throw exception
        if(System.currentTimeMillis() > timeCodeGenerated + 900000) {
            throw new CredentialsExpiredException("That code has expired");
        }

        //remove record
        repo.deleteById(realCode.getId());
    }

    public boolean isAlreadyAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getClass().equals(UsernamePasswordAuthenticationToken.class) && auth.isAuthenticated();
    }

    @Value("${mail.send-from.address}")
    private String sendFromAddress;

    @Value("${mail.send-from.name}")
    private String sendFromName;

    public void send2FAEmail(CustomUserDetails user, String twoFactorAuthCode) throws MessagingException, InvalidPropertiesFormatException {
        MimeMessage msg;
        try {
            msg = new MimeMessage(emailHelper.getSession());
            msg.setFrom(new InternetAddress(sendFromAddress, sendFromName));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            msg.setSubject("Your Requested Two Factor Authentication Code");
            msg.setContent(String.join(
                    System.getProperty("line.separator"),
                    "<h2>Here is your 2FA Code</h2>",
                    "<p>" + twoFactorAuthCode + "</p>"
            ), "text/html");

        }catch (UnsupportedEncodingException e) {
            throw new InvalidPropertiesFormatException("Double check the send-from properties in application.properties");
        }

        new Thread(() -> {
            try {
                emailHelper.sendEmail(msg);
            }catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public String generateTwoFactorAuthCode() {

        StringBuilder code = new StringBuilder();

        for(int i = 0; i < 6; i++) {
            int newDigit = (int) (Math.random() * 10);
            code.append(newDigit);
        }

        return code.toString();
    }
}
