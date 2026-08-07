# Considerações Finais

## Etapa 2: Análise, Priorização e Tratamento de Riscos com o NIST CSF

### Resumo dos Riscos

| Risco | Nível inicial | Estratégia principal | Justificativa |
| --- | --- | --- | --- |
| **R01** — Spoofing | Crítico | Reduzir | Login sem MFA facilita invasão de contas com senhas vazadas. |
| **R02** — Tampering | Crítico | Reduzir | Preço deve ser validado no backend, não confiar no cliente. |
| **R03** — Repudiation | Alto | Reduzir | Logs auditáveis reduzem o risco, mas não eliminam disputas. |
| **R04** — Information Disclosure | Crítico | Reduzir | Dados pessoais precisam de controle de acesso rigoroso por usuário. |
| **R05** — Denial of Service | Alto | Reduzir e Compartilhar | Rate limiting próprio somado a proteção terceirizada (WAF). |
| **R06** — Elevation of Privilege | Crítico | Reduzir | Papel do usuário deve vir só de token assinado, nunca do cliente. |

### Riscos mais importantes

Os riscos **R04, R06, R01 e R02** foram os mais críticos. Todos têm a mesma causa: o sistema confia em dados enviados pelo cliente (ID, papel, senha ou preço) em vez de validar tudo no servidor.

### Priorização

A ordem levou em conta principalmente **quantos usuários são afetados** e **quão fácil é explorar a falha**. Por isso R04 (IDOR) ficou em primeiro lugar, e riscos que exigem mais capacidade técnica do atacante, como R05, ficaram por último.

### Estratégias e NIST CSF

Todos os riscos foram tratados com **Reduzir**, pois existem controles técnicos que corrigem a causa sem remover funcionalidades. R05 também usa **Compartilhar**, com um serviço terceirizado de proteção contra DDoS. As funções **Protect** e **Detect** foram as mais usadas, por priorizarem prevenção e monitoramento; **Govern** apareceu nos riscos ligados a decisões de design mal definidas (R03 e R06).

### Controles essenciais

- Validar preços, permissões e papéis **sempre no backend**.
- Checar se o dado pedido pertence ao usuário logado.
- Autenticação multifator e limite de tentativas de login.
- Logs auditáveis para toda transação.
- Proteção contra picos de tráfego (rate limiting e WAF).

