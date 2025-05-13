// package com.remind.back.services;

// import com.remind.back.entities.LoginRequest;
// import com.remind.back.entities.Usuario;
// import com.remind.back.repositories.UsuarioRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


// @Service
// public class UserDetailService {
// @PostMapping("/login")
// public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//     try {
//         Authentication authentication = authManager.authenticate(
//             new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasena())
//         );

//         Usuario user = (Usuario) authentication.getPrincipal();
//         String token = jwtTokenProvider.createToken(user.getUsername(), user.getRol());

//         return ResponseEntity.ok(Map.of(
//             "token", token,
//             "rol", user.getRol()
//         ));
//     } catch (AuthenticationException e) {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
//     }
// }
// }
