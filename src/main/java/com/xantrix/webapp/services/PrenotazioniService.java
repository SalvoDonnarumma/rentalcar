package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.entities.Prenotazione;

import java.util.List;

public interface PrenotazioniService {

    public PrenotazioneDto getPrenotazioneById(Integer id);
    public List<PrenotazioneDto> GetPrenotazioniByEmail(String filtro, int pageNum, int recForPage);
    public List<PrenotazioneDto> GetPrenotazioniByIdVeicolo(String filtro, int pageNum, int recForPage);
}
