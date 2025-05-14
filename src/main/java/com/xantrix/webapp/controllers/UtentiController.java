package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homepage")
public class UtentiController {

    private UtentiService utentiService;
    private Paging paging;

    List<PagingData> pages = new ArrayList<>();

    private UtentiController(UtentiService utentiService) {
        this.utentiService = utentiService;
    }

    @GetMapping
    public String homepage(
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            Model model) {


        int pageNum = 0;
        int recForPage = Integer.parseInt(selected);

        try{
            recForPage = Integer.parseInt(selected);
        } catch (NumberFormatException e){
            recForPage = 10;
        }

        List<UtenteDto> utenti = utentiService.SearchCostumers(pageNum, recForPage);

        int numCos = utentiService.NumRecords();
        boolean notFound = utenti.isEmpty();

        model.addAttribute("utenti", utenti);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("pages", pages);
        model.addAttribute("notFound", notFound);

        return "adminhomepage";
    }

    ///homepage/search/parametri(paging=${pageNum}, offset=-1, selected=${recPage})}
    @GetMapping(value="/search/{parametri}")
    public String GetArticoliWithPar(
            @MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            ModelMap model){
        int numArt = 0;
        int pageNum = 0;
        int recForPage = 10;
        int diffPage = 0;

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

        /*
        //PARAMETRI FILTRI AGGIUNTIVI
        List<String> ExFilter = parametri.get("exFilter");
        if(ExFilter != null){
            try{
                System.out.println(String.format("status: %s", ExFilter.get(0)));
                System.out.println(String.format("categoria: %s", ExFilter.get(1)));
            } catch (Exception e){
            }
        }
         */

        List<UtenteDto> utenti =utentiService.SearchCostumers(pageNum, recForPage);
        numArt = utentiService.NumRecords();

        pages = paging.setPages(pageNum, numArt);
        model.addAttribute("utenti", utenti);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("pages", pages);

        return "adminhomepage";
    }

    /*
    @GetMapping
    public String SearchItem(
            @RequestParam(name = "filtro") String filtro,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            Model model) {

        int page = 0;
        int recForPage = Integer.parseInt(selected);

        try{
            recForPage = Integer.parseInt(selected);
        } catch (NumberFormatException e){
            recForPage = 10;
        }

        List<UtentiDto> articoli = utentiService.SearchArticoli(filtro, pageNum, recForPage);

        int numArt = articoliService.NumRecords(filtro);
        boolean notFound = articoli.isEmpty();

        model.addAttribute("articoli", articoli);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("filtro", filtro);
        model.addAttribute("pages", pages);
        model.addAttribute("notFound", notFound);

        return "articoli";


    }
    */


}
