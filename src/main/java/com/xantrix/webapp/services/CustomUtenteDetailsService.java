package com.xantrix.webapp.services;

import com.xantrix.webapp.dtos.UtenteDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customUtenteDetailsService")
public class CustomUtenteDetailsService implements UserDetailsService {

    @Autowired
    private UtentiService utentiService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UtenteDto utente = utentiService.SelByEmail(email);

        if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato");
        }

        UserDetails userDetails = new User(
                utente.getEmail(),
                utente.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo().toUpperCase()))
        );

        return userDetails;
    }

}
