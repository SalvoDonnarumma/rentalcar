package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.services.UtentiService;
import com.xantrix.webapp.utils.Paging;
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

    private UtentiController(UtentiService utentiService, Paging paging) {
        this.utentiService = utentiService;
        this.paging = paging;
    }

    @GetMapping
    public String homepage(
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            Model model) {

        return "redirect:/homepage/search/parametri;paging=0,0?selected=10";
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
}
