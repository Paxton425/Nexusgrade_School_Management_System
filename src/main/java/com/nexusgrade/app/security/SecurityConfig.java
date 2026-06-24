package com.nexusgrade.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Allows you to use @PreAuthorize("hasRole('ADMIN')") on controllers
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Authorize Requests based on Roles
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/scripts/**", "/images/**", "/webjars/**").permitAll() // Public static resources
                        .requestMatchers("/login", "/register").permitAll() // Public Auth routes
                        .requestMatchers("/students/create", "/instructors/create").hasRole("ADMIN") // Restrict management tools
                        .anyRequest().authenticated() // Everything else requires logging in
                )

                // 2. Configure Form Login
                .formLogin(form -> form
                        .loginPage("/login") // custom login endpoint
                        .defaultSuccessUrl("/dashboard", true) // Send them to dashboard on success
                        .permitAll()
                )

                // 3. Configure Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) // Destroys the server-side session
                        .clearAuthentication(true)   // Clears credentials
                        .deleteCookies("JSESSIONID") // Wipes the browser cookie
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Essential for hashing user passwords safely before saving them to the DB
        return new BCryptPasswordEncoder(12);
    }
}