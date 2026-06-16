package com.remind.back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/agenda/paciente/**").hasAnyRole("PACIENTE", "TERAPEUTA")
                        .requestMatchers(HttpMethod.PUT, "/api/agenda/*/juego/*/completar").hasAnyRole("PACIENTE", "TERAPEUTA")
                        .requestMatchers("/api/administrador/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/terapeuta").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/terapeuta/*").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/terapeuta/*").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/terapeuta").hasAnyRole("ADMINISTRADOR", "TERAPEUTA")
                        .requestMatchers(HttpMethod.GET, "/api/terapeuta/*").hasAnyRole("ADMINISTRADOR", "TERAPEUTA")
                        .requestMatchers(HttpMethod.POST, "/api/terapeuta/*/reset-password").hasAnyRole("ADMINISTRADOR", "TERAPEUTA")
                        .requestMatchers("/api/terapeuta/**").hasRole("TERAPEUTA")
                        .requestMatchers("/api/paciente/**").hasRole("TERAPEUTA")
                        .requestMatchers("/api/juegos/**").hasRole("TERAPEUTA")
                        .requestMatchers("/api/agenda/**").hasRole("TERAPEUTA")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@org.springframework.lang.NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}