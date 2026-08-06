package com.techstore.repository;

import com.techstore.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * "Banco de dados" de usuários guardado em memória (HashMap).
 * Trocar isso por um banco de dados real (MySQL, Postgres, etc.)
 * no futuro é simples: basta reimplementar estes mesmos métodos
 * usando JDBC, mantendo o resto do sistema igual.
 */
public class UserRepository {

    private final Map<Integer, User> usuarios = new HashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    public User salvar(String nome, String email, String cpf, String senhaHash, String role) {
        int id = proximoId.getAndIncrement();
        User user = new User(id, nome, email, cpf, senhaHash, role);
        usuarios.put(id, user);
        return user;
    }

    public User buscarPorId(int id) {
        return usuarios.get(id);
    }

    public User buscarPorEmail(String email) {
        for (User u : usuarios.values()) {
            if (u.email.equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public boolean emailJaCadastrado(String email) {
        return buscarPorEmail(email) != null;
    }
}
