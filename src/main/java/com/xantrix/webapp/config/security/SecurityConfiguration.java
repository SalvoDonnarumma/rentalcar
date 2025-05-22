package com.xantrix.webapp.config.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    @Qualifier("customUtenteDetailsService")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);

        return firewall;
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(getAuthenticationProvider())
                .authorizeHttpRequests( (authorize) -> authorize
                        .requestMatchers("/homepage/elimina/**", "prenotazioni/visualizzaprenot/**","homepage/search/**",
                                        "/parcoauto/elimina/**", "/parcoauto/modifica/**", "prenotazioni/valida",
                                      "/prenotazioni/approva")
                                  .hasRole("ADMIN")
                              .requestMatchers("/homepage/**", "homepage/profiloutente", "homepage/aggiungi/**",
                                      "/parcoauto", "/parcoauto/search/**", "/homepage/modifica/**",
                                      "/prenotazioni/aggiungi/**", "prenotazioni/modifica/**", "prenotazioni/cancella/**")
                                    .authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login/form")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler(authenticationFailureHandler())
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/login/form?forbidden")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/form?logout")
                );
        http
                .addFilterBefore(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                            throws ServletException, IOException {
                        System.out.println("REQUEST: " + request.getRequestURI());
                        filterChain.doFilter(request, response);
                    }
                }, UsernamePasswordAuthenticationFilter.class);

        // http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            System.out.println(">>> Autenticazione fallita: " + exception.getMessage());
            new SimpleUrlAuthenticationFailureHandler("/login/form?error").onAuthenticationFailure(request, response, exception);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector(ApplicationContext context) {
        return new HandlerMappingIntrospector();
    }
}
