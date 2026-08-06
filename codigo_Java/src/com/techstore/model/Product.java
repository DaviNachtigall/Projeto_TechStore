package com.techstore.model;

/**
 * Representa um produto do catálogo.
 * O preço "oficial" mora aqui, no servidor — é este valor que será
 * usado no checkout, nunca um preço enviado pelo cliente
 * (isso corrige a ameaça T02 / CA01 da documentação: adulteração de preço).
 */
public class Product {

    public int id;
    public String nome;
    public double preco;
    public int estoque;

    public Product(int id, String nome, double preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }
}
