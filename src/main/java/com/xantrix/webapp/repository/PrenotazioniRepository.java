package com.xantrix.webapp.repository;

import com.xantrix.webapp.entities.Prenotazione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrenotazioniRepository extends JpaRepository<Prenotazione, Integer> {

    Page<Prenotazione> findByUtente(Integer idUtente, Pageable pageable);
    Page<Prenotazione> findByVeicolo(Integer idVeicolo, Pageable pageable);
    Page<Prenotazione> findByIdPrenotazione(Integer idPrenotazione, Pageable pageable);
}
