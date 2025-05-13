package com.xantrix.webapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "veicoli")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Veicolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idveicolo")
    private Integer idVeicoli;

    @Column(name = "targa")
    private String targa;

    @Column(name = "anno_immatricolazione")
    private String anno_immatricolazione;

    @Column(name = "modello")
    private String modello;

    @Column(name = "casa_produttrice")
    private String casa_produttrice;

    @Column(name = "tipologia")
    private String tipologia;
}
