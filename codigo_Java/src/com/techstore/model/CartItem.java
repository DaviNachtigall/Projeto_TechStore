package com.techstore.model;

/**
 * Um item dentro do carrinho de compras de um usuário.
 * Guardamos apenas o ID do produto e a quantidade — o preço é sempre
 * consultado no ProductRepository na hora do checkout, nunca guardado
 * ou aceito vindo do cliente.
 */
public class CartItem {

    public int produtoId;
    public int quantidade;

    public CartItem(int produtoId, int quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }
}
