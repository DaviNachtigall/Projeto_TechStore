package com.techstore.service;

import com.techstore.model.User;
import com.techstore.repository.UserRepository;
import com.techstore.util.PasswordUtil;

/**
 * Regras de cadastro e login.
 * Mitiga parte da ameaça T01 (Spoofing / força bruta) ao não revelar
 * se o erro foi "email não existe" ou "senha errada" — a mensagem
 * de erro é sempre genérica.
 */
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Cadastra um novo cliente. Lança exceção se o e-mail já existir. */
    public User cadastrar(String nome, String email, String cpf, String senhaPura) {
        if (userRepository.emailJaCadastrado(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        String senhaHash = PasswordUtil.hash(senhaPura);
        return userRepository.salvar(nome, email, cpf, senhaHash, "CLIENTE");
    }

    /** Retorna o usuário autenticado, ou null se e-mail/senha não conferirem. */
    public User autenticar(String email, String senhaPura) {
        User user = userRepository.buscarPorEmail(email);
        if (user == null) {
            return null; // não dizemos "email não existe" para não facilitar enumeração de contas
        }
        if (!PasswordUtil.confere(senhaPura, user.senhaHash)) {
            return null; // não dizemos "senha errada" pelo mesmo motivo
        }
        return user;
    }
}
