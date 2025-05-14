package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.entities.Utente;

import java.util.List;

public interface UtentiService {

    public List<UtenteDto> SelAll();

    List<UtenteDto> SearchCostumers(String filtro, String campoFiltro, int pageNum, int recForPage);

    int NumRecords();
}
