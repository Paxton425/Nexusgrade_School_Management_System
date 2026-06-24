package com.nexusgrade.app.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    // Catch specific "Page Not Found" errors
    @ExceptionHandler(NoResourceFoundException.class)
    public String handle404(Model model) {
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", "Oops! This resource doesn't exist.");
        return "error"; // Points to error.html
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandler(Model model){
        return handle404(model);
    }

    // Catch everything else (Generic Server Errors)
    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, Model model) {
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "The system hit a snag. Our school janitor is on it!");
        // We log the real error to the console for the developer (the Boxer),
        // but hide the scary details from the user (the Principal).
        System.err.println("System Error: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        // This turns that internal Java error into a clean 400 for your JS
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
