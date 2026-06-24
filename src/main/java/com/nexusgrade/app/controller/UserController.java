package com.nexusgrade.app.controller;

import com.nexusgrade.app.model.User;
import com.nexusgrade.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class UserController {

    UserRepository userRepository;
    UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal UserDetails loggedInUser, Model model) {
        // 1. Get the authenticated session username
        String username = loggedInUser.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found for account: " + username));

        model.addAttribute("user", user);

        // If the user is an instructor, their linked entity is already available via user.getInstructor()
        if (user.getRole() == User.Role.INSTRUCTOR && user.getInstructor() != null) {
            model.addAttribute("instructorDetails", user.getInstructor());
            model.addAttribute("assignedClasses", user.getInstructor().getSchoolClasses());
        }

        return "users/profile-view";
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(@AuthenticationPrincipal UserDetails loggedInUser, Model model) {
        String username = loggedInUser.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found: " + username));

        model.addAttribute("user", user);
        return "users/profile-edit";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails loggedInUser,
                                @ModelAttribute("user") User updatedData) {
        String username = loggedInUser.getUsername();
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found: " + username));

        // SECURE LAYER: Explicitly map only allowed modification fields.
        // We completely skip mapping updatedData.getEmployeeId() or updatedData.getRole()!
        existingUser.setFirstName(updatedData.getFirstName());
        existingUser.setMiddleName(updatedData.getMiddleName());
        existingUser.setLastName(updatedData.getLastName());
        existingUser.setEmail(updatedData.getEmail());
        existingUser.setPhone(updatedData.getPhone());
        existingUser.setGender(updatedData.getGender());

        userRepository.save(existingUser);

        return "redirect:/profile?success";
    }
}
