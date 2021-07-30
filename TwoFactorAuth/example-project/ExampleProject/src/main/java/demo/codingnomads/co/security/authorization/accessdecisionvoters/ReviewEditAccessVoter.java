package demo.codingnomads.co.security.authorization.accessdecisionvoters;

import demo.codingnomads.co.models.Review;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public class ReviewEditAccessVoter implements AccessDecisionVoter {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        System.out.println(attribute.getAttribute());
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        return 0;
    }

    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(Review.class);
    }
}
