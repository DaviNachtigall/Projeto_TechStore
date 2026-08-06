package com.techstore.model;

import java.util.List;

/**
 * Representa um pedido já finalizado (após o checkout).
 * Guarda uma "foto" dos preços no momento da compra, para servir
 * como registro auditável (isso ajuda a mitigar a ameaça T03 —
 * Repúdio — descrita na documentação).
 */
public class Order {

    public int id;
    public int userId;
    public List<OrderItem> itens;
    public double total;
    public String status; // "APROVADO", "RECUSADO", etc.

    public Order(int id, int userId, List<OrderItem> itens, double total, String status) {
        this.id = id;
        this.userId = userId;
        this.itens = itens;
        this.total = total;
        this.status = status;
    }

    /** Item de um pedido: preço já "congelado" no momento da compra. */
    public static class OrderItem {
        public int produtoId;
        public int quantidade;
        public double precoUnitario;

        public OrderItem(int produtoId, int quantidade, double precoUnitario) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
        }
    }
}
