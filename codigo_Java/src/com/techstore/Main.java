package com.techstore;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.techstore.model.CartItem;
import com.techstore.model.Order;
import com.techstore.model.Product;
import com.techstore.model.User;
import com.techstore.repository.OrderRepository;
import com.techstore.repository.ProductRepository;
import com.techstore.repository.UserRepository;
import com.techstore.service.AuthService;
import com.techstore.service.CartService;
import com.techstore.service.CheckoutService;
import com.techstore.service.TokenService;
import com.techstore.util.PasswordUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Backend do TechStore.
 *
 * Propositalmente escrito SEM frameworks (sem Spring, sem bibliotecas de
 * JSON) para que a lógica fique fácil de acompanhar: usamos apenas o
 * HttpServer que já vem dentro do próprio Java (com.sun.net.httpserver).
 *
 * Formato das requisições:
 *  - Corpo do POST no formato "application/x-www-form-urlencoded"
 *    (o mesmo formato que um <form> HTML comum envia), ex: "email=a@b.com&senha=123"
 *  - Autenticação: depois do login, o cliente recebe um "token" e deve
 *    enviá-lo em toda requisição protegida (via parâmetro "token").
 *
 * Rotas disponíveis:
 *   POST /cadastro          nome, email, cpf, senha
 *   POST /login             email, senha
 *   GET  /produtos
 *   POST /admin/produtos    token, nome, preco, estoque      (somente ADMIN)
 *   POST /carrinho/adicionar token, produtoId, quantidade
 *   GET  /carrinho           token
 *   POST /checkout            token
 *   GET  /perfil              token, id                      (dono do id, ou ADMIN)
 *   GET  /admin/pedidos       token                           (somente ADMIN)
 */
public class Main {

    // "Banco de dados" e serviços - uma instância única (singleton simples) para todo o sistema
    private static final UserRepository userRepository = new UserRepository();
    private static final ProductRepository productRepository = new ProductRepository();
    private static final OrderRepository orderRepository = new OrderRepository();

    private static final TokenService tokenService = new TokenService();
    private static final AuthService authService = new AuthService(userRepository);
    private static final CartService cartService = new CartService(productRepository);
    private static final CheckoutService checkoutService =
            new CheckoutService(productRepository, orderRepository, cartService);

    public static void main(String[] args) throws IOException {
        popularDadosDeExemplo();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/cadastro", Main::handleCadastro);
        server.createContext("/login", Main::handleLogin);
        server.createContext("/produtos", Main::handleListarProdutos);
        server.createContext("/admin/produtos", Main::handleCriarProduto);
        server.createContext("/carrinho/adicionar", Main::handleAdicionarAoCarrinho);
        server.createContext("/carrinho", Main::handleVerCarrinho);
        server.createContext("/checkout", Main::handleCheckout);
        server.createContext("/perfil", Main::handlePerfil);
        server.createContext("/admin/pedidos", Main::handleListarPedidos);

        server.setExecutor(null); // usa o executor padrão (thread por requisição)
        server.start();

        System.out.println("TechStore backend rodando em http://localhost:8080");
    }

    // ---------------------------------------------------------------
    // Dados de exemplo, para o sistema já nascer com algo para testar
    // ---------------------------------------------------------------
    private static void popularDadosDeExemplo() {
        // Um administrador padrão
        String senhaHashAdmin = PasswordUtil.hash("admin123");
        userRepository.salvar("Administrador", "admin@techstore.com", "000.000.000-00", senhaHashAdmin, "ADMIN");

        // Alguns produtos de catálogo
        productRepository.salvar("Notebook Gamer", 5000.00, 10);
        productRepository.salvar("Mouse sem fio", 89.90, 50);
        productRepository.salvar("Teclado Mecânico", 250.00, 30);
    }

    // ---------------------------------------------------------------
    // Handlers de cada rota
    // ---------------------------------------------------------------

