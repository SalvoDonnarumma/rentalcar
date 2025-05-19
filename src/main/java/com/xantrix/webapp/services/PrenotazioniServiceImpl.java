package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.repository.PrenotazioniRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrenotazioniServiceImpl implements PrenotazioniService {

    @Autowired
    private PrenotazioniRepository prenotazioniRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PrenotazioneDto getPrenotazioneById(Integer id) {
        return null;
    }

    @Override
    public List<PrenotazioneDto> GetPrenotazioniByEmail(String filtro, int pageNum, int recForPage) {
        return List.of();
    }

    @Override
    public List<PrenotazioneDto> GetPrenotazioniByIdVeicolo(String filtro, int pageNum, int recForPage) {
        return List.of();
    }
}
