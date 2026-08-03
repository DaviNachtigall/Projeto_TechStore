package com.techstore.service;

import com.techstore.model.CartItem;
import com.techstore.model.Product;
import com.techstore.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Carrinho de compras: guarda, por usuário, uma lista de (produtoId, quantidade).
 * Importante: o carrinho NUNCA guarda preço. O preço é sempre buscado
 * no ProductRepository quando for necessário (ex: mostrar o carrinho
 * ou fechar o pedido). Isso é o que impede o ataque descrito no
 * caso de abuso CA01 (alterar o preço no corpo da requisição).
 */
public class CartService {

    private final Map<Integer, List<CartItem>> carrinhosPorUsuario = new HashMap<>();
    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void adicionarItem(int userId, int produtoId, int quantidade) {
        Product produto = productRepository.buscarPorId(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        List<CartItem> carrinho = carrinhosPorUsuario.computeIfAbsent(userId, k -> new ArrayList<>());

        // Se o produto já está no carrinho, apenas soma a quantidade
        for (CartItem item : carrinho) {
            if (item.produtoId == produtoId) {
                item.quantidade += quantidade;
                return;
            }
        }
        carrinho.add(new CartItem(produtoId, quantidade));
    }

    public List<CartItem> getCarrinho(int userId) {
        return carrinhosPorUsuario.getOrDefault(userId, new ArrayList<>());
    }

    public void limparCarrinho(int userId) {
        carrinhosPorUsuario.remove(userId);
    }
}
