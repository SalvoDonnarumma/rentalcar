package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.repository.UtentiRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        if ( filtro == null || filtro.isEmpty() ) {
            resultPage = utentiRepository.findAll(pageAndRecords);
        } else {
            if( campoFiltro.equalsIgnoreCase("nome"))
                resultPage = utentiRepository.findByNome(filtro, pageAndRecords);
            else if( campoFiltro.equalsIgnoreCase("cognome"))
                resultPage = utentiRepository.findByCognome(filtro, pageAndRecords);
                else if( campoFiltro.equalsIgnoreCase("dataNascita")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date data = formatter.parse(filtro);
                    System.out.println("Data convertita: " + data);
                    resultPage = utentiRepository.findByDataNascita(data, pageAndRecords);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        List<Utente> utenti = resultPage.getContent();

        System.out.println("--------- RISULTATI PAGINA ---------");
        System.out.println("Numero pagina (zero-based): " + pageNum);
        System.out.println("Numero risultati: " + utenti.size());
        utenti.forEach(u -> System.out.println("Utente: " + u.getCognome() + " - ID: " + u.getIdutente()));
        System.out.println("------------------------------------");

        return ConvertToDto(utenti);
    }

    @Override
    public int NumRecords() {
        return (int) utentiRepository.count();
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


}
