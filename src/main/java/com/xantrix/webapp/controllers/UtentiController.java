package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.services.PrenotazioniService;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homepage")
public class UtentiController {

    @Autowired
    private UtentiService utentiService;
    @Autowired
    private PrenotazioniService prenotazioniService;
    @Autowired
    private Paging paging;
    @Autowired
    private PasswordEncoder passwordEncoder;

    List<PagingData> pages = new ArrayList<>();


    @GetMapping
    public String homepage(
            Authentication authentication,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            Model model) {

        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_ADMIN")) {
                    return "redirect:/homepage/search/parametri;paging=0,0?selected=10";
                } else if (role.equals("ROLE_USER")) {
                    return "redirect:/homepage/customerhomepage/parametri;paging=0,0?selected=10";
                }
            }
        }

        return "login";
    }

    //http://localhost:8080/alphashop/articoli/cerca/parametri;paging=0,1?filtro=acqua&selected=10
    @GetMapping(value="/search/{parametri}")
    public String GetCostumersWithPar(
            @MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro,
            @RequestParam(name = "campoFiltro", required = false) String campoFiltro,
            ModelMap model){
        int numCos = 0;
        int pageNum = 0;
        int recForPage = 10;
        int diffPage = 0;
        boolean notFound = true;

        //PARAMETRI PAGING
        List<String> paramPaging = parametri.get("paging");
        if(paramPaging != null){
            try{
                pageNum = Integer.parseInt(paramPaging.get(0)); //Numero della pagina
                recForPage = Integer.parseInt(selected); //Record per pagina
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
        List<UtenteDto> utenti = utentiService.SearchCostumers(filtro, campoFiltro, realPage, recForPage);
        numCos = utentiService.NumRecords();

        if(!utenti.isEmpty()){
            notFound = false;
        }

        pages = paging.setPages(pageNum, numCos);
        model.addAttribute("utenti", utenti);
        model.addAttribute("notFound", notFound);
        model.addAttribute("title", "HOMEPAGE ADMIN");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("pages", pages);
        model.addAttribute("filtro", filtro);
        model.addAttribute("campoFiltro", campoFiltro);

        return "adminhomepage";
    }

    @GetMapping(value = "/aggiungi")
    public String AddCostumerPage(Model model) {
        model.addAttribute("title", "AGGIUNTA COSTUMER");
        model.addAttribute("datiutente", new UtenteDto());

        return "gestutenti";
    }

    @PostMapping(value = "/aggiungi")
    public String AddCostumer(
            @ModelAttribute("datiutenti") UtenteDto utente,
            BindingResult result, Model model, RedirectAttributes redirectAttributes){

        if(!utente.getRuolo().equalsIgnoreCase("admin"))
            utente.setRuolo("user");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ruolo = authentication.getAuthorities().toString();

        if(ruolo.equalsIgnoreCase("[ROLE_USER]") && utente.getId()==null){ //Un costumer sta provando ad inserire un nuovo costumer, azione non permessa
            return "redirect: /rentalcar/login/form?forbidden";
        } else if(ruolo.equalsIgnoreCase("[ROLE_ADMIN]") && utente.getId()==null){//Un admin sta provando ad inserire un nuovo costumer.
            //Controllo email già esistente
            if(utentiService.EmailExists(utente.getEmail())){
                redirectAttributes.addFlashAttribute("duplicateEmail", true);
                return "redirect:/homepage/aggiungi";
            }
            utentiService.InsertCostumer(utente);
            return "redirect:/homepage/search/parametri;paging=0,0?filtro="+utente.getEmail()+"&campoFiltro=email";
        }

        if( (ruolo.equalsIgnoreCase("[ROLE_ADMIN]") || ruolo.equalsIgnoreCase("[ROLE_USER]") ) && utente.getId()!=null) { //Modificare il profilo è concetto a entrambi
            //Controllo vecchia password valida
            String hashedPasswordOnDB = utentiService.SelById(utente.getId()).getPassword();
            if(!passwordEncoder.matches(utente.getVecchiaPassword(), hashedPasswordOnDB)){
                redirectAttributes.addFlashAttribute("errorOldPassword", true);
                return "redirect:/homepage/modifica/"+utente.getId();
            }

            if(!utente.getPassword().isBlank()) {
                if (!utente.getPassword().equals(utente.getConfermaPassword())) {
                    redirectAttributes.addFlashAttribute("errorConfirmPassword", true);
                    return "redirect:/homepage/modifica/" + utente.getId();
                }
                utente.setPassword(passwordEncoder.encode(utente.getPassword()));
            } else
                utente.setPassword(hashedPasswordOnDB);

            //Controlla email valida
            if(utentiService.EmailExists(utente.getEmail(), utente.getId())){
                System.out.println("Email Already Exists");
                redirectAttributes.addFlashAttribute("duplicateEmail", true);
                return "redirect:/homepage/modifica/"+utente.getId();
            }

            utentiService.InsertCostumer(utente);
        }

        if(ruolo.equalsIgnoreCase("[ROLE_ADMIN]"))
            return "redirect:/homepage/search/parametri;paging=0,0?filtro="+utente.getEmail()+"&campoFiltro=email";
        else
            return "redirect:/login/form?logout";
    }

    @GetMapping(value="/modifica/{id}")
    public String ModificaCostumer(
            Model model,
            @PathVariable("id") Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedEmail = authentication.getName();
        String ruolo = authentication.getAuthorities().toString();

        UtenteDto utenteDaModificare = utentiService.SelById(id);

        if(!loggedEmail.equals(utenteDaModificare.getEmail()) && ruolo.equals("[ROLE_USER]")) //Un costumer sta cercando di accedere alla modifiche di un altro costumer
            return "redirect: /rentalcar/login/form?forbidden";

        model.addAttribute("datiutente", utenteDaModificare);
        model.addAttribute("title", "MODIFICA COSTUMER");

        return "gestutenti";
    }

    @GetMapping(value = "/elimina/{id}")
    public String DeleteCostumer(
            @PathVariable("id") Integer id,
            ModelMap model){

        try{
            utentiService.DeleteCostumer(id);
        }catch(Exception e){
            throw new RuntimeException("Errore eliminazione Costumer", e);
        }

        return "redirect:/homepage/search/parametri;paging=0,0?filtro="+id+"&campoFiltro=id";
    }

    @GetMapping(value="/customerhomepage/{parametri}")
    public String GetCustomerPage(
            Authentication authentication,
            @MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            @RequestParam(name = "dataInizio", required = false, defaultValue = "") String dataInit,
            @RequestParam(name = "dataFine", required = false, defaultValue = "") String dataFin,
            Model model){

        String email = authentication.getName();
        System.out.println(">>>Utente loggato: "+email);
        UtenteDto utenteLogged = utentiService.SelByEmail(email);

        int numCos = 0;
        int pageNum = 0;
        int recForPage = 10;
        int diffPage = 0;
        boolean notFound = true;

        //PARAMETRI PAGING
        List<String> paramPaging = parametri.get("paging");

        if(paramPaging != null){
            try{
                pageNum = Integer.parseInt(paramPaging.get(0)); //Numero della pagina
                recForPage = Integer.parseInt(selected); //Record per pagina
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

        List<PrenotazioneDto> prenotazioni = prenotazioniService.SelByIdUtente(utenteLogged.getId(), realPage, recForPage, dataInit, dataFin);
        if(!prenotazioni.isEmpty()){
            notFound = false;
        }

        numCos = prenotazioni.size();
        pages = paging.setPages(pageNum, numCos);

        model.addAttribute("title", "CUSTOMER HOMEPAGE");
        model.addAttribute("email", utenteLogged.getEmail());
        model.addAttribute("pageNum", realPage);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("pages", pages);
        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("dataInit", dataInit);
        model.addAttribute("dataFin", dataFin);

        return "costumerhomepage";
    }

    @GetMapping("/profiloutente")
    public String GetProfiloUtentePage(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UtenteDto utente = utentiService.SelByEmail(email);
        model.addAttribute("datiutente", utente);
        model.addAttribute("title", "MODIFICA PROFILO UTENTE");
        return "gestutenti";
    }
}
