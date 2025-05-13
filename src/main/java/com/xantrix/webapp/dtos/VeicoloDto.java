package com.xantrix.webapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeicoloDto {

    private String targa;

    private String anno_immatricolazione;

    private String modello;

    private String casa_produttrice;

    private String tipologia;
}
