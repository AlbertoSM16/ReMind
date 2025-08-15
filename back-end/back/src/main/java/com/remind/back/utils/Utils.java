package com.remind.back.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Random;

@Component
public class Utils {

    /**
     * Quita las tildes y caracteres diacríticos de un texto.
     * Ejemplo: "María José" -> "Maria Jose"
     * 
     * @param texto El texto a normalizar.
     * @return El texto sin tildes.
     */
    private String quitarTildes(String texto) {
        if (texto == null) {
            return null;
        }
        // Normaliza el texto para separar los caracteres base de las tildes
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Reemplaza todas las marcas diacríticas (tildes, etc.) con una cadena vacía
        return textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Crea un nombre de usuario con el formato Nombre.Apellido y un número
     * aleatorio,
     * todo en minúsculas y sin tildes.
     * Ejemplo: "Alberto", "Gómez" -> "alberto.gomez123"
     * 
     * @param nombre   El nombre de la persona.
     * @param apellido El apellido de la persona.
     * @return Un nombre de usuario con formato "nombre.apellido" + número
     *         aleatorio.
     */
    public String generateRandomUsername(String nombre, String apellido) {
        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            nombre = "usuario";
            apellido = "generico";
        }

        String nombreLimpio = quitarTildes(nombre.trim());
        String apellidoLimpio = quitarTildes(apellido.trim()).replaceAll("\\s+", ".");

        Random random = new Random();
        int numeroAleatorio = 100 + random.nextInt(900);

        String nombreDeUsuario = nombreLimpio + "." + apellidoLimpio + numeroAleatorio;
        return nombreDeUsuario.toLowerCase();
    }

    /**
     * Genera una contraseña aleatoria a partir del nombre y el apellido.
     * La contraseña combina los 2 primeros caracteres del nombre (mayúsculas),
     * los 2 últimos del apellido (minúsculas), un número de 2 dígitos y un carácter
     * especial.
     * Ejemplo: "María", "González" -> "MAez5821@"
     * 
     * @param nombre   El nombre de la persona.
     * @param apellido El apellido de la persona.
     * @return Una contraseña aleatoria.
     */
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
