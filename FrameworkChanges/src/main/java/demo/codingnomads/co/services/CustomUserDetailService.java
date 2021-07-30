package demo.codingnomads.co.services;

import demo.codingnomads.co.exceptions.NoSuchUserException;
import demo.codingnomads.co.models.CustomUserDetails;
import demo.codingnomads.co.repositories.UserRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails user = userRepo.findByUsernameOrEmail(username, username);

        if(user == null) {
            throw new UsernameNotFoundException(username + " is not a valid username! Check for typos and try again.");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public CustomUserDetails getByUserId(Long userId) throws NoSuchUserException {
       CustomUserDetails user = userRepo.getOne(userId);

       if(null == user) {
           throw new NoSuchUserException("No user could be found with that ID");
       }

       return (CustomUserDetails) Hibernate.unproxy(user);
    }

    public void updateUser(CustomUserDetails userDetails) {
        userRepo.save(userDetails);
    }
}
