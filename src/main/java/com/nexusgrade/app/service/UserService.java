package com.nexusgrade.app.service;

import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch the user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Get the enum role string and format it with the mandatory 'ROLE_' prefix
        String formattedRole = "ROLE_" + user.getRole().name();

        // 3. Create a single-item authority list for Spring Security
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(formattedRole));

        // 4. Return the standard Spring Security User context wrapper
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}