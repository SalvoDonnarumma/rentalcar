package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.services.PrenotazioniService;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.services.VeicoliService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private PrenotazioniService prenotazioniService;
    private Paging paging;
    private UtentiService utentiService;
    private VeicoliService veicoliService;

    List<PagingData> pages = new ArrayList<>();

    private PrenotazioniController(PrenotazioniService prenotazioniService, Paging paging, UtentiService utentiService, VeicoliService veicoliService) {
        this.prenotazioniService = prenotazioniService;
        this.paging = paging;
        this.utentiService = utentiService;
        this.veicoliService = veicoliService;
    }

    @GetMapping("/visualizza")
    public String GetPrenotazioniPageFromUtente(
            @RequestParam(name= "id",required = true) Integer id,
            @RequestParam(name="campoFiltro",required = true) String campoFiltro,
            Model model) {

        Set<PrenotazioneDto> prenotazioni = new HashSet<>();
        UtenteDto utente = null;
        VeicoloDto veicolo = null;
        if(campoFiltro.equalsIgnoreCase("ut")) {
            utente = utentiService.SelById(id);
            System.out.println("Lista di prenotazioni che interessano l'utente: "+utente.getEmail());
            utente.getPrenotazioni().forEach(System.out::println);
            prenotazioni = utente.getPrenotazioni();
        }
        else {
            veicolo = veicoliService.SelById(id);
            System.out.println("Lista di prenotazioni che interessano il veicolo: "+veicolo.getTarga());
            veicolo.getPrenotazioni().forEach(System.out::println);
            prenotazioni = veicolo.getPrenotazioni();
        }

        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("title", "PAGINA PRENOTAZIONI");

        return "prenotazionipage";
    }
}
