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

---

### 13.5 Justificativas

**R01 — Spoofing (Credential Stuffing)**
- **Probabilidade (3):** ataques automatizados com senhas vazadas são fáceis de executar; falta rate limiting e MFA no sistema.
- **Impacto (4):** expõe dados pessoais e permite compras fraudulentas, podendo afetar muitos clientes que reutilizam senhas.
- **Afetados:** contas, dados pessoais, meios de pagamento.
- **Consequências:** fraude financeira e perda de confiança na plataforma.

**R02 — Tampering (Adulteração de Preço)**
- **Probabilidade (3):** basta interceptar a requisição do checkout, sem precisar de acesso privilegiado.
- **Impacto (4):** afeta diretamente a receita e a integridade dos registros de vendas.
- **Afetados:** precificação, banco de vendas, gateway de pagamento.
- **Consequências:** prejuízo financeiro direto e inconsistência contábil.

**R03 — Repudiation (Ausência de Logs Auditáveis)**
- **Probabilidade (3):** basta contatar o suporte negando a compra; não exige conhecimento técnico.
- **Impacto (3):** gera estornos indevidos, mas afeta transações pontuais, não a plataforma inteira.
- **Afetados:** histórico de pedidos, suporte, relação com o gateway.
- **Consequências:** perda financeira recorrente e fragilidade jurídica.

**R04 — Information Disclosure (IDOR)**
- **Probabilidade (4):** exploração trivial (trocar um número na URL) e fácil de automatizar em massa.
- **Impacto (4):** compromete os dados de toda a base de clientes, violando a LGPD.
- **Afetados:** banco de dados, API de perfil, todos os clientes.
- **Consequências:** vazamento massivo de dados e dano severo à reputação.

**R05 — Denial of Service (Sobrecarga do Checkout)**
- **Probabilidade (2):** exige mais recursos técnicos (botnet ou ferramentas de ataque), reduzindo a frequência esperada.
- **Impacto (4):** indisponibilidade em período de pico gera perda imediata de receita.
- **Afetados:** servidor backend, checkout, clientes tentando comprar.
- **Consequências:** queda de vendas e possível perda de clientes.

**R06 — Elevation of Privilege (Manipulação de Role)**
- **Probabilidade (3):** exige uma falha específica de design, mas é fácil de explorar quando presente.
- **Impacto (4):** dá controle total do sistema, o maior nível de comprometimento possível.
- **Afetados:** painel administrativo, catálogo, banco de dados completo.
- **Consequências:** comprometimento total do sistema e fraude em massa.

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
