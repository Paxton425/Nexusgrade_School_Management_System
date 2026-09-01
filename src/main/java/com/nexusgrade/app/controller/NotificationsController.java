package com.nexusgrade.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/notifications")
public class NotificationsController {

    @GetMapping("")
    public String viewNotifications(){
        return "/notifications/notifications-view";
    }
}
