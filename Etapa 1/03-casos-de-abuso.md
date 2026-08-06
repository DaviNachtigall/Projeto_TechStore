# Casos de Abuso

## Etapa 1: Casos de Abuso e Modelagem de Ameaças com STRIDE

### CA01 — Alteração Maliciosa de Valor no Checkout

- **Ator:** Cliente mal-intencionado.
- **Objetivo:** Adquirir produtos por um valor irrisório definido por ele mesmo.
- **Condições Necessárias:** O sistema realiza a verificação do preço do produto apenas na interface do cliente (*frontend*) e aceita o valor enviado na requisição HTTP sem revalidar com o banco de dados antes de cobrar no gateway.

- **Sequência de Ações:**
  1. O atacante adiciona um produto de R$ 5.000,00 ao carrinho de compras.
  2. Ao acionar o botão de pagamento, o atacante intercepta a requisição usando um proxy de interceptação.
  3. Modifica o parâmetro de preço no corpo da requisição de "preco": 5000.00 para "preco": 1.00.
  4. O backend processa o pedido aceitando o valor enviado e encaminha a cobrança de R$ 1,00 para a processadora do cartão.
  5. A transação é aprovada e o pedido é gerado.

- **Impacto Esperado:** Prejuízo financeiro direto para o e-commerce e falha na gestão de estoque.
- **Categoria STRIDE Relacionada:** **Tampering** (Adulteração de dados).

---

### CA02 — Acesso Não Autorizado via Credenciais Vazadas (*Credential Stuffing*)

- **Ator:** Atacante externo com posse de listas de credenciais vazadas de outros serviços.
- **Objetivo:** Assumir a identidade de clientes legítimos para realizar compras fraudulentas e acessar dados pessoais.
- **Condições Necessárias:** O sistema de login não possui limite de tentativas (*rate limiting*), captcha ou autenticação multifator, permitindo testes automatizados de credenciais em larga escala.

- **Sequência de Ações:**
  1. O atacante obtém uma lista de pares e-mail/senha vazados de outros vazamentos públicos.
  2. Utiliza uma ferramenta automatizada para submeter essas combinações no formulário de login do TechStore.
  3. Como não há bloqueio por tentativas repetidas, o script testa milhares de combinações em sequência.
  4. Algumas credenciais reutilizadas pelos usuários coincidem, permitindo login bem-sucedido em várias contas.
  5. O atacante acessa os dados salvos (endereços, cartões tokenizados) e realiza compras em nome das vítimas.

- **Impacto Esperado:** Compras fraudulentas, comprometimento de contas de clientes e possível responsabilização da plataforma por falha de proteção.
- **Categoria STRIDE Relacionada:** **Spoofing** (Falsificação de identidade).

---

### CA03 — Repúdio de Transação por Ausência de Logs Auditáveis

- **Ator:** Cliente mal-intencionado que já realizou uma compra legítima.
- **Objetivo:** Negar a autoria de uma compra para obter reembolso indevido ou evitar cobrança.
- **Condições Necessárias:** O sistema não mantém registros auditáveis (logs) suficientes — como IP de origem, timestamp preciso, ou trilha de confirmação — vinculados de forma inequívoca à ação do usuário autenticado.

- **Sequência de Ações:**
  1. O atacante realiza uma compra normalmente, autenticado em sua própria conta.
  2. Após receber o produto, entra em contato com o suporte alegando que nunca efetuou a compra.
  3. A equipe de suporte tenta verificar o histórico de ações, mas encontra apenas o registro do pedido, sem logs detalhados que comprovem a origem da requisição (IP, dispositivo, hora exata da confirmação).
  4. Sem evidências técnicas robustas para contestar a alegação, a empresa aceita o pedido de estorno (*chargeback*).

- **Impacto Esperado:** Perda financeira direta com estornos indevidos, dificuldade jurídica em contestar disputas e abertura de precedente para fraudes recorrentes.
- **Categoria STRIDE Relacionada:** **Repudiation** (Repúdio).

