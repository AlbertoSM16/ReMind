package com.remind.back.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Random;

@Component
public class Utils {


    private String quitarTildes(String texto) {
        if (texto == null) {
            return null;
        }
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    
    public String generateRandomUsername(String nombre, String apellido) {
        String baseUsername = nombre.trim().toLowerCase().replaceAll("\\s+", "") 
                            + "." 
                            + apellido.trim().toLowerCase().replaceAll("\\s+", "");
        
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); 
        
        return baseUsername + randomNumber;
    }

    
    public String generateRandomPassword(String nombre, String apellido) {
        if (nombre == null || nombre.trim().length() < 2 || apellido == null || apellido.trim().length() < 2) {
            nombre = "Usuario";
            apellido = "Generico";
        }
        String nombreLimpio = quitarTildes(nombre.trim());
        String apellidoLimpio = quitarTildes(apellido.trim());

        String parteNombre = nombreLimpio.substring(0, 2).toUpperCase();
        String parteApellido = apellidoLimpio.substring(apellidoLimpio.length() - 2).toLowerCase();

        SecureRandom random = new SecureRandom();
        int numeroAleatorio = 1 + random.nextInt(100);

        String caracteresEspeciales = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        char caracterEspecial = caracteresEspeciales.charAt(random.nextInt(caracteresEspeciales.length()));

        return parteNombre + parteApellido + numeroAleatorio + caracterEspecial;
    }
}
