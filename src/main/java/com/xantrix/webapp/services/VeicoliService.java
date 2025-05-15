package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.entities.Veicolo;
import java.util.List;

public interface VeicoliService {

    public List<VeicoloDto> SelAll();

    List<VeicoloDto> SearchVeicoli(String filtro, String campoFiltro, int realPage, int recForPage);

    int NumRecords();
}