---

### CA04 — Extração de Dados de Clientes via Enumeração de IDs (IDOR)

- **Ator:** Atacante externo ou usuário cadastrado.
- **Objetivo:** Coletar em massa dados pessoais (CPF, e-mail, endereço) e histórico de compras de outros clientes.
- **Condições Necessárias:** A API de dados do usuário utiliza identificadores numéricos sequenciais na URL (ex: /api/v1/perfil?id=102) sem verificar se o ID pertence ao token do usuário que fez a requisição.

- **Sequência de Ações:**
  1. O atacante realiza o login no sistema com a sua conta legítima (ID 102).
  2. Altera manualmente o parâmetro no endereço da requisição para /api/v1/perfil?id=101.
  3. O backend consulta o banco de dados sem verificar o vínculo de posse e retorna os dados completos do cliente 101.
  4. O atacante executa um script automatizado variando a sequência numérica dos IDs para extrair os dados de toda a base de usuários.

- **Impacto Esperado:** Vazamento massivo de informações pessoais sensíveis, violação das normas da LGPD e danos severos à reputação da empresa.
- **Categoria STRIDE Relacionada:** **Information Disclosure** (Exposição de informação).

---

### CA05 — Indisponibilidade do Checkout em Período de Alto Tráfego

- **Ator:** Atacante externo (concorrente desleal, extorsionário ou "hacktivista").
- **Objetivo:** Tornar a plataforma indisponível durante um período crítico de vendas, causando prejuízo financeiro e de reputação.
- **Condições Necessárias:** A rota de checkout não possui proteção contra excesso de requisições (*rate limiting*, WAF ou sistema anti-DDoS), permitindo que um volume anormal de tráfego sobrecarregue o servidor.

- **Sequência de Ações:**
  1. O atacante identifica que a plataforma terá uma promoção de grande porte (ex: Black Friday).
  2. Utiliza uma rede de bots (botnet) ou ferramentas de flood para disparar milhares de requisições simultâneas à rota de checkout.
  3. O servidor backend, sem mecanismos de limitação ou balanceamento adequado, esgota seus recursos (CPU, memória, conexões de banco de dados).
  4. Clientes legítimos não conseguem finalizar compras devido a timeouts e erros 500.
  5. O sistema fica parcial ou totalmente indisponível durante o pico de vendas.

- **Impacto Esperado:** Perda direta de vendas no período mais lucrativo do ano, danos à reputação e possível migração de clientes para concorrentes.
- **Categoria STRIDE Relacionada:** **Denial of Service** (Negação de serviço).

---

### CA06 — Elevação de Privilégio via Manipulação do Parâmetro de Papel (Role)

- **Ator:** Cliente autenticado comum.
- **Objetivo:** Obter acesso ao painel administrativo para manipular o catálogo, preços ou dados de outros usuários.
- **Condições Necessárias:** O backend confia no valor do campo "role" enviado pelo cliente na requisição, em vez de validar o papel do usuário exclusivamente a partir de informações armazenadas e verificadas no servidor (ex: no token JWT assinado).

- **Sequência de Ações:**
  1. O atacante realiza login normalmente como cliente comum.
  2. Intercepta uma requisição de atualização de perfil ou autenticação usando um proxy.
  3. Identifica o campo "role": "user" no corpo da requisição e o altera para "role": "admin".
  4. O backend aceita o valor enviado sem revalidar contra o registro real do usuário no banco de dados.
  5. O atacante passa a ter acesso às rotas e funcionalidades exclusivas do painel administrativo.

- **Impacto Esperado:** Acesso completo ao sistema, permitindo alteração no catálogo, exclusão de dados, manipulação de preços e roubo de informações sensíveis de todos os clientes.
- **Categoria STRIDE Relacionada:** **Elevation of Privilege** (Elevação de privilégio).
