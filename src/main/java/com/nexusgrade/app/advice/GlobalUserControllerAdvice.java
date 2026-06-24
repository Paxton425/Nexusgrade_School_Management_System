package com.nexusgrade.app.advice;

import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserControllerAdvice {

    private final UserRepository userRepository;

    public GlobalUserControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * The @ModelAttribute annotation tells Spring Boot:
     * "Run this method before rendering ANY view, and attach the result to the template model."
     * This instantly makes the "${currentUser}" variable available to your shared layout fragment.
     */
    @ModelAttribute("currentUser")
    public User addCurrentUserToModel() {
        // 1. Ask Spring Security who is currently logged in via the session token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Safeguard: If nobody is logged in (or it's an anonymous public page), return null
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        // 3. Get the login username (the email/employee ID used at login, e.g., sthandiwe@email.com)
        String currentUsername = authentication.getName();

        // 4. Query your database to get the rich User profile object (with firstNames, lastName, role)
        return userRepository.findByUsername(currentUsername).orElse(null);
    }
}
