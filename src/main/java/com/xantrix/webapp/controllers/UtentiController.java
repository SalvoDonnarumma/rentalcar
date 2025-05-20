package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homepage")
public class UtentiController {

    private UtentiService utentiService;
    private Paging paging;
    private PasswordEncoder passwordEncoder;

    List<PagingData> pages = new ArrayList<>();

    private UtentiController(UtentiService utentiService, Paging paging, PasswordEncoder passwordEncoder) {
        this.utentiService = utentiService;
        this.paging = paging;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String homepage(
            Authentication authentication,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            Model model) {

        System.out.println(">>> Utente autenticato: " + authentication.getName() + "con ruolo"+ authentication.getAuthorities());

        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_ADMIN")) {
                    return "redirect:/homepage/search/parametri;paging=0,0?selected=10";
                } else if (role.equals("ROLE_USER")) {
                    return "redirect:/homepage/customerhomepage";
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
            BindingResult result){

        utente.setRuolo("costumer");
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utentiService.InsertCostumer(utente);
        return "redirect:/homepage/search/parametri;paging=0,0?filtro="+utente.getEmail()+"&campoFiltro=email";
    }

    @GetMapping(value="/modifica/{id}")
    public String ModificaCostumer(
            Model model,
            @PathVariable("id") Integer id){

        UtenteDto utente = utentiService.SelById(id);
        model.addAttribute("datiutente", utente);
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

    @GetMapping(value="/customerhomepage")
    public String GetCustomerPage(Model model){
        model.addAttribute("title", "CUSTOMER HOMEPAGE");

        return "costumerhomepage";
    }
}
