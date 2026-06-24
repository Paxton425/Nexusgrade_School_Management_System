package com.nexusgrade.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @RequestMapping("/login")
    public String Login(){
        return "authentication/login";
    }

    @PostMapping("/logout")
    public String performLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 1. Check if there is an active logged-in session to terminate
        if (authentication != null) {
            // 2. Programmatically clear the Security context, invalidate the HTTP session, and wipe cookies
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // 3. Redirect back to your login page with a clean query string signal parameter
        return "redirect:/login?logout=true";
    }
}
