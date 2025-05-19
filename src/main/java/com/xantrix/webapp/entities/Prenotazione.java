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
@ToString
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
    private Utente idUtente;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idveicolo_fk",  referencedColumnName = "idVeicolo")
    private Veicolo idVeicolo;
}
