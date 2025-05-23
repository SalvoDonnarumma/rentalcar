package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.entities.Veicolo;

import java.util.List;

public interface PrenotazioniService {

    public PrenotazioneDto SelById(Integer id);
    public List<PrenotazioneDto> SelByIdUtente(Integer idUtente, int pageNum, int recForPage, String dataInit, String dataFin);
    public List<PrenotazioneDto> SelByIdVeicolo(Integer idVeicolo, int pageNum, int recForPage);
    public List<PrenotazioneDto> SelByVeicolo(Veicolo veicolo, int pageNum, int recForPage);

    public void InsertPrenotazione(PrenotazioneDto prenotazione);

    void EliminaPrenotazione(Integer id);
}
