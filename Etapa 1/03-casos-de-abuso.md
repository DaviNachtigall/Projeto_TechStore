# Casos de Abuso

## Etapa 1: Casos de Abuso e Modelagem de Ameaças com STRIDE

### CA01 — Alteração Maliciosa de Valor no Checkout

- **Ator:** Cliente mal-intencionado.
- **Objetivo:** Adquirir produtos por um valor irrisório definido por ele mesmo.
- **Condições Necessárias:** O sistema verifica o preço do produto apenas na tela do cliente e aceita o valor enviado na requisição sem revalidar com o banco de dados antes de cobrar no gateway.

- **Sequência de Ações:**
  1. O atacante adiciona um produto de R$ 5.000,00 ao carrinho de compras.
  2. Antes de confirmar o pagamento, ele usa um programa que permite ver e editar os dados enviados pelo site.
  3. Ele muda o preço enviado de R$ 5.000,00 para R$ 1,00.
  4. O backend aceita esse novo valor sem checar se está correto e cobra apenas R$ 1,00 no cartão.
  5. A compra é aprovada e o pedido é gerado.

- **Impacto Esperado:** Prejuízo financeiro direto para o e-commerce e falha na gestão de estoque.
- **Categoria STRIDE Relacionada:** **Tampering** (Adulteração de dados).

---

### CA02 — Acesso Não Autorizado via Credenciais Vazadas (*Credential Stuffing*)

- **Ator:** Atacante externo com listas de e-mails e senhas vazados de outros sites.
- **Objetivo:** Entrar em contas de clientes reais para comprar em nome deles e acessar seus dados.
- **Condições Necessárias:** O login não bloqueia várias tentativas seguidas, não pede confirmação extra (como código por SMS) e não usa captcha, permitindo testar muitas senhas rapidamente.

- **Sequência de Ações:**
  1. O atacante consegue uma lista de e-mails e senhas que vazaram de outros sites.
  2. Ele usa um programa automático que tenta fazer login no TechStore com cada combinação da lista.
  3. Como o site não bloqueia tentativas repetidas, o programa testa milhares de combinações seguidas.
  4. Algumas pessoas usam a mesma senha em vários sites, então algumas contas são invadidas com sucesso.
  5. O atacante entra nessas contas, vê os dados salvos e faz compras usando o cartão da vítima.

- **Impacto Esperado:** Compras fraudulentas, contas de clientes invadidas e possível responsabilização da plataforma por falha de proteção.
- **Categoria STRIDE Relacionada:** **Spoofing** (Falsificação de identidade).

---

### CA03 — Repúdio de Transação por Ausência de Logs Auditáveis

- **Ator:** Cliente mal-intencionado que já fez uma compra de verdade.
- **Objetivo:** Negar que fez a compra para conseguir o dinheiro de volta sem motivo real.
- **Condições Necessárias:** O sistema não guarda registros suficientes (como IP, data e hora exatas) que comprovem, sem dúvida, quem fez a compra.

- **Sequência de Ações:**
  1. O atacante faz uma compra normalmente, logado na sua própria conta.
  2. Depois de receber o produto, ele entra em contato com o suporte dizendo que nunca fez essa compra.
  3. O suporte tenta verificar o que aconteceu, mas só encontra o registro do pedido, sem detalhes (como IP ou horário exato) que provem que foi ele mesmo quem comprou.
  4. Sem provas suficientes, a empresa aceita devolver o dinheiro (estorno).

- **Impacto Esperado:** Perda financeira direta com estornos indevidos, dificuldade jurídica em contestar disputas e abertura de precedente para fraudes recorrentes.
- **Categoria STRIDE Relacionada:** **Repudiation** (Repúdio).

---

### CA04 — Extração de Dados de Clientes via Enumeração de IDs (IDOR)

- **Ator:** Atacante externo ou usuário cadastrado.
- **Objetivo:** Coletar em massa dados pessoais (CPF, e-mail, endereço) e histórico de compras de outros clientes.
- **Condições Necessárias:** A página de perfil do usuário usa números simples e sequenciais no endereço (ex: /perfil?id=102) e não confere se esse número pertence mesmo à pessoa que está logada.

- **Sequência de Ações:**
  1. O atacante faz login na sua própria conta, que tem o número 102.
  2. Ele muda esse número no endereço do site para 101.
  3. O sistema não verifica se o número 101 é realmente dele, e mostra os dados completos de outro cliente.
  4. O atacante repete isso automaticamente, trocando o número várias vezes, e consegue os dados de todos os clientes cadastrados.

- **Impacto Esperado:** Vazamento massivo de informações pessoais sensíveis, violação das normas da LGPD e danos severos à reputação da empresa.
- **Categoria STRIDE Relacionada:** **Information Disclosure** (Exposição de informação).

---

### CA05 — Indisponibilidade do Checkout em Período de Alto Tráfego

- **Ator:** Atacante externo (concorrente desleal, extorsionário ou "hacktivista").
- **Objetivo:** Deixar o site fora do ar durante um período importante de vendas, causando prejuízo financeiro e de imagem.
- **Condições Necessárias:** A página de pagamento não tem proteção contra excesso de acessos, permitindo que um volume muito grande de requisições sobrecarregue o servidor.

- **Sequência de Ações:**
  1. O atacante descobre que a loja terá uma grande promoção (ex: Black Friday).
  2. Ele usa programas ou vários computadores para enviar uma quantidade enorme de acessos ao mesmo tempo na página de pagamento.
  3. O servidor não aguenta tantos acessos de uma vez e fica sobrecarregado.
  4. Clientes reais não conseguem finalizar suas compras, pois o site trava ou apresenta erro.
  5. O sistema fica parcial ou totalmente fora do ar durante o período de vendas.

- **Impacto Esperado:** Perda direta de vendas no período mais lucrativo do ano, danos à reputação e possível migração de clientes para concorrentes.
- **Categoria STRIDE Relacionada:** **Denial of Service** (Negação de serviço).

---

### CA06 — Elevação de Privilégio via Manipulação do Parâmetro de Papel (Role)

- **Ator:** Cliente autenticado comum.
- **Objetivo:** Conseguir acesso ao painel administrativo para mudar o catálogo, preços ou dados de outros usuários.
- **Condições Necessárias:** O sistema confia na informação de "tipo de usuário" que vem do cliente, em vez de verificar isso de forma segura no servidor.

- **Sequência de Ações:**
  1. O atacante faz login normalmente como cliente comum.
  2. Usando um programa que mostra os dados enviados pelo site, ele encontra a informação "role": "user" (tipo de usuário: cliente).
  3. Ele altera essa informação para "role": "admin" (tipo de usuário: administrador).
  4. O sistema aceita essa mudança sem checar de verdade qual é o tipo de conta do usuário no banco de dados.
  5. O atacante passa a ter acesso às funções exclusivas do painel administrativo.

- **Impacto Esperado:** Acesso completo ao sistema, permitindo alteração no catálogo, exclusão de dados, manipulação de preços e roubo de informações sensíveis de todos os clientes.
- **Categoria STRIDE Relacionada:** **Elevation of Privilege** (Elevação de privilégio).
