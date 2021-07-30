package demo.codingnomads.co.services.userservices;

import demo.codingnomads.co.models.securitymodels.CustomUserDetails;
import demo.codingnomads.co.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomUserPasswordService implements UserDetailsPasswordService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        customUserDetails.setPassword(passwordEncoder.encode(newPassword));

        userRepo.save(customUserDetails);

        return customUserDetails;
    }
}
