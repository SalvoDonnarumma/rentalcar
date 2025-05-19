package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;

import java.util.List;

public interface PrenotazioniService {

    public PrenotazioneDto SelById(Integer id);
    public List<PrenotazioneDto> SelByIdUtente(Integer idUtente, int pageNum, int recForPage);
    public List<PrenotazioneDto> SelByIdVeicolo(Integer idVeicolo, int pageNum, int recForPage);
}
