package com.xantrix.webapp.services;

import com.xantrix.webapp.entities.Utente;
import com.xantrix.webapp.repository.UtentiRepository;

import java.util.List;

public class UtentiServiceImpl implements UtentiService {

    private UtentiRepository utentiRepository;

    private UtentiServiceImpl(UtentiRepository utentiRepository) {
        this.utentiRepository = utentiRepository;
    }

    @Override
    public List<Utente> SelAll() {
        return utentiRepository.findAll();
    }
}
