package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.services.PrenotazioniService;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.services.VeicoliService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    @Autowired
    private PrenotazioniService prenotazioniService;
    @Autowired
    private Paging paging;
    @Autowired
    private UtentiService utentiService;
    @Autowired
    private VeicoliService veicoliService;

    List<PagingData> pages = new ArrayList<>();

    //parametri;paging=0,0?selected=10#
    @GetMapping("/visualizzaprenot/{parametri}")
    public String GetPrenotazioniPageFromUtente(
            @MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri,
            @RequestParam(name= "id",required = true) Integer id,
            @RequestParam(name="campoFiltro",required = true) String campoFiltro,
            @RequestParam(name="selected", required  =false, defaultValue = "10") String selected,
            @RequestParam(name="error", required = false, defaultValue = "false") Boolean error,
            Model model) {

        Set<PrenotazioneDto> prenotazioni = new HashSet<>();
        UtenteDto utente = null;
        VeicoloDto veicolo = null;
        if(campoFiltro.equalsIgnoreCase("ut")) {
            utente = utentiService.SelById(id);
            prenotazioni = utente.getPrenotazioni();
            model.addAttribute("tableTitle", "Lista prenotazione del costumer "+utente.getEmail());
        }
        else {
            veicolo = veicoliService.SelById(id);
            prenotazioni = veicolo.getPrenotazioni();
            model.addAttribute("tableTitle", "Lista prenotazione del veicolo "+veicolo.getTarga());
        }

        prenotazioniService.AggiornaStatoPrenotazione(prenotazioni);

        List<PrenotazioneDto> listaPrenotazioni = new ArrayList<>(prenotazioni);
        listaPrenotazioni = prenotazioni.stream()
                .sorted(Comparator.comparing(PrenotazioneDto::getIdPrenotazione))
                .toList();

        //SEZIONE PAGING
        int numPrenot = 0;
        int pageNum = 0;
        int recForPage = 0;
        int diffPage = 0;
        List<String> paramPaging = parametri.get("paging");
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
        numPrenot = prenotazioni.size();
        pages = paging.setPages(realPage, numPrenot);

        model.addAttribute("prenotazioni", listaPrenotazioni);
        model.addAttribute("title", "PAGINA PRENOTAZIONI");
        model.addAttribute("pages", pages);
        model.addAttribute("errorStatus", error);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("id", Integer.toString(id));
        model.addAttribute("campoFiltro", campoFiltro);

        if(prenotazioni.isEmpty())
            model.addAttribute("notFound", true);

        return "prenotazionipage";
    }

    @GetMapping("/modifica/{idPrenotazione}")
    public String GetModificaPrenotazione(
            @PathVariable("idPrenotazione") Integer id,
            @RequestParam(name = "errorDate", required = false) Boolean errorDate,
            Model model) {

        if(prenotazioniService.IsPrenotazioneInvalid(id))
            return "redirect:/homepage/customerhomepage/parametri;paging=0,0?selected=10&errorDate=true";

        PrenotazioneDto prenotazione= prenotazioniService.SelById(id);
        VeicoloDto veicolo = veicoliService.SelById(prenotazione.getIdVeicolo());
        UtenteDto utente = utentiService.SelById(prenotazione.getIdUtente());

        model.addAttribute("datiprenotazione", prenotazione);
        model.addAttribute("title", "PAGINA MODIFICA PRENOTAZIONE");
        model.addAttribute("dativeicolo", veicolo);
        model.addAttribute("datiutente", utente);
        model.addAttribute("dataOdierna", LocalDate.now().plusDays(3));
        model.addAttribute("errorDate", errorDate);

        return "gestprenotazione";
    }

    @GetMapping("/aggiungi/{idVeicolo}")
    public String GetAggiungiPrenotazione(
            @PathVariable("idVeicolo") Integer idVeicolo,
            @RequestParam(name = "errorDate", required = false) Boolean errorDate,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UtenteDto utente = utentiService.SelByEmail(authentication.getName());
        VeicoloDto veicolo = veicoliService.SelById(idVeicolo);

        model.addAttribute("title", "PAGINA MODIFICA PRENOTAZIONE");
        model.addAttribute("dativeicolo", veicolo);
        model.addAttribute("datiutente", utente);
        model.addAttribute("datiprenotazione", new PrenotazioneDto());
        model.addAttribute("dataOdierna", LocalDate.now().plusDays(2));
        model.addAttribute("errorDate", errorDate);

        return "gestprenotazione";
    }


    @PostMapping("/aggiungi")
    public String PostAggiungiPrenotazione(
            @ModelAttribute("dativec") PrenotazioneDto prenotazione,
            @RequestParam(name="idVeicolo", required=true) Integer idVeicolo,
            @RequestParam(name="idUtente", required=true) Integer idUtente,
            BindingResult result, RedirectAttributes redirectAttributes){

        if(prenotazioniService.IsPrenotazioneInvalid(prenotazione.getDataInizio())) {
            redirectAttributes.addAttribute("errorDate", true);
            if(prenotazione.getIdPrenotazione()==null)
                return "redirect: /rentalcar/prenotazioni/aggiungi/" + idVeicolo;
            else
                return "redirect: /rentalcar/prenotazioni/modifica/" + prenotazione.getIdPrenotazione();
        }

        if(prenotazione.getDataInizio().after(prenotazione.getDataFine())) {
            redirectAttributes.addAttribute("errorDate", true);
            if(prenotazione.getIdPrenotazione()==null)
                return "redirect: /rentalcar/prenotazioni/aggiungi/" + idVeicolo;
            else
                return "redirect: /rentalcar/prenotazioni/modifica/" + prenotazione.getIdPrenotazione();
        }

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
            return "redirect: /rentalcar/prenotazioni/visualizzaprenot/parametri;paging=0,0?selected=10&id="+prenotazioneDto.getIdUtente()+"&campoFiltro=ut&error=true";
        }

        prenotazioniService.InsertPrenotazione(prenotazioneDto);

        return "redirect: /rentalcar/prenotazioni/visualizzaprenot/parametri;paging=0,0?selected=10&id="+prenotazioneDto.getIdUtente()+"&campoFiltro=ut";
    }

    @GetMapping("/elimina/{id}")
    public String eliminaPrenotazione(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

        if(prenotazioniService.IsPrenotazioneFromThePast(id)) { //Se è una prenotazione passata
            prenotazioniService.EliminaPrenotazione(id);
            return "redirect:/homepage/customerhomepage/parametri;paging=0,0?selected=10&errorDate=false&confirmDelete=true";
        }
        else if( prenotazioniService.IsPrenotazioneInvalid(id) ) //Se è una prenotazione non scaduta
            return "redirect:/homepage/customerhomepage/parametri;paging=0,0?selected=10&errorDate=true&confirmDelete=false";

        prenotazioniService.EliminaPrenotazione(id);
        return "redirect:/homepage/customerhomepage/parametri;paging=0,0?selected=10&errorDate=false&confirmDelete=true";
    }
}