    private static void handleCadastro(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "POST")) return;

        Map<String, String> dados = lerCorpoForm(exchange);
        try {
            User user = authService.cadastrar(
                    dados.get("nome"), dados.get("email"), dados.get("cpf"), dados.get("senha"));
            responderJson(exchange, 200, "{\"mensagem\":\"Cadastro realizado\",\"id\":" + user.id + "}");
        } catch (IllegalArgumentException e) {
            responderJson(exchange, 400, "{\"erro\":\"" + e.getMessage() + "\"}");
        }
    }

    private static void handleLogin(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "POST")) return;

        Map<String, String> dados = lerCorpoForm(exchange);
        User user = authService.autenticar(dados.get("email"), dados.get("senha"));
        if (user == null) {
            // Mensagem genérica: não revela se o problema foi o e-mail ou a senha (mitiga T01)
            responderJson(exchange, 401, "{\"erro\":\"E-mail ou senha inválidos\"}");
            return;
        }
        String token = tokenService.gerarToken(user.id);
        responderJson(exchange, 200, "{\"token\":\"" + token + "\",\"role\":\"" + user.role + "\"}");
    }

    private static void handleListarProdutos(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "GET")) return;

        List<Product> produtos = productRepository.listarTodos();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < produtos.size(); i++) {
            Product p = produtos.get(i);
            if (i > 0) json.append(",");
            json.append("{\"id\":").append(p.id)
                    .append(",\"nome\":\"").append(p.nome).append("\"")
                    .append(",\"preco\":").append(p.preco)
                    .append(",\"estoque\":").append(p.estoque)
                    .append("}");
        }
        json.append("]");
        responderJson(exchange, 200, json.toString());
    }

    private static void handleCriarProduto(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "POST")) return;

        Map<String, String> dados = lerCorpoForm(exchange);
        User usuarioLogado = usuarioAutenticado(exchange, dados.get("token"));
        if (usuarioLogado == null) return; // erro de autenticação já foi respondido

        // Somente ADMIN pode cadastrar produto (mitiga T06 - Elevação de Privilégio)
        if (!usuarioLogado.isAdmin()) {
            responderJson(exchange, 403, "{\"erro\":\"Acesso restrito ao administrador\"}");
            return;
        }

        try {
            String nome = dados.get("nome");
            double preco = Double.parseDouble(dados.get("preco"));
            int estoque = Integer.parseInt(dados.get("estoque"));
            Product produto = productRepository.salvar(nome, preco, estoque);
            responderJson(exchange, 200, "{\"mensagem\":\"Produto criado\",\"id\":" + produto.id + "}");
        } catch (NumberFormatException e) {
            responderJson(exchange, 400, "{\"erro\":\"Preço ou estoque inválido\"}");
        }
    }

    private static void handleAdicionarAoCarrinho(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "POST")) return;

        Map<String, String> dados = lerCorpoForm(exchange);
        User usuarioLogado = usuarioAutenticado(exchange, dados.get("token"));
        if (usuarioLogado == null) return;

        try {
            int produtoId = Integer.parseInt(dados.get("produtoId"));
            int quantidade = Integer.parseInt(dados.get("quantidade"));
            cartService.adicionarItem(usuarioLogado.id, produtoId, quantidade);
            responderJson(exchange, 200, "{\"mensagem\":\"Item adicionado ao carrinho\"}");
        } catch (NumberFormatException e) {
            responderJson(exchange, 400, "{\"erro\":\"produtoId ou quantidade inválidos\"}");
        } catch (IllegalArgumentException e) {
            responderJson(exchange, 400, "{\"erro\":\"" + e.getMessage() + "\"}");
        }
    }

    private static void handleVerCarrinho(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "GET")) return;

        Map<String, String> params = lerQueryString(exchange);
        User usuarioLogado = usuarioAutenticado(exchange, params.get("token"));
        if (usuarioLogado == null) return;

        List<CartItem> carrinho = cartService.getCarrinho(usuarioLogado.id);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < carrinho.size(); i++) {
            CartItem item = carrinho.get(i);
            // O preço mostrado aqui vem sempre do ProductRepository (fonte da verdade)
            Product produto = productRepository.buscarPorId(item.produtoId);
            if (i > 0) json.append(",");
            json.append("{\"produtoId\":").append(item.produtoId)
                    .append(",\"nome\":\"").append(produto != null ? produto.nome : "?").append("\"")
                    .append(",\"quantidade\":").append(item.quantidade)
                    .append(",\"precoUnitario\":").append(produto != null ? produto.preco : 0)
                    .append("}");
        }
        json.append("]");
        responderJson(exchange, 200, json.toString());
    }

    private static void handleCheckout(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "POST")) return;

        Map<String, String> dados = lerCorpoForm(exchange);
        User usuarioLogado = usuarioAutenticado(exchange, dados.get("token"));
        if (usuarioLogado == null) return;

        try {
            Order pedido = checkoutService.finalizarCompra(usuarioLogado.id);
            responderJson(exchange, 200, "{\"mensagem\":\"Pedido realizado\",\"pedidoId\":" + pedido.id
                    + ",\"total\":" + pedido.total + ",\"status\":\"" + pedido.status + "\"}");
        } catch (IllegalStateException e) {
            responderJson(exchange, 400, "{\"erro\":\"" + e.getMessage() + "\"}");
        }
    }

    private static void handlePerfil(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "GET")) return;

        Map<String, String> params = lerQueryString(exchange);
        User usuarioLogado = usuarioAutenticado(exchange, params.get("token"));
        if (usuarioLogado == null) return;

        int idSolicitado;
        try {
            idSolicitado = Integer.parseInt(params.get("id"));
        } catch (NumberFormatException e) {
            responderJson(exchange, 400, "{\"erro\":\"id inválido\"}");
            return;
        }

        // Correção do IDOR do caso de abuso CA02: só pode ver o próprio
        // perfil, a menos que seja administrador.
        if (idSolicitado != usuarioLogado.id && !usuarioLogado.isAdmin()) {
            responderJson(exchange, 403, "{\"erro\":\"Você não tem permissão para ver este perfil\"}");
            return;
        }

        User alvo = userRepository.buscarPorId(idSolicitado);
        if (alvo == null) {
            responderJson(exchange, 404, "{\"erro\":\"Usuário não encontrado\"}");
            return;
        }

        responderJson(exchange, 200, "{\"id\":" + alvo.id
                + ",\"nome\":\"" + alvo.nome + "\""
                + ",\"email\":\"" + alvo.email + "\""
                + ",\"cpf\":\"" + alvo.cpf + "\""
                + ",\"role\":\"" + alvo.role + "\"}");
    }

    private static void handleListarPedidos(HttpExchange exchange) throws IOException {
        if (!metodoEh(exchange, "GET")) return;

        Map<String, String> params = lerQueryString(exchange);
        User usuarioLogado = usuarioAutenticado(exchange, params.get("token"));
        if (usuarioLogado == null) return;

        // Somente ADMIN pode ver todos os pedidos (mitiga T06)
        if (!usuarioLogado.isAdmin()) {
            responderJson(exchange, 403, "{\"erro\":\"Acesso restrito ao administrador\"}");
            return;
        }

        List<Order> pedidos = orderRepository.listarTodos();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < pedidos.size(); i++) {
            Order pedido = pedidos.get(i);
            if (i > 0) json.append(",");
            json.append("{\"id\":").append(pedido.id)
                    .append(",\"userId\":").append(pedido.userId)
                    .append(",\"total\":").append(pedido.total)
                    .append(",\"status\":\"").append(pedido.status).append("\"")
                    .append("}");
        }
        json.append("]");
        responderJson(exchange, 200, json.toString());
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares (parsing, autenticação, resposta HTTP)
    // ---------------------------------------------------------------

    /** Confere se o token pertence a um usuário válido; senão, já responde com erro 401. */
    private static User usuarioAutenticado(HttpExchange exchange, String token) throws IOException {
        Integer userId = tokenService.usuarioDoToken(token);
        if (userId == null) {
            responderJson(exchange, 401, "{\"erro\":\"Token inválido ou ausente. Faça login novamente.\"}");
            return null;
        }
        User user = userRepository.buscarPorId(userId);
        if (user == null) {
            responderJson(exchange, 401, "{\"erro\":\"Usuário do token não existe mais.\"}");
            return null;
        }
        return user;
    }

    private static boolean metodoEh(HttpExchange exchange, String metodoEsperado) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(metodoEsperado)) {
            responderJson(exchange, 405, "{\"erro\":\"Método HTTP não permitido nesta rota\"}");
            return false;
        }
        return true;
    }

    /** Lê o corpo de uma requisição POST no formato "chave=valor&chave2=valor2". */
    private static Map<String, String> lerCorpoForm(HttpExchange exchange) throws IOException {
        byte[] bytes = exchange.getRequestBody().readAllBytes();
        String corpo = new String(bytes, StandardCharsets.UTF_8);
        return parseFormUrlEncoded(corpo);
    }

    /** Lê os parâmetros da query string de uma requisição GET ("?chave=valor&..."). */
    private static Map<String, String> lerQueryString(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        return parseFormUrlEncoded(query);
    }

    private static Map<String, String> parseFormUrlEncoded(String texto) {
        Map<String, String> resultado = new HashMap<>();
        if (texto == null || texto.isEmpty()) {
            return resultado;
        }
        for (String par : texto.split("&")) {
            String[] partes = par.split("=", 2);
            String chave = URLDecoder.decode(partes[0], StandardCharsets.UTF_8);
            String valor = partes.length > 1 ? URLDecoder.decode(partes[1], StandardCharsets.UTF_8) : "";
            resultado.put(chave, valor);
        }
        return resultado;
    }

    private static void responderJson(HttpExchange exchange, int codigoStatus, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(codigoStatus, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
