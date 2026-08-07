# Análise e Priorização de Riscos

## Etapa 2: Análise, Priorização e Tratamento de Riscos com o NIST CSF

### Objetivo

Esta etapa dá continuidade à análise iniciada na Etapa 1, transformando as ameaças STRIDE e os casos de abuso identificados anteriormente em **riscos** avaliáveis, comparáveis, priorizáveis e tratáveis. O sistema analisado, o repositório, as ameaças STRIDE e os casos de abuso permanecem os mesmos definidos na Etapa 1.

---

### 13.1 Critérios de Probabilidade

| Valor | Classificação | Critério |
| --- | --- | --- |
| 1 | Baixa | O evento depende de condições incomuns, acesso muito específico ou grande capacidade técnica. |
| 2 | Média-baixa | O evento é possível, mas depende de uma vulnerabilidade ou condição específica. |
| 3 | Média-alta | O evento é plausível e pode ocorrer em situações comuns de uso ou ataque. |
| 4 | Alta | O evento pode ocorrer com facilidade, frequência ou durante condições previsíveis do sistema. |

### 13.2 Critérios de Impacto

| Valor | Classificação | Critério |
| --- | --- | --- |
| 1 | Baixo | Causa pequeno transtorno e pode ser corrigido rapidamente. |
| 2 | Moderado | Causa interrupção ou inconsistência limitada, com possibilidade de recuperação. |
| 3 | Alto | Causa prejuízo relevante aos usuários, ao negócio, à administração ou à privacidade. |
| 4 | Muito alto | Pode afetar muitos usuários, comprometer operações críticas ou causar prejuízo grave. |

### 13.3 Cálculo e Classificação

**Pontuação = Probabilidade × Impacto**

| Pontuação | Nível do risco |
| --- | --- |
| 1 a 3 | Baixo |
| 4 a 7 | Médio |
| 8 a 11 | Alto |
| 12 a 16 | Crítico |

---

### 13.4 Registro de Riscos

Cada um dos 6 casos de abuso da Etapa 1 (um por categoria STRIDE) originou um risco correspondente.

| ID | Origem STRIDE | Evento de risco | Vulnerabilidade ou condição | Probabilidade | Impacto | Pontuação | Nível |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **R01** | Spoofing | Um atacante acessa a conta de um cliente usando credenciais vazadas de outros vazamentos (*credential stuffing*) e realiza compras em nome dele. | Ausência de limite de tentativas de login, captcha e autenticação multifator. | 3 | 4 | 12 | Crítico |
| **R02** | Tampering | Um atacante intercepta a requisição de checkout e altera o preço do produto antes do envio ao servidor. | O backend aceita o valor de preço enviado pelo cliente sem revalidar contra o banco de dados. | 3 | 4 | 12 | Crítico |
| **R03** | Repudiation | Um cliente nega ter realizado uma compra legítima para obter estorno indevido. | Ausência de logs auditáveis (IP, timestamp, dispositivo) vinculados de forma inequívoca à transação. | 3 | 3 | 9 | Alto |
| **R04** | Information Disclosure | Um atacante acessa dados pessoais de outros clientes manipulando o ID na URL do endpoint de perfil (IDOR). | Uso de identificadores sequenciais previsíveis sem verificação de posse do recurso. | 4 | 4 | 16 | Crítico |
| **R05** | Denial of Service | Um atacante sobrecarrega a rota de checkout com requisições massivas durante um período de alto tráfego. | Ausência de rate limiting, WAF ou proteção anti-DDoS na rota de checkout. | 2 | 4 | 8 | Alto |
| **R06** | Elevation of Privilege | Um cliente comum altera o campo "role" na requisição para obter acesso ao painel administrativo. | O backend confia no valor de "role" enviado pelo cliente em vez de validar via token assinado (JWT). | 3 | 4 | 12 | Crítico |

---

### 13.5 Justificativas

**R01 — Spoofing (Credential Stuffing)**
- **Probabilidade (3 — Média-alta):** ataques de *credential stuffing* são automatizados e amplamente disponíveis como serviço (listas de credenciais vazadas são vendidas ou distribuídas publicamente); a ausência de rate limiting e MFA no TechStore torna o ataque plausível em situações comuns de uso, embora dependa de reutilização de senha por parte do usuário.
- **Impacto (4 — Muito alto):** o comprometimento de uma conta expõe dados pessoais protegidos pela LGPD, permite compras fraudulentas com o método de pagamento salvo e pode afetar um grande número de clientes que reutilizam senhas.
- **Afetados:** clientes autenticados, dados pessoais, tokens de sessão, meios de pagamento.
- **Consequências:** fraude financeira, comprometimento de contas, dano à confiança na plataforma.

**R02 — Tampering (Adulteração de Preço)**
- **Probabilidade (3 — Média-alta):** a exploração exige apenas um proxy de interceptação (ferramenta gratuita e amplamente conhecida) e não requer acesso privilegiado, tornando o ataque plausível em qualquer sessão de compra.
- **Impacto (4 — Muito alto):** afeta diretamente a receita da empresa, pode ser repetido em escala e compromete a integridade dos registros financeiros e de estoque.
- **Afetados:** módulo de precificação, banco de dados de vendas, gateway de pagamento.
- **Consequências:** prejuízo financeiro direto, inconsistência contábil, possível abuso em massa antes da detecção.

