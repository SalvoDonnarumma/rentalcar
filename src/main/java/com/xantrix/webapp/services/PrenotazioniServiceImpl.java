package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.entities.Prenotazione;
import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.repository.PrenotazioniRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private List<PrenotazioneDto> ConvertToDto(List<Prenotazione> prenotazioni) {

        return prenotazioni
                .stream()
                .map(source -> modelMapper.map(source, PrenotazioneDto.class))
                .collect(Collectors.toList());
    }

    private PrenotazioneDto ConvertToDto(Prenotazione prenotazione) {
        PrenotazioneDto prenotazioneDto = null;
        if(prenotazione != null) {
            prenotazioneDto = modelMapper.map(prenotazione, PrenotazioneDto.class);
        }
        return prenotazioneDto;
    }

    private Prenotazione ConvertFromDto(PrenotazioneDto prenotazioneDto) {
        Prenotazione prenotazione = null;
        if(prenotazioneDto != null) {
            prenotazione = modelMapper.map(prenotazioneDto, Prenotazione.class);
        }
        return prenotazione;
    }
}
