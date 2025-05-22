package com.xantrix.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value="/")
    public String getWelcome(Model model) {
        model.addAttribute("intestazione", "Benvenuti nella root page della webapp Rental Car");
        model.addAttribute("saluti", "Saluti, sono il progetto della Fase Due del tuo stage");

        return "index";
    }

}
