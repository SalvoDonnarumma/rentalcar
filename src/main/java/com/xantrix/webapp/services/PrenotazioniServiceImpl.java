package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.entities.Prenotazione;
import com.xantrix.webapp.entities.Veicolo;
import com.xantrix.webapp.repository.PrenotazioniRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public List<PrenotazioneDto> SelByIdUtente(Integer idUtente, int pageNum, int recForPage, String dataInit, String dataFin) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date inizio = null;
        Date fine = null;

        Pageable pageAndRecords = PageRequest.of(pageNum, recForPage);
        Page<Prenotazione> prenotazioni = null;

        try {
                if(!dataInit.isBlank() && !dataFin.isBlank()) {
                    inizio = new java.sql.Date(formatter.parse(dataInit).getTime());
                    fine = new java.sql.Date(formatter.parse(dataFin).getTime());
                    prenotazioni = prenotazioniRepository.findByUtenteIdUtenteAndDataInizioBetween(idUtente, inizio, fine, pageAndRecords);
                } else if(!dataFin.isBlank()) {
                    fine = new java.sql.Date(formatter.parse(dataFin).getTime());
                    prenotazioni = prenotazioniRepository.findByUtenteIdUtenteAndDataInizioLessThanEqual(idUtente, fine, pageAndRecords);
                } else if(!dataInit.isBlank()) {
                    inizio = new java.sql.Date(formatter.parse(dataInit).getTime());
                    prenotazioni = prenotazioniRepository.findByUtenteIdUtenteAndDataInizioGreaterThanEqual(idUtente, inizio, pageAndRecords);
                } else {
                    prenotazioni = prenotazioniRepository.findByUtenteIdUtente(idUtente, pageAndRecords);
                }
        } catch (ParseException e) {
                throw new RuntimeException(e);
        }

        return ConvertToDto(prenotazioni.getContent());
    }

    @Override
    public List<PrenotazioneDto> SelByIdVeicolo(Integer idVeicolo, int pageNum, int recForPage) {
        Pageable pageAndRecords = PageRequest.of(pageNum, recForPage);
        return ConvertToDto(prenotazioniRepository.findByVeicoloIdVeicolo(idVeicolo, pageAndRecords).getContent());
    }

    @Override
    public List<PrenotazioneDto> SelByVeicolo(Veicolo veicolo, int pageNum, int recForPage) {
        return List.of();
    }

    @Override
    public void InsertPrenotazione(PrenotazioneDto prenotazione) {
        prenotazioniRepository.save(ConvertFromDto(prenotazione));
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
