package com.xantrix.webapp.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class PrenotazioneDto {

    private Integer idPrenotazione;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInizio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataFine;

    private String stato;

    private Integer idUtente;
    private Integer idVeicolo;

    private UtenteDto utente;
    private VeicoloDto veicolo;

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + idPrenotazione +
                ", utenteId=" + (utente != null ? utente.getId() : "null") +
                ", veicoloId=" + (veicolo != null ? veicolo.getId() : "null") +
                ", stato=" + stato +
                '}';
    }
}