**R03 — Repudiation (Ausência de Logs Auditáveis)**
- **Probabilidade (3 — Média-alta):** não exige conhecimento técnico avançado, apenas contato com o suporte alegando desconhecimento da compra; plausível em qualquer disputa de cobrança.
- **Impacto (3 — Alto):** gera prejuízo financeiro via estornos e cria fragilidade jurídica, mas está limitado a transações individuais, sem comprometer a plataforma como um todo.
- **Afetados:** histórico de pedidos, processo de suporte, relação com adquirente/gateway (chargebacks).
- **Consequências:** perda financeira recorrente, dificuldade de contestação jurídica, possível abuso sistemático por clientes mal-intencionados.

**R04 — Information Disclosure (IDOR)**
- **Probabilidade (4 — Alta):** a exploração é trivial — basta alterar um número na URL — e pode ser automatizada facilmente para varrer toda a base de usuários, sem necessidade de ferramentas sofisticadas.
- **Impacto (4 — Muito alto):** compromete a base completa de dados pessoais dos clientes (CPF, endereço, telefone, histórico de compras), configurando violação grave da LGPD com potencial afetação de todos os usuários cadastrados.
- **Afetados:** banco de dados relacional, API de perfil, todos os clientes cadastrados.
- **Consequências:** vazamento massivo de PII, sanções regulatórias, dano severo e duradouro à reputação.

**R05 — Denial of Service (Sobrecarga do Checkout)**
- **Probabilidade (2 — Média-baixa):** exige recursos técnicos um pouco maiores (botnet, ferramentas de flood ou serviços de ataque contratados), o que reduz a frequência esperada do evento em comparação com os demais, embora seja uma condição específica e conhecida (picos de tráfego como Black Friday).
- **Impacto (4 — Muito alto):** a indisponibilidade durante um período de pico comercial gera perda direta e imediata de receita, além de dano à reputação em um momento de alta visibilidade.
- **Afetados:** servidor backend, rota de checkout, todos os clientes tentando comprar no período.
- **Consequências:** queda de vendas, sobrecarga de suporte, possível perda de clientes para concorrentes.

**R06 — Elevation of Privilege (Manipulação de Role)**
- **Probabilidade (3 — Média-alta):** a exploração depende de uma falha específica de design (confiar em dado enviado pelo cliente), mas quando presente é de fácil execução via interceptação simples de requisição, tornando-a plausível em situações comuns de uso malicioso.
- **Impacto (4 — Muito alto):** concede controle total sobre o sistema — alteração de catálogo, exclusão de dados e acesso a informações de todos os clientes — representando o maior nível de comprometimento possível.
- **Afetados:** painel administrativo, catálogo de produtos, banco de dados completo.
- **Consequências:** comprometimento total do sistema, fraude em massa, exclusão ou corrupção de dados críticos.

---

### 13.6 Priorização

Ordem inicial de tratamento, da maior para a menor prioridade:

| Ordem | Risco | Pontuação | Justificativa da posição |
| --- | --- | --- | --- |
| 1º | **R04** — Information Disclosure (IDOR) | 16 | Maior pontuação do registro; afeta a totalidade da base de clientes de forma simultânea, com correção tecnicamente simples (validação de posse do recurso), tornando o não tratamento injustificável. |
| 2º | **R06** — Elevation of Privilege | 12 | Entre os riscos críticos de pontuação 12, é o que oferece o maior raio de dano possível, pois um único exploit concede controle irrestrito sobre todo o sistema, incluindo os demais ativos. |
| 3º | **R01** — Spoofing | 12 | Compromete diretamente contas de clientes e dados pessoais em escala, com potencial de automação (ataques em lote), exigindo tratamento prioritário mesmo com pontuação igual a R02. |
| 4º | **R02** — Tampering | 12 | Embora tenha a mesma pontuação de R01, seu impacto está mais concentrado no aspecto financeiro/contábil da empresa, sem comprometer diretamente dados pessoais de terceiros, o que o posiciona logo após os riscos de exposição de dados e identidade. |
| 5º | **R03** — Repudiation | 9 | Impacto relevante, mas limitado a disputas pontuais e reversível via processo administrativo/jurídico, sem risco sistêmico imediato à plataforma. |
| 6º | **R05** — Denial of Service | 8 | Menor probabilidade entre os riscos altos, por depender de maior capacidade técnica do atacante; ainda assim deve ser tratado antes de eventos sazonais de alto tráfego. |

A priorização considerou, além da pontuação bruta: a abrangência do dano (quantidade de usuários/dados afetados), a criticidade do ativo comprometido, a possibilidade de recuperação e as dependências entre os riscos (por exemplo, corrigir R06 reduz indiretamente o risco de exploração combinada com R04).
