package com.techstore.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Versão simplificada de um "token de sessão" (o JWT citado na
 * documentação). Em vez de gerar/validar um JWT de verdade (o que
 * exigiria uma biblioteca externa), guardamos aqui, em memória, a
 * relação token -> usuário. O efeito para o resto do sistema é o
 * mesmo: cada requisição autenticada carrega um token, e o servidor
 * descobre quem é o usuário a partir dele.
 */
public class TokenService {

    private final Map<String, Integer> tokenParaUserId = new HashMap<>();

    public String gerarToken(int userId) {
        String token = UUID.randomUUID().toString();
        tokenParaUserId.put(token, userId);
        return token;
    }

    /** Retorna o ID do usuário dono do token, ou null se o token for inválido. */
    public Integer usuarioDoToken(String token) {
        if (token == null) return null;
        return tokenParaUserId.get(token);
    }

    public void invalidarToken(String token) {
        tokenParaUserId.remove(token);
    }
}
