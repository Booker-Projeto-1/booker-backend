package com.ufcg.booker.security;

import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public BookerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with login %s not found", email));
        }
        return new LoggedUser(user.get());
    }
}
