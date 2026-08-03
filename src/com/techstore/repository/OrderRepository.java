package com.techstore.repository;

import com.techstore.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * "Banco de dados" de pedidos guardado em memória.
 * Funciona como o nosso "log auditável": uma vez criado, o pedido
 * não é mais alterado (mitigando o Repúdio - T03 da documentação).
 */
public class OrderRepository {

    private final Map<Integer, Order> pedidos = new HashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    public Order salvar(int userId, List<Order.OrderItem> itens, double total, String status) {
        int id = proximoId.getAndIncrement();
        Order pedido = new Order(id, userId, itens, total, status);
        pedidos.put(id, pedido);
        return pedido;
    }

    public List<Order> listarTodos() {
        return new ArrayList<>(pedidos.values());
    }

    public List<Order> listarPorUsuario(int userId) {
        List<Order> resultado = new ArrayList<>();
        for (Order pedido : pedidos.values()) {
            if (pedido.userId == userId) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }
}
