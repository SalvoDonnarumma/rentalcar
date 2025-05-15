package com.xantrix.webapp.repository;

import com.xantrix.webapp.entities.Veicolo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface VeicoliRepository extends JpaRepository<Veicolo, Integer> {

    Page<Veicolo> findByTarga(String filtro, Pageable pageAndRecords);

    Page<Veicolo> findByModello(String filtro, Pageable pageAndRecords);

    Page<Veicolo> findByTipologia(String filtro, Pageable pageAndRecords);

    Page<Veicolo> findByCasaProduttrice(String filtro, Pageable pageAndRecords);
}
