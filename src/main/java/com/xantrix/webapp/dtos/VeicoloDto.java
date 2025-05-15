package com.xantrix.webapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeicoloDto {

    private Integer id;

    private String targa;

    private String annoImmatricolazione;

    private String modello;

    private String casaProduttrice;

    private String tipologia;
}
