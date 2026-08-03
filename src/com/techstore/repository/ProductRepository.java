package com.techstore.repository;

import com.techstore.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * "Banco de dados" de produtos guardado em memória.
 * O preço e o estoque que estão aqui são sempre a "fonte da verdade":
 * o checkout nunca confia em preço vindo do cliente.
 */
public class ProductRepository {

    private final Map<Integer, Product> produtos = new HashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    public Product salvar(String nome, double preco, int estoque) {
        int id = proximoId.getAndIncrement();
        Product produto = new Product(id, nome, preco, estoque);
        produtos.put(id, produto);
        return produto;
    }

    public Product buscarPorId(int id) {
        return produtos.get(id);
    }

    public List<Product> listarTodos() {
        return new ArrayList<>(produtos.values());
    }
}
