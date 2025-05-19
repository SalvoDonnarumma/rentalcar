package com.xantrix.webapp.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
public class PrenotazioneDto {

    private Integer idPrenotazione;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInizio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataFine;

    private UtenteDto utente;
    private VeicoloDto veicolo;
}
