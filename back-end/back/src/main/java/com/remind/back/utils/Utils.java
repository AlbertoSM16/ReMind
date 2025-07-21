package com.remind.back.utils;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Random;

@Component
public class Utils {

 /**
     * Quita las tildes y caracteres diacríticos de un texto.
     * Ejemplo: "María José" -> "Maria Jose"
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
     * Crea un nombre de usuario con el formato Nombre.Apellido y un número aleatorio,
     * todo en minúsculas y sin tildes.
     * Ejemplo: "Alberto", "Gómez" -> "alberto.gomez123"
     * @param nombre El nombre de la persona.
     * @param apellido El apellido de la persona.
     * @return Un nombre de usuario con formato "nombre.apellido" + número aleatorio.
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
}
