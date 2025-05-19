package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.services.PrenotazioniService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private PrenotazioniService prenotazioniService;
    private Paging paging;

    List<PagingData> pages = new ArrayList<>();

    private PrenotazioniController(PrenotazioniService prenotazioniService, Paging paging) {
        this.prenotazioniService = prenotazioniService;
        this.paging = paging;
    }
}
