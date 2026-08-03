# TechStore Backend

Backend simples do TechStore, escrito em **Java puro** (sem Spring, sem Maven/Gradle,
sem bibliotecas de JSON). 
Mais detalhes da documentação: https://docs.google.com/document/d/1j5WWgimgs508_vjUn12m266oI9SJPUDeTA702Mq0-Jw/edit?usp=sharing
(`com.sun.net.httpserver.HttpServer`).

## Como compilar e rodar

Pré-requisito: ter o **JDK** instalado (Java 17 ou superior).

```bash
# 1. Compilar
javac -d out $(find src -name "*.java")

# 2. Rodar
java -cp out com.techstore.Main
```

O servidor sobe em `http://localhost:8080`.

Dados já criados automaticamente ao iniciar:
- Um administrador: `admin@techstore.com` / senha `admin123`
- Três produtos de exemplo no catálogo

## Estrutura do projeto

```
src/com/techstore/
├── Main.java              # servidor HTTP + rotas (ponto de entrada)
├── model/                 # classes de dados (User, Product, CartItem, Order)
├── repository/             # "banco de dados" em memória (HashMap)
├── service/                # regras de negócio (login, carrinho, checkout, token)
└── util/                    # utilitário de hash de senha
```

## Rotas disponíveis

Corpo dos `POST` no formato `application/x-www-form-urlencoded`
(o mesmo formato de um `<form>` HTML comum: `chave=valor&chave2=valor2`).

| Método | Rota                   | Parâmetros                              | Quem pode acessar         |
|--------|------------------------|------------------------------------------|---------------------------|
| POST   | `/cadastro`             | nome, email, cpf, senha                  | qualquer pessoa           |
| POST   | `/login`                | email, senha                             | qualquer pessoa           |
| GET    | `/produtos`             | —                                        | qualquer pessoa           |
| POST   | `/admin/produtos`       | token, nome, preco, estoque               | somente ADMIN             |
| POST   | `/carrinho/adicionar`   | token, produtoId, quantidade              | cliente autenticado       |
| GET    | `/carrinho`             | token                                     | cliente autenticado       |
| POST   | `/checkout`             | token                                     | cliente autenticado       |
| GET    | `/perfil`               | token, id                                 | dono do id, ou ADMIN      |
| GET    | `/admin/pedidos`        | token                                     | somente ADMIN             |



## Como o código se conecta às ameaças da documentação (STRIDE)

| Ameaça | Onde foi tratada | Como |
|---|---|---|
| **T01 — Spoofing** | `AuthService`, `PasswordUtil` | Senha nunca guardada em texto puro (hash SHA-256); mensagem de erro de login genérica (não revela se o e-mail existe). |
| **T02 — Tampering (CA01)** | `CartItem`, `CheckoutService` | O carrinho guarda só `produtoId` e `quantidade`. O preço usado no checkout é **sempre** lido do `ProductRepository` no servidor — o preço nunca vem (nem é aceito) do corpo da requisição do cliente. |
| **T03 — Repudiation** | `OrderRepository`, `Order` | Todo checkout gera um `Order` imutável, guardado no servidor, com itens, preços e status — funciona como registro auditável. |
| **T04 — Information Disclosure (CA02 / IDOR)** | `Main.handlePerfil` | Antes de devolver os dados de um perfil, o servidor confere se o `id` pedido é o mesmo do dono do token — ou se quem pediu é ADMIN. Sem essa checagem, seria o IDOR descrito na documentação. |
| **T05 — Denial of Service** | (fora do escopo deste código) | Mitigação real depende de infraestrutura (rate limiting, WAF, CDN) — não é resolvido só na lógica da aplicação. |
| **T06 — Elevation of Privilege** | `Main.handleCriarProduto`, `Main.handleListarPedidos` | O `role` do usuário vem do banco (a partir do token), nunca de um campo enviado pelo cliente. Rotas administrativas checam `usuarioLogado.isAdmin()` no servidor. |


