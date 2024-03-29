package com.bensiegler.models;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.TwoFactorPreference;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }


    public String getTwoFactorAuthSendLocation() {
        return email;
    }

    @Override
    public HashMap<Integer, TwoFactorPreference> getTwoFactorAuthPreferences() {
        HashMap<Integer, TwoFactorPreference> preferences = new HashMap<>();
        TwoFactorPreference preference = new TwoFactorPreference(true, "VDYNVO3U5QGJ26KGEYIEOQ2J7AX5PF6T");
        preferences.put(2, preference);
        TwoFactorPreference preference1 = new TwoFactorPreference(false, "bensiegler@icloud.com");
        preferences.put(1, preference1);
        return preferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
