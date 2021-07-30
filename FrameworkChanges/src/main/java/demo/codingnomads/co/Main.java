package demo.codingnomads.co;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.TwoFactorPreference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.web.authentication.twofa.services.TotpService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class Main {

    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Main.class);
    }
}
