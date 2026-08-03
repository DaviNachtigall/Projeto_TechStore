package com.techstore.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitário bem simples para nunca guardar senha em texto puro.
 * Usa SHA-256, que já vem pronto no Java (não precisa de bibliotecas
 * externas). Isso ajuda a mitigar a ameaça de Spoofing (T01):
 * mesmo que o banco vaze, as senhas não ficam expostas em texto puro.
 */
public class PasswordUtil {

    public static String hash(String senhaPura) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(senhaPura.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexBuilder = new StringBuilder();
            for (byte b : bytes) {
                hexBuilder.append(String.format("%02x", b));
            }
            return hexBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 sempre existe no Java, então isso nunca deve acontecer
            throw new RuntimeException("Algoritmo de hash não encontrado", e);
        }
    }

    public static boolean confere(String senhaPura, String hashSalvo) {
        return hash(senhaPura).equals(hashSalvo);
    }
}
