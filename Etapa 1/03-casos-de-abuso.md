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
- **Categorias STRIDE Relacionadas:** **Tampering** (Adulteração de dados) e **Elevation of Privilege** (Elevação de privilégio).

---

### CA02 — Acesso Direto a Dados de Terceiros via IDOR (*Insecure Direct Object Reference*)

- **Ator:** Atacante externo ou usuário cadastrado.
- **Objetivo:** Coletar em massa dados pessoais (CPF, e-mail, endereço) e histórico de compras de outros clientes.
- **Condições Necessárias:** A API de dados do usuário utiliza identificadores numéricos sequenciais na URL (ex: /api/v1/perfil?id=102) sem verificar se o ID pertence ao token do usuário que fez a requisição.

- **Sequência de Ações:**
  1. O atacante realiza o login no sistema com a sua conta legítima (ID 102).
  2. Altera manualmente o parâmetro no endereço da requisição para /api/v1/perfil?id=101.
  3. O backend consulta o banco de dados sem verificar o vínculo de posse e retorna os dados completos do cliente 101.
  4. O atacante executa um script automatizado variando a sequência numérica dos IDs para extrair os dados de toda a base de usuários.

- **Impacto Esperado:** Vazamento massivo de informações pessoais sensíveis, violação das normas da LGPD e danos severos à reputação da empresa.
- **Categorias STRIDE Relacionadas:** **Information Disclosure** (Exposição de informação) e **Elevation of Privilege** (Elevação de privilégio).
