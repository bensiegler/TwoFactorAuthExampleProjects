package demo.codingnomads.co.services;

import demo.codingnomads.co.models.securitymodels.CustomGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GrantedAuthorityService {

    public ArrayList<CustomGrantedAuthority> getGrantedAuthoritiesByUserId(Long userId) {
        return new ArrayList<>();
    }
}
