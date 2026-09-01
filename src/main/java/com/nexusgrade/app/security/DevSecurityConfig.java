package com.nexusgrade.app.security;

import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@Order(1)  // This runs FIRST before your main SecurityConfig
@Profile("dev")
public class DevSecurityConfig {

    private final UserRepository userRepository;

    public DevSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))  // ← THIS
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    FilterChain filterChain)
                            throws ServletException, IOException {

                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            try {
                                List<User> users = userRepository.findAll();
                                if (!users.isEmpty()) {
                                    User firstUser = users.get(0);
                                    String role = "ROLE_" + firstUser.getRole().name();
                                    List<SimpleGrantedAuthority> authorities = List.of(
                                            new SimpleGrantedAuthority(role)
                                    );

                                    Authentication auth = new UsernamePasswordAuthenticationToken(
                                            firstUser.getUsername(),
                                            null,
                                            authorities
                                    );

                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                }
                            } catch (Exception e) {
                                System.out.println("⚠️ Auto-login error: " + e.getMessage());
                            }
                        }

                        filterChain.doFilter(request, response);
                    }
                }, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
