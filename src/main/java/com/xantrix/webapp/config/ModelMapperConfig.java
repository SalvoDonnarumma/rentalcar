package com.xantrix.webapp.config;

import com.xantrix.webapp.dtos.UtenteDto;
import com.xantrix.webapp.dtos.VeicoloDto;
import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.entities.Veicolo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.addMappings(utenteMapping);
        modelMapper.addMappings(utenteDtoMapping);
        modelMapper.addMappings(veicoloMapping);
        modelMapper.addMappings(veicoloDtoMapping);

        return modelMapper;
    }

    PropertyMap<Utente, UtenteDto> utenteMapping = new PropertyMap<Utente, UtenteDto>() {
        protected void configure() {
            map().setId(source.getIdutente());
            map().setNome(source.getNome());
            map().setCognome(source.getCognome());
            map().setDataNascita(source.getDataNascita());
            map().setRuolo(source.getRuolo());
            map().setEmail(source.getEmail());
            map().setPassword(source.getPassword());
        }
    };

    PropertyMap<UtenteDto, Utente> utenteDtoMapping = new PropertyMap<UtenteDto, Utente>() {
        protected void configure() {
            map().setIdutente(source.getId());
            map().setNome(source.getNome());
            map().setCognome(source.getCognome());
            map().setDataNascita(source.getDataNascita());
            map().setRuolo(source.getRuolo());
            map().setEmail(source.getEmail());
            map().setPassword(source.getPassword());
        }
    };

    PropertyMap<Veicolo, VeicoloDto> veicoloMapping = new PropertyMap<Veicolo, VeicoloDto>() {
        protected void configure() {
            map().setId(source.getIdVeicoli());
            map().setTarga(source.getTarga());
            map().setModello(source.getModello());
            map().setTipologia(source.getTipologia());
            map().setAnnoImmatricolazione(source.getAnnoImmatricolazione());
            map().setCasaProduttrice(source.getCasaProduttrice());
        }
    };

    PropertyMap<VeicoloDto, Veicolo> veicoloDtoMapping = new PropertyMap<VeicoloDto, Veicolo>() {
        protected void configure() {
            map().setIdVeicoli(source.getId());
            map().setTarga(source.getTarga());
            map().setModello(source.getModello());
            map().setTipologia(source.getTipologia());
            map().setAnnoImmatricolazione(source.getAnnoImmatricolazione());
            map().setCasaProduttrice(source.getCasaProduttrice());
        }
    };



}
