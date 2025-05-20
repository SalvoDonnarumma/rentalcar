package com.xantrix.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login/form")
public class LoginController {

    @GetMapping
    public String GetLoginPage(Model model) {
        return "login";
    }
}
