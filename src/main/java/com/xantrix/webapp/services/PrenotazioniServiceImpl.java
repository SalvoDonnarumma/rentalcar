package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.entities.Prenotazione;
import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.repository.PrenotazioniRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PrenotazioneDto SelById(Integer id) {
        PrenotazioneDto prenotazioneDto = null;
        Pageable pageAndRecords = PageRequest.of(0, 1);
        if(id != null) {
            prenotazioneDto = ConvertToDto(prenotazioniRepository.findByIdPrenotazione(id, pageAndRecords).getContent().get(0));
        }
        return prenotazioneDto;
    }

    @Override
    public List<PrenotazioneDto> SelByIdUtente(Integer idUtente, int pageNum, int recForPage) {
        Pageable pageAndRecords = PageRequest.of(pageNum, recForPage);
        return ConvertToDto(prenotazioniRepository.findByUtente(idUtente, pageAndRecords).getContent());
    }

    @Override
    public List<PrenotazioneDto> SelByIdVeicolo(Integer idVeicolo, int pageNum, int recForPage) {
        Pageable pageAndRecords = PageRequest.of(pageNum, recForPage);
        return ConvertToDto(prenotazioniRepository.findByVeicolo(idVeicolo, pageAndRecords).getContent());
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
