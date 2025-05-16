package com.xantrix.webapp.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
public class UtenteDto {

    private Integer id;
    private String nome;
    private String cognome;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNascita;
    private String ruolo;
    private String email;
    private String password;
}
