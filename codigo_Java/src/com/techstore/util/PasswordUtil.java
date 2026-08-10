package com.techstore.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilitário para nunca guardar senha em texto puro.
 *
 * Usa PBKDF2 com salt aleatório por usuário (em vez de SHA-256 puro),
 * que é o que a OWASP recomenda no "Password Storage Cheat Sheet".
 * Isso corrige duas falhas do hash antigo:
 *   1) Duas senhas iguais agora geram hashes diferentes (por causa do salt).
 *   2) O algoritmo é propositalmente lento (100.000 iterações), dificultando
 *      ataques de força bruta em massa mesmo se o banco vazar.
 */
public class PasswordUtil {

    private static final int ITERACOES = 100_000;
    private static final int TAMANHO_HASH_BITS = 256;
    private static final int TAMANHO_SALT_BYTES = 16;

    /** Gera o hash da senha, já incluindo um salt aleatório novo. */
    public static String hash(String senhaPura) {
        byte[] salt = gerarSaltAleatorio();
        byte[] hashBytes = pbkdf2(senhaPura, salt);

        // Guardamos salt e hash juntos, separados por ":", em Base64 para virar texto
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);
        return saltBase64 + ":" + hashBase64;
    }

    /** Confere se a senha digitada bate com o hash salvo no banco. */
    public static boolean confere(String senhaPura, String hashSalvo) {
        String[] partes = hashSalvo.split(":");
        if (partes.length != 2) {
            return false; // formato inesperado (ex: hash antigo, sem salt)
        }

        byte[] salt = Base64.getDecoder().decode(partes[0]);
        byte[] hashOriginal = Base64.getDecoder().decode(partes[1]);

        byte[] hashCalculado = pbkdf2(senhaPura, salt);

        return constantTimeEquals(hashCalculado, hashOriginal);
    }

    private static byte[] gerarSaltAleatorio() {
        byte[] salt = new byte[TAMANHO_SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] pbkdf2(String senhaPura, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    senhaPura.toCharArray(), salt, ITERACOES, TAMANHO_HASH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    /** Compara dois hashes em tempo constante, evitando ataques de timing. */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int resultado = 0;
        for (int i = 0; i < a.length; i++) {
            resultado |= a[i] ^ b[i];
        }
        return resultado == 0;
    }
}
