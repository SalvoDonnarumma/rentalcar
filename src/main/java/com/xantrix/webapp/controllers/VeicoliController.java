package com.xantrix.webapp.controllers;

import com.xantrix.webapp.dtos.PagingData;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.services.VeicoliService;
import com.xantrix.webapp.utils.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/parcoauto")
public class VeicoliController {

    @Autowired
    private VeicoliService veicoliService;
    @Autowired
    private Paging paging;

    List<PagingData> pages = new ArrayList<>();


    @GetMapping
    public String GetParcoAutoPage(
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            Model model) {

        return "redirect:/parcoauto/search/parametri;paging=0,0?selected=10";
    }

    //http://localhost:8080/alphashop/parcoauto/cerca/parametri;paging=0,1?filtro=acqua&selected=10
    @GetMapping(value="/search/{parametri}")
    public String GetVeicoliWithPar(
            @MatrixVariable(pathVar = "parametri") Map<String, List<String>> parametri,
            @RequestParam(name = "selected", required = false, defaultValue = "10") String selected,
            @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro,
            @RequestParam(name = "campoFiltro", required = false) String campoFiltro,
            ModelMap model){
        int numVec = 0;
        int pageNum = 0;
        int recForPage = 10;
        int diffPage = 0;
        boolean notFound = true;

        //SEZIONE PAGING
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

        List<VeicoloDto> veicoli = veicoliService.SearchVeicoli(filtro, campoFiltro, realPage, recForPage);
        numVec = veicoliService.NumRecords();
        pages = paging.setPages(realPage, numVec);

        if(!veicoli.isEmpty()){
             notFound = false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("veicoli", veicoli);
        model.addAttribute("notFound", notFound);
        model.addAttribute("title", "PARCO AUTO");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("recPage", recForPage);
        model.addAttribute("pages", pages);
        model.addAttribute("filtro", filtro);
        model.addAttribute("campoFiltro", campoFiltro);

        return "parcoauto";
    }

    @GetMapping(value = "/aggiungi")
    public String GetGestVeicoliPage(Model model){
        model.addAttribute("title", "AGGIUNTA VEICOLO");
        model.addAttribute("dativec", new VeicoloDto());

        return "gestveicoli";
    }

    @PostMapping(value = "/aggiungi")
    public String GetInsVeicolo(
            @ModelAttribute("dativec") VeicoloDto veicolo,
            BindingResult result){

        if(veicolo.getId()!=null)
            System.out.println("Veicolo da modificare: "+veicolo);
        veicoliService.InsertVeicolo(veicolo);
        return "redirect:/parcoauto/search/parametri;paging=0,0?filtro="+veicolo.getTarga()+"&campoFiltro=targa";
    }

    @GetMapping(value = "/elimina/{targa}")
    public String GetElimina(
            @PathVariable("targa") String targa,
            ModelMap model){
        try{
            if(!targa.isEmpty())
                veicoliService.DelVeicoloByTarga(targa);
        } catch (Exception ex){
            throw new RuntimeException("Errore eliminazione veicolo",ex);
        }

        return "redirect:/parcoauto/search/parametri;paging=0,0?filtro="+targa+"&campoFiltro=targa";
    }


    @GetMapping(value = "/modifica/{targa}")
    public String GetModificaPage(
            Model model,
            @PathVariable("targa") String targa){

        VeicoloDto veicoloDto = veicoliService.SelByTarga(targa);

        model.addAttribute("title", "MODIFICA VEICOLO");
        model.addAttribute("dativec", veicoloDto);

        return "gestveicoli";
    }

}
