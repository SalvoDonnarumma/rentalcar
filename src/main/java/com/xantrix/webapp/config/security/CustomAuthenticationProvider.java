package com.xantrix.webapp.config.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        String presentedPassword = authentication.getCredentials().toString();
        System.out.println("Password inserita: " + presentedPassword);
        System.out.println("Password hash DB: " + userDetails.getPassword());
        System.out.println("Password inserita quando viene hashata: " + getPasswordEncoder().encode(presentedPassword));
        System.out.println("Match? " + getPasswordEncoder().matches(presentedPassword, userDetails.getPassword()));


        if (!getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Password non valida");
        }
    }
}
