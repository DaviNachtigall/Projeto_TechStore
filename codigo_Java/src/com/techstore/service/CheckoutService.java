package com.techstore.service;

import com.techstore.model.CartItem;
import com.techstore.model.Order;
import com.techstore.model.Product;
import com.techstore.repository.OrderRepository;
import com.techstore.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Fecha o pedido (checkout).
 *
 * Regra de segurança central deste serviço (mitiga T02 / CA01):
 * o preço de cada item é SEMPRE lido do ProductRepository no momento
 * do checkout. O cliente nunca envia (e o servidor nunca aceita)
 * um preço vindo de fora.
 */
public class CheckoutService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public CheckoutService(ProductRepository productRepository,
                            OrderRepository orderRepository,
                            CartService cartService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order finalizarCompra(int userId) {
        List<CartItem> carrinho = cartService.getCarrinho(userId);
        if (carrinho.isEmpty()) {
            throw new IllegalStateException("Carrinho vazio.");
        }

        List<Order.OrderItem> itensDoPedido = new ArrayList<>();
        double total = 0.0;

        // 1) Valida estoque e calcula o total usando SEMPRE o preço do servidor
        for (CartItem itemCarrinho : carrinho) {
            Product produto = productRepository.buscarPorId(itemCarrinho.produtoId);
            if (produto == null) {
                throw new IllegalStateException("Produto do carrinho não existe mais.");
            }
            if (produto.estoque < itemCarrinho.quantidade) {
                throw new IllegalStateException("Estoque insuficiente para: " + produto.nome);
            }

            double precoOficial = produto.preco; // <- nunca vem do cliente
            total += precoOficial * itemCarrinho.quantidade;

            itensDoPedido.add(new Order.OrderItem(produto.id, itemCarrinho.quantidade, precoOficial));
        }

        // 2) Debita o estoque
        for (CartItem itemCarrinho : carrinho) {
            Product produto = productRepository.buscarPorId(itemCarrinho.produtoId);
            produto.estoque -= itemCarrinho.quantidade;
        }

        // 3) Simula a chamada ao gateway de pagamento externo.
        //    Em um sistema real, aqui entraria a chamada HTTPS para a
        //    adquirente/gateway, enviando o "total" calculado acima.
        boolean pagamentoAprovado = simularGatewayDePagamento(total);
        String status = pagamentoAprovado ? "APROVADO" : "RECUSADO";

        // 4) Registra o pedido (nosso log auditável - mitiga T03)
        Order pedido = orderRepository.salvar(userId, itensDoPedido, total, status);

        // 5) Limpa o carrinho após finalizar a compra
        cartService.limparCarrinho(userId);

        return pedido;
    }

    private boolean simularGatewayDePagamento(double total) {
        // Lógica de exemplo bem simples: qualquer valor positivo é "aprovado".
        return total > 0;
    }
}
