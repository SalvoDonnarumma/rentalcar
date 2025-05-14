package com.xantrix.webapp.repository;

import com.xantrix.webapp.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface UtentiRepository extends JpaRepository<Utente, Integer> {
    Page<Utente> findByNome(String nome, Pageable pageable);
    Page<Utente> findByCognome(String nome, Pageable pageable);
    Page<Utente> findByDataNascita(Date dataNascita, Pageable pageable);
}
