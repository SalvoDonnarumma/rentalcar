package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.entities.Veicolo;
import com.xantrix.webapp.services.PrenotazioniService;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.services.VeicoliService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private PrenotazioniService prenotazioniService;
    private Paging paging;
    private UtentiService utentiService;
    private VeicoliService veicoliService;
    private List<String> paramPaging;

    List<PagingData> pages = new ArrayList<>();

    private PrenotazioniController(PrenotazioniService prenotazioniService, Paging paging, UtentiService utentiService, VeicoliService veicoliService) {
        this.prenotazioniService = prenotazioniService;
        this.paging = paging;
        this.utentiService = utentiService;
        this.veicoliService = veicoliService;
    }

    //parametri;paging=0,0?selected=10#
    @GetMapping("/visualizzaprenot/{parametri}")
    public String GetPrenotazioniPageFromUtente(
            @MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri,
            @RequestParam(name= "id",required = true) Integer id,
            @RequestParam(name="campoFiltro",required = true) String campoFiltro,
            @RequestParam(name="selected", required  =false, defaultValue = "10") String selected,
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

        //SEZIONE PAGING
        int numVec = 0;
        int pageNum = 0;
        int recForPage = 0;
        int diffPage = 0;
        paramPaging = parametri.get("paging");
        if(paramPaging != null){
            try{
                pageNum = Integer.parseInt(paramPaging.get(0));
                recForPage = Integer.parseInt(selected);
                diffPage = Integer.parseInt(paramPaging.get(1));

                if(pageNum >= 1)
                    pageNum+= diffPage;
                else
                    pageNum = 1;
            } catch (NumberFormatException e){
                pageNum = 0;
                diffPage = 0;
                recForPage = 10;
            }
        }
        int realPage = (pageNum > 0) ? pageNum - 1 : 0;
        pages = paging.setPages(realPage, numVec);

        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("title", "PAGINA PRENOTAZIONI");
        model.addAttribute("pages", pages);

        if(prenotazioni.isEmpty())
            model.addAttribute("notFound", true);

        return "prenotazionipage";
    }

    @GetMapping("/modifica/{idPrenotazione}")
    public String GetModificaPrenotazione(
            @PathVariable("idPrenotazione") Integer id,
            Model model) {

        PrenotazioneDto prenotazione= prenotazioniService.SelById(id);
        VeicoloDto veicolo = veicoliService.SelById(prenotazione.getIdVeicolo());
        UtenteDto utente = utentiService.SelById(prenotazione.getIdUtente());

        model.addAttribute("datiprenotazione", prenotazione);
        model.addAttribute("title", "PAGINA MODIFICA PRENOTAZIONE");
        model.addAttribute("dativeicolo", veicolo);
        model.addAttribute("datiutente", utente);

        return "gestprenotazione";
    }

    @GetMapping("/aggiungi/{idVeicolo}")
    public String GetAggiungiPrenotazione(
            @PathVariable("idVeicolo") Integer idVeicolo,
            Model model) {

        VeicoloDto veicolo = veicoliService.SelById(idVeicolo);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UtenteDto utente = utentiService.SelByEmail(authentication.getName());

        model.addAttribute("title", "PAGINA MODIFICA PRENOTAZIONE");
        model.addAttribute("dativeicolo", veicolo);
        model.addAttribute("datiutente", utente);
        model.addAttribute("datiprenotazione", new PrenotazioneDto());

        return "gestprenotazione";
    }

    @PostMapping("/aggiungi")
    public String PostAggiungiPrenotazione(
            @ModelAttribute("dativec") PrenotazioneDto prenotazione,
            @RequestParam(name="idVeicolo", required=true) Integer idVeicolo,
            @RequestParam(name="idUtente", required=true) Integer idUtente,
            BindingResult result){

        prenotazione.setIdVeicolo(idVeicolo);
        prenotazione.setIdUtente(idUtente);
        prenotazione.setStato("IN ATTESA");
        prenotazioniService.InsertPrenotazione(prenotazione);

        return "redirect:/homepage/customerhomepage/parametri;paging=0,0?selected=10";
    }

    @GetMapping("/valida")
    public String GetValidaPrenotazione(
            @RequestParam(name = "idPrenotazione", required = true) String idPrenotazione,
            @RequestParam(name = "modificaStato", required = true) String modificaStato,
            Model model) {

        PrenotazioneDto prenotazioneDto = prenotazioniService.SelById(Integer.parseInt(idPrenotazione));
        if(modificaStato.equalsIgnoreCase("APPROVATO")){
            prenotazioneDto.setStato("APPROVATO");
        } else if(modificaStato.equalsIgnoreCase("DECLINATO")){
            prenotazioneDto.setStato("DECLINATO");
        } else {
            model.addAttribute("error", "Stato non valido!");
        }

        prenotazioniService.InsertPrenotazione(prenotazioneDto);

        return "redirect: /rentalcar/prenotazioni/visualizzaprenot/parametri;paging=0,0?selected=10&id="+prenotazioneDto.getIdUtente()+"&campoFiltro=ut";
    }
}
