package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.entities.Utente;

import java.util.List;

public interface UtentiService {

    public List<UtenteDto> SelAll();

    public List<UtenteDto> SearchUtenti(String filtro, int pageNum, int recForPage);

    List<UtenteDto> SearchCostumers(int pageNum, int recForPage);

    int NumRecords();
}
