package com.remind.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager; // Importar AuthenticationManager
import org.springframework.security.authentication.ProviderManager; // Importar ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Importar DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService; // Importar UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importar BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Importar PasswordEncoder

// Estos imports son placeholders si decides usar JWT más adelante
// import com.remind.back.security.JwtUtil;
// import com.remind.back.security.JwtRequestFilter; // Necesitarías crear este filtro

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Si aún no has implementado JwtUtil o CustomUserDetailsService, puedes omitir la inyección por ahora
    // y añadirlas cuando empieces a trabajar con JWT.
    // private final JwtUtil jwtUtil;
    // private final UserDetailsService userDetailsService;

    // public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    //     this.jwtUtil = jwtUtil;
    //     this.userDetailsService = userDetailsService;
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para APIs REST
            .cors(cors -> {}) // Habilita CORS (necesitas una configuración de CORS más completa si es compleja)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No usar sesiones HTTP con JWT
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/paciente/**").permitAll() //
                .requestMatchers("/api/auth/**").permitAll() //
                .requestMatchers("/api/terapeuta/**").permitAll() 
                .requestMatchers("/api/administrador/**").permitAll() // Puedes quitar esto más tarde
                // Cualquier otra solicitud, por ahora, también la permitimos para facilitar pruebas,
                // PERO ESTO DEBERÍA CAMBIARSE A .authenticated() PARA SEGURIDAD REAL
                .anyRequest().permitAll() // CAMBIAR A .authenticated() CUANDO SE IMPLEMENTE LA SEGURIDAD COMPLETA
            );

        // Si tu JwtUtil.java y AuthController.java están comentados, no necesitas estos filtros JWT todavía.
        // Los añadirás cuando implementes la seguridad completa.
        // http.addFilterBefore(new JwtRequestFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Estos Beans son necesarios si vas a usar el AuthenticationManager en tu AuthController
    // pero pueden no ser estrictamente necesarios si solo quieres un .permitAll() simple.
    // Sin embargo, es buena práctica tenerlos para cuando avances con la autenticación real.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Para que funcione el Autowired de AuthenticationManager en AuthController
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }
    // *** Necesitarás implementar tu propio UserDetailsService ***
    // (Como te expliqué en la respuesta anterior, para cargar los detalles del usuario de tus entidades)
}