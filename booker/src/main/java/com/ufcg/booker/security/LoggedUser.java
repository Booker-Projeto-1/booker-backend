package com.ufcg.booker.security;

import com.ufcg.booker.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class LoggedUser implements UserDetails {

    private final User user;
    private final org.springframework.security.core.userdetails.User springUserDetails;
    public LoggedUser(User user) {
        this.user = user;
        this.springUserDetails = new
                org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of());

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return springUserDetails.getAuthorities();
    }

    @Override
    public String getPassword() {
        return springUserDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return springUserDetails.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return springUserDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return springUserDetails.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return springUserDetails.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return springUserDetails.isEnabled();
    }

    public User get() {
        return this.user;
    }
}
