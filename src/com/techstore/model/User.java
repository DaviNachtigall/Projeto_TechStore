package com.techstore.model;

/**
 * Representa um usuário do sistema (Cliente ou Administrador).
 * Os campos são públicos de propósito, para manter o código o mais
 * simples possível de ler (sem getters/setters repetitivos).
 */
public class User {

    public int id;
    public String nome;
    public String email;
    public String cpf;
    public String senhaHash; // nunca guardamos a senha em texto puro
    public String role;      // "CLIENTE" ou "ADMIN"

    public User(int id, String nome, String email, String cpf, String senhaHash, String role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.role = role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
