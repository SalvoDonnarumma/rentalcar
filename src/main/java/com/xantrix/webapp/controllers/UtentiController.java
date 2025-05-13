package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/homepage")
public class UtentiController {

    private UtentiService utentiService;

    private UtentiController(UtentiService utentiService) {
        this.utentiService = utentiService;
    }

    @GetMapping
    public String homepage(Model model) {
        List<UtenteDto> utenti = utentiService.SelAll();
        model.addAttribute("utenti", utenti);

        return "adminhomepage";
    }

}
