package com.xantrix.webapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idprenotazioni")
    private Integer idPrenotazione;

    @Column(name = "data_inizio")
    private Date dataInizio;

    @Column(name = "data_fine")
    private Date dataFine;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idutente_fk",  referencedColumnName = "idutente")
    private Utente utente;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idveicolo_fk",  referencedColumnName = "idveicolo")
    private Veicolo veicolo;

    // Prenotazione.java
    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + idPrenotazione +
                ", utenteId=" + (utente != null ? utente.getIdutente() : "null") +
                ", veicoloId=" + (veicolo != null ? veicolo.getIdVeicolo() : "null") +
                '}';
    }

}
