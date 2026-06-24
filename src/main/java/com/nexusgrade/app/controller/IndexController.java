package com.nexusgrade.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }
}
