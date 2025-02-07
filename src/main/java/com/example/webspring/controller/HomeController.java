package com.example.webspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String customerHome() {
        return "home";
    }

    @GetMapping("/admin-home")
    public String adminHome() {
        return "admin-home";
    }
}
