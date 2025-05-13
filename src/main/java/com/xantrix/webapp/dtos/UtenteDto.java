package com.xantrix.webapp.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UtenteDto {

    private String nome;
    private String cognome;
    private Date dataNascita;
    private String ruolo;
}
