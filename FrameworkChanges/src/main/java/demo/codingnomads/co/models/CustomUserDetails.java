package demo.codingnomads.co.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.TwoFactorPreference;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    public String username;

    @Column(nullable = false)
    private String password;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Long lastSignIn;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "userId",
            nullable = false
    )
    private Collection<CustomGrantedAuthority> authorities = new ArrayList<>();

    @Column(nullable = false)
    private boolean isTwoFactorAuthEnabled;

    @Column(nullable = false)
    private boolean isAccountNonExpired;

    @Column(nullable = false)
    private boolean isAccountNonLocked;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired;

    @Column(nullable = false)
    private boolean isEnabled;

    public CustomUserDetails(UserDetails userDetails) {
        username = userDetails.getUsername();
        password = userDetails.getPassword();
        authorities = userDetails.getAuthorities().stream()
                .map((GrantedAuthority authority)  -> (CustomGrantedAuthority)authority)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getTwoFactorAuthSendLocation() {
        return email;
    }

    @Override
    public HashMap<Integer, TwoFactorPreference> getTwoFactorAuthPreferences() {
        HashMap<Integer, TwoFactorPreference> preferences = new HashMap<>();
        TwoFactorPreference preference = new TwoFactorPreference(true, "VDYNVO3U5QGJ26KGEYIEOQ2J7AX5PF6T");
        preferences.put(1, preference);
        TwoFactorPreference preference1 = new TwoFactorPreference(false, "bensiegler@icloud.com");
        preferences.put(2, preference1);
        return preferences;
    }
}
