package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.repository.UtentiRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtentiServiceImpl implements UtentiService {

    @Autowired
    private UtentiRepository utentiRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UtenteDto> SelAll() {
        List<Utente> utenti = utentiRepository.findAll();
        System.out.println("Totale utenti: " + utenti.size());

        return ConvertToDto(utenti);
    }

    @Override
    public List<UtenteDto> SearchCostumers(String filtro, String campoFiltro, int pageNum, int recForPage) {
        Pageable pageAndRecords = PageRequest.of(pageNum, recForPage);
        Page<Utente> resultPage = null;

        if(filtro == null || filtro.isEmpty()) {
            resultPage = utentiRepository.findAll(pageAndRecords);
        } else if(campoFiltro.equalsIgnoreCase("id")){
            Integer id = Integer.parseInt(filtro);
            Optional<Utente> utente = utentiRepository.findById(id);
            /*
            if(utente.isPresent())
                resultPage = new PageImpl<>(List.of(utente.get()), pageAndRecords, 1);
            else
                resultPage = new PageImpl<>(List.of(), pageAndRecords, 0); */

            resultPage = utente.<Page<Utente>>map(value -> new PageImpl<>(List.of(value), pageAndRecords, 1)).orElseGet(() -> new PageImpl<>(List.of(), pageAndRecords, 0));
        } else if(campoFiltro.equalsIgnoreCase("nome")) {
                resultPage = utentiRepository.findByNome(filtro, pageAndRecords);
            } else if(campoFiltro.equalsIgnoreCase("cognome")) {
                resultPage = utentiRepository.findByCognome(filtro, pageAndRecords);
                } else
                    resultPage = utentiRepository.findByEmailContainingIgnoreCase(filtro, pageAndRecords);

        List<Utente> utenti = resultPage.getContent();
        return ConvertToDto(utenti);
    }

    @Override
    public int NumRecords() {
        return (int) utentiRepository.count();
    }

    @Override
    public void InsertCostumer(UtenteDto utente) {
        utentiRepository.save(ConvertFromDto(utente));
    }

    @Override
    public void DeleteCostumer(Integer id) {
        Optional<Utente> utente = utentiRepository.findById(id);
        utentiRepository.delete(utente.get());
    }

    @Override
    public UtenteDto SelById(Integer id) {
        Utente utente = utentiRepository.findById(id).get();
        return ConvertToDto(utente);
    }

    @Override
    public UtenteDto SelByEmail(String email) {
        Pageable pageAndRecords = PageRequest.of(0, 1);
        Utente utente = utentiRepository.findByEmailContainingIgnoreCase(email, pageAndRecords).getContent().get(0);
        return ConvertToDto(utente);
    }

    @Override
    public boolean EmailExists(String email, Integer idUtente) {
        return utentiRepository.findByEmailAndIdUtenteNot(email, idUtente).isPresent();
    }

    @Override
    public boolean EmailExists(String email) {
        return utentiRepository.findByEmail(email).isPresent();
    }

    private List<UtenteDto> ConvertToDto(List<Utente> utenti) {
        List<UtenteDto> utentiDtoList = utenti
                .stream()
                .map(source -> modelMapper.map(source, UtenteDto.class))
                .collect(Collectors.toList());

        return utentiDtoList;
    }

    private UtenteDto ConvertToDto(Utente utente) {
        UtenteDto utenteDto = null;
        if(utente != null) {
            utenteDto = modelMapper.map(utente, UtenteDto.class);
        }
        return utenteDto;
    }

    private Utente ConvertFromDto(UtenteDto utenteDto) {
        Utente utente = null;
        if(utenteDto != null) {
            utente = modelMapper.map(utenteDto, Utente.class);
        }
        return utente;
    }


}
