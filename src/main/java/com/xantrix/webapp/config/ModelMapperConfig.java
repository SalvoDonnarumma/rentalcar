package com.xantrix.webapp.config;

import com.xantrix.webapp.dtos.PrenotazioneDto;
import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.entities.Prenotazione;
import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.entities.Veicolo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {
    private final ModelMapper modelMapper = new ModelMapper();
    private final Converter<Set<Prenotazione>, Set<PrenotazioneDto>> prenotazioniToDtoConverter =
            ctx -> {
                Set<Prenotazione> source = ctx.getSource();
                if (source == null) {
                    return Collections.emptySet();
                }
                return source.stream()
                        .map(p -> modelMapper.map(p, PrenotazioneDto.class))
                        .collect(Collectors.toCollection(HashSet::new)); // forza HashSet
            };

    private final Converter<Set<PrenotazioneDto>, Set<Prenotazione>> prenotazioniDtoToEntityConverter =
            ctx -> {
                Set<PrenotazioneDto> source = ctx.getSource();
                if (source == null) {
                    return Collections.emptySet();
                }
                return source.stream()
                        .map(dto -> modelMapper.map(dto, Prenotazione.class))
                        .collect(Collectors.toCollection(HashSet::new)); // forza HashSet
            };



    @Bean
    public ModelMapper modelMapper() {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.addMappings(utenteMapping);
        modelMapper.addMappings(utenteDtoMapping);
        modelMapper.addMappings(veicoloMapping);
        modelMapper.addMappings(veicoloDtoMapping);
        modelMapper.addMappings(prenotazioneMapping);
        modelMapper.addMappings(prenotazioneDtoMapping);

        return modelMapper;
    }

    PropertyMap<Utente, UtenteDto> utenteMapping = new PropertyMap<Utente, UtenteDto>() {
        protected void configure() {
            map().setId(source.getIdUtente());
            map().setNome(source.getNome());
            map().setCognome(source.getCognome());
            map().setDataNascita(source.getDataNascita());
            map().setRuolo(source.getRuolo());
            map().setEmail(source.getEmail());
            map().setPassword(source.getPassword());
        //    using(prenotazioniToDtoConverter).map(source.getPrenotazioni()).setPrenotazioni(null);
        }
    };

    PropertyMap<UtenteDto, Utente> utenteDtoMapping = new PropertyMap<UtenteDto, Utente>() {
        protected void configure() {
            map().setIdUtente(source.getId());
            map().setNome(source.getNome());
            map().setCognome(source.getCognome());
            map().setDataNascita(source.getDataNascita());
            map().setRuolo(source.getRuolo());
            map().setEmail(source.getEmail());
            map().setPassword(source.getPassword());
         //   using(prenotazioniDtoToEntityConverter).map(source.getPrenotazioni()).setPrenotazioni(null);
        }
    };

    PropertyMap<Veicolo, VeicoloDto> veicoloMapping = new PropertyMap<Veicolo, VeicoloDto>() {
        protected void configure() {
            map().setId(source.getIdVeicolo());
            map().setTarga(source.getTarga());
            map().setModello(source.getModello());
            map().setTipologia(source.getTipologia());
            map().setAnnoImmatricolazione(source.getAnnoImmatricolazione());
            map().setCasaProduttrice(source.getCasaProduttrice());
        }
    };

    PropertyMap<VeicoloDto, Veicolo> veicoloDtoMapping = new PropertyMap<VeicoloDto, Veicolo>() {
        protected void configure() {
            map().setIdVeicolo(source.getId());
            map().setTarga(source.getTarga());
            map().setModello(source.getModello());
            map().setTipologia(source.getTipologia());
            map().setAnnoImmatricolazione(source.getAnnoImmatricolazione());
            map().setCasaProduttrice(source.getCasaProduttrice());
        }
    };

    PropertyMap<Prenotazione, PrenotazioneDto> prenotazioneMapping = new PropertyMap<Prenotazione, PrenotazioneDto>() {
        protected void configure() {
            map().setDataFine(source.getDataFine());
            map().setDataInizio(source.getDataInizio());
            map().setIdUtente(source.getUtente().getIdUtente());
            map().setIdVeicolo(source.getVeicolo().getIdVeicolo());
        }
    };

    PropertyMap<PrenotazioneDto, Prenotazione> prenotazioneDtoMapping = new PropertyMap<PrenotazioneDto, Prenotazione>() {
        protected void configure() {
            map().setDataFine(source.getDataFine());
            map().setDataInizio(source.getDataInizio());
            map().getUtente().setIdUtente(source.getIdUtente());
            map().getVeicolo().setIdVeicolo(source.getIdVeicolo());
        }
    };

}
