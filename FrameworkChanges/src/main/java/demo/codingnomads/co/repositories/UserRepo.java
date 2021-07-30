package demo.codingnomads.co.repositories;

import demo.codingnomads.co.models.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<CustomUserDetails, Long> {

    CustomUserDetails findByUsernameOrEmail(String username, String email);
}
