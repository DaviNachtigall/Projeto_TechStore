# Identificação e Descrição do Sistema

## Etapa 1: Casos de Abuso e Modelagem de Ameaças com STRIDE

### Identificação do Sistema

- **Nome do Sistema:** TechStore (Plataforma de E-Commerce)
- **Integrantes do Grupo:** Adriana Parra Agnolin, Bruno Henrique Moura Bitencourt e Davi Ramos Nachtigall.
- **Endereço do Repositório:** [https://github.com/DaviNachtigall/Projeto_TechStore](https://github.com/DaviNachtigall/Projeto_TechStore)
- **Justificativa da Escolha:** A escolha de uma plataforma de e-commerce justifica-se pela diversidade de perfis de usuário (clientes, administradores, vendedores), pela alta sensibilidade dos dados transacionados (dados bancários, dados pessoais cobertos pela LGPD, credenciais) e pelo impacto direto que falhas de segurança podem causar financeiramente e à reputação do negócio.

### Descrição do Sistema

O **TechStore** é uma plataforma web voltada para a compra e venda de produtos eletrônicos e tecnológicos online.

- **Problema que Resolve:** Conecta compradores a um catálogo diversificado de produtos tecnológicos com entrega em todo o país e processamento de pagamentos centralizado.

- **Quem Utiliza:**
  - **Clientes:** Navegam pelo catálogo, adicionam itens ao carrinho, efetuam pagamentos e acompanham o status das entregas.
  - **Administradores e Equipe de Logística:** Gerenciam o catálogo de produtos, alteram preços, visualizam relatórios financeiros, gerenciam permissões de acesso e atualizam o status de envio dos pedidos.

- **Principais Funcionalidades:** Cadastro e autenticação de usuários, busca e filtragem de produtos, gestão do carrinho de compras, checkout integrado com gateway de pagamento externo e painel de gestão de pedidos.

- **Informações Armazenadas ou Transmitidas:** Credenciais de acesso (hashes de senhas), dados pessoais dos clientes (CPF, endereço, e-mail, telefone), tokens de transação de cartão de crédito e histórico de transações/pedidos.

- **Recursos a Proteger:** Banco de dados de clientes e vendas, API de comunicação com o gateway de pagamento, painel administrativo e a integridade da precificação dos produtos.

### Usuários, Ativos e Pontos de Interação

#### Perfis de Acesso

- **Cliente Não Autenticado (Visitante):** Possui permissão restrita para visualizar o catálogo e buscar produtos.
- **Cliente Autenticado:** Pode gerenciar seu perfil, realizar compras, salvar endereços e acessar o histórico de pedidos.
- **Administrador do Sistema:** Possui acesso total ao painel de controle, relatórios operacionais e gestão de contas.

#### Ativos Críticos

- **Banco de Dados Relacional:** Contém Informações Pessoais Identificáveis (PII) e hashes de senhas.
- **Tokens de Sessão (JWT):** Chaves que garantem a autenticação contínua e segura do usuário.
- **API de Checkout e Pagamento:** Canal de integração direto com a adquirente/gateway de pagamento.
- **Serviço de Regra de Negócio e Precificação:** Módulo responsável pelo cálculo exato do valor total da compra e frete.

#### Pontos de Interação (Superfície de Ataque)

- Formatação e envio dos formulários de login e cadastro.
- Requisições HTTP do carrinho de compras (parâmetros de quantidade e valor).
- Endpoints da API REST de backend expostos para o aplicativo e interface web.

### Visão Geral da Arquitetura e Fluxo

```
[ Cliente / App Web ]
        │
   (Conexão Segura HTTPS / TLS)
        │
        ▼
[ API Gateway / Servidor Backend ] <────> [ Gateway de Pagamentos Externo ]
        │
        ▼
[ Banco de Dados Relacional ]
```

**Descrição do Fluxo**

1. **Interação do Cliente:** O usuário interage com a interface (Web), enviando requisições protegidas por protocolo HTTPS/TLS para a camada de backend.
2. **Processamento pelo Backend:** O servidor backend valida a autenticação e processa as regras de negócio, como a confirmação dos preços no catálogo (Tokens JWT).
3. **Integração de Pagamento:** Ao finalizar a compra, o backend comunica-se diretamente com o Gateway de Pagamentos externo via API segura para autorizar a transação financeira.
4. **Persistência:** Após a confirmação do pagamento, os dados do pedido e o status da transação são gravados no Banco de Dados Relacional.
