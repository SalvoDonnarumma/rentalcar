package com.xantrix.webapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idutente")
    private Integer idutente;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascita")
    //@NotNull(message = "{NotNull.Articoli.dataCreaz.Validation}")
    private Date dataNascita;

    @Column(name = "ruolo")
    private String ruolo;
}
