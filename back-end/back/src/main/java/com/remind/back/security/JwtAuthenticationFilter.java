package com.remind.back.security;

import com.remind.back.repositories.AdministradorRepository;
import com.remind.back.repositories.PacienteRepository;
import com.remind.back.repositories.TerapeutaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AdministradorRepository administradorRepository;
    private final TerapeutaRepository terapeutaRepository;
    private final PacienteRepository pacienteRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   AdministradorRepository administradorRepository,
                                   TerapeutaRepository terapeutaRepository,
                                   PacienteRepository pacienteRepository) {
        this.jwtUtil = jwtUtil;
        this.administradorRepository = administradorRepository;
        this.terapeutaRepository = terapeutaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);

                boolean exists = switch (role) {
                    case "administrador" -> administradorRepository.findByUsuario(username).isPresent();
                    case "terapeuta" -> terapeutaRepository.findByUsuario(username).isPresent();
                    case "paciente" -> pacienteRepository.findByUsuario(username).isPresent();
                    default -> false;
                };

                if (exists) {
                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
