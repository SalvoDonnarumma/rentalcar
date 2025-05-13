package com.xantrix.webapp.repository;

import com.xantrix.webapp.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtentiRepository extends JpaRepository<Utente, Integer> {
}
