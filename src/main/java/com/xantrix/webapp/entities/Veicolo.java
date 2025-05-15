package com.xantrix.webapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veicoli")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Veicolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idveicolo")
    private Integer idVeicoli;

    @Column(name = "targa")
    private String targa;

    @Column(name = "anno_immatricolazione")
    private String annoImmatricolazione;

    @Column(name = "modello")
    private String modello;

    @Column(name = "casa_produttrice")
    private String casaProduttrice;

    @Column(name = "tipologia")
    private String tipologia;
}
