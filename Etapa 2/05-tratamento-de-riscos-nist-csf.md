# Tratamento de Riscos com o NIST CSF 2.0

## Etapa 2: Análise, Priorização e Tratamento de Riscos com o NIST CSF

### 14.1 Estratégias de Tratamento

| Risco | Estratégia | Justificativa |
| --- | --- | --- |
| **R01** — Spoofing | Reduzir | Não é possível eliminar totalmente a tentativa de login malicioso (a funcionalidade de login é essencial ao sistema); a estratégia foca em reduzir a probabilidade (rate limiting, MFA) e o impacto (detecção e bloqueio rápido de contas comprometidas). |
| **R02** — Tampering | Reduzir | A validação de preço é uma correção técnica direta que reduz a probabilidade do evento a praticamente zero, sem necessidade de eliminar a funcionalidade de checkout. |
| **R03** — Repudiation | Reduzir | A implementação de logs auditáveis reduz a capacidade de repúdio sem alterar o fluxo de compra existente. |
| **R04** — Information Disclosure | Reduzir | A correção do controle de autorização (IDOR) elimina a vulnerabilidade específica sem exigir a remoção da funcionalidade de consulta de perfil. |
| **R05** — Denial of Service | Reduzir | Combina controles próprios (rate limiting) com um serviço terceirizado de proteção (WAF/anti-DDoS), reduzindo tanto a probabilidade quanto o impacto do evento. |
| **R06** — Elevation of Privilege | Reduzir | A correção do critério de autorização (validar o papel apenas via token assinado no servidor) elimina o vetor de exploração identificado, sem necessidade de restringir funcionalidades legítimas do sistema. |

Nenhum dos riscos identificados foi classificado como **Aceitar**, dado que todos possuem nível Alto ou Crítico e possuem controles técnicos viáveis de implementação. A estratégia **Compartilhar** aparece de forma complementar apenas no R05, por meio do uso de um provedor terceirizado de proteção contra DDoS.

---

### 14.2 Funções do NIST CSF 2.0

| Função | Finalidade |
| --- | --- |
| **Govern** | Definir políticas, responsabilidades, prioridades e critérios de decisão. |
| **Identify** | Conhecer ativos, dependências, vulnerabilidades e riscos. |
| **Protect** | Implementar salvaguardas para reduzir a probabilidade ou o impacto. |
| **Detect** | Identificar eventos suspeitos, falhas e possíveis incidentes. |
| **Respond** | Conter, analisar, comunicar e tratar incidentes. |
| **Recover** | Restaurar serviços e dados e reduzir os prejuízos causados. |

---

### 14.3 Mapeamento dos Riscos para o NIST CSF

| Risco | Govern | Identify | Protect | Detect | Respond | Recover |
| --- | :---: | :---: | :---: | :---: | :---: | :---: |
| **R01** — Spoofing |  |  | X | X | X |  |
| **R02** — Tampering |  | X | X | X |  |  |
| **R03** — Repudiation | X |  |  | X | X |  |
| **R04** — Information Disclosure |  | X | X | X | X |  |
| **R05** — Denial of Service |  |  | X | X | X | X |
| **R06** — Elevation of Privilege | X | X | X | X |  |  |

**Justificativas do mapeamento:**
- **R01:** *Protect* (MFA e rate limiting), *Detect* (monitoramento de tentativas de login anômalas), *Respond* (bloqueio automático de conta suspeita).
- **R02:** *Identify* (mapeamento dos pontos de confiança do backend), *Protect* (revalidação server-side), *Detect* (alertas de divergência de preço).
- **R03:** *Govern* (política de retenção e formato dos logs), *Detect* (registro de eventos), *Respond* (processo de análise de disputas com base em evidências).
- **R04:** *Identify* (mapeamento de endpoints que expõem dados sensíveis), *Protect* (controle de autorização), *Detect* (alertas de varredura de IDs), *Respond* (bloqueio de IP em caso de enumeração).
- **R05:** *Protect* (rate limiting e WAF), *Detect* (monitoramento de tráfego anômalo), *Respond* (mitigação ativa), *Recover* (restabelecimento rápido do serviço).
- **R06:** *Govern* (política de que papéis nunca são aceitos do cliente), *Identify* (mapeamento das rotas administrativas), *Protect* (validação via JWT assinado), *Detect* (log de tentativas de acesso indevido ao painel).

Optou-se por **não marcar todas as funções em todos os riscos**, mantendo apenas as relações efetivamente aplicáveis a cada caso.

---

### 14.4 Plano de Tratamento

| Risco | Estratégia | Controles propostos | Funções relacionadas | Responsáveis | Evidências e verificação |
| --- | --- | --- | --- | --- | --- |
| **R01** | Reduzir | Autenticação multifator (TOTP) no login; bloqueio/captcha após 5 tentativas falhas; alerta de login em novo dispositivo/localização. | Protect, Detect, Respond | Equipe de Backend / Segurança | Testes automatizados de bloqueio após tentativas falhas; simulação de *credential stuffing* em ambiente de teste; logs de alertas gerados. |
| **R02** | Reduzir | Revalidação obrigatória do preço do produto no backend, consultando o banco de dados antes de autorizar o pagamento; rejeição automática de divergências. | Identify, Protect, Detect | Equipe de Backend | Teste de integração enviando preço divergente e verificando rejeição; log de tentativas de divergência de valor. |
| **R03** | Reduzir | Registro estruturado e imutável de cada transação (ID do usuário, IP, timestamp, dispositivo, status); retenção mínima definida em política interna. | Govern, Detect, Respond | Equipe de Backend / Infraestrutura | Amostragem periódica de logs de auditoria; procedimento documentado de consulta em caso de contestação de compra. |
| **R04** | Reduzir | Verificação de posse do recurso em todo endpoint que retorna dados de usuário (comparar ID do token com ID solicitado); migração de identificadores sequenciais para UUID em endpoints sensíveis. | Identify, Protect, Detect, Respond | Equipe de Backend / Segurança | Teste de segurança (pentest interno) tentando acessar perfil de outro ID; revisão de código dos endpoints de perfil. |
| **R05** | Reduzir / Compartilhar | Rate limiting na rota de checkout; contratação de serviço de WAF / proteção anti-DDoS terceirizado; configuração de auto-scaling no backend. | Protect, Detect, Respond, Recover | Equipe de Infraestrutura / DevOps | Teste de carga simulando pico de tráfego; relatório de disponibilidade do serviço durante o teste; métricas do WAF. |
| **R06** | Reduzir | Validação do papel (role) do usuário exclusivamente a partir de claims assinadas no token JWT emitido pelo servidor, ignorando qualquer valor de "role" enviado no corpo da requisição. | Govern, Identify, Protect, Detect | Equipe de Backend / Segurança | Teste automatizado tentando forjar "role": "admin" na requisição; revisão de código do middleware de autorização; log de tentativas negadas. |

---

### 14.5 Ordem Inicial de Implementação

| Ordem | Risco | Justificativa |
| --- | --- | --- |
| 1º | **R04** — IDOR | Correção de baixa complexidade técnica (validação de posse do recurso) com o maior impacto de redução de risco; deve ser tratada imediatamente por afetar toda a base de clientes. |
| 2º | **R02** — Tampering | Ajuste pontual no fluxo de checkout (revalidação de preço no backend); rápido de implementar e elimina uma fraude financeira direta. |
| 3º | **R06** — Elevation of Privilege | Depende de uma mudança já necessária na forma como o backend trata autenticação (uso do JWT), reaproveitando parte do trabalho de R01; deve ser feita logo após as correções mais simples. |
| 4º | **R01** — Spoofing | Requer mudanças de maior porte (MFA, fluxo de autenticação), incluindo decisões de UX; depende parcialmente da infraestrutura de autenticação usada por R06. |
| 5º | **R03** — Repudiation | Exige a criação de uma infraestrutura de logging estruturado, o que demanda mais planejamento de armazenamento e política de retenção. |
| 6º | **R05** — Denial of Service | Envolve maior custo e complexidade (contratação de serviço terceirizado, testes de carga), sendo o item de maior prazo de implementação; deve estar concluído antes de eventos sazonais de alto tráfego. |

A ordem prioriza correções de **baixo custo e alto impacto** primeiro (R04, R02), seguidas por controles que compartilham infraestrutura (R06, R01) e, por fim, os controles de maior complexidade e custo (R03, R05).

---

### 14.6 Estimativa do Risco Residual

| Risco | Nível inicial | Nível residual esperado | Condição para aceitar o residual |
| --- | --- | --- | --- |
| **R01** — Spoofing | Crítico (12) | Baixo (3) — Probabilidade 1 × Impacto 3 | MFA obrigatório ativo para todas as contas e monitoramento de login anômalo em produção, validado por simulação de ataque. |
| **R02** — Tampering | Crítico (12) | Baixo (2) — Probabilidade 1 × Impacto 2 | Testes automatizados de regressão confirmando rejeição de preços divergentes em todo *deploy*. |
| **R03** — Repudiation | Alto (9) | Baixo (2) — Probabilidade 1 × Impacto 2 | Logs auditáveis implementados, testados e com processo de consulta documentado e utilizado pelo suporte. |
| **R04** — Information Disclosure | Crítico (16) | Baixo (2) — Probabilidade 1 × Impacto 2 | Testes de penetração periódicos confirmando ausência de IDOR em todos os endpoints que retornam dados de usuário. |
| **R05** — Denial of Service | Alto (8) | Médio (4) — Probabilidade 2 × Impacto 2 | WAF/anti-DDoS ativo e testado; ainda assim mantém probabilidade residual pela natureza distribuída e difícil de eliminar totalmente do ataque. |
| **R06** — Elevation of Privilege | Crítico (12) | Baixo (2) — Probabilidade 1 × Impacto 2 | Testes de penetração periódicos nas rotas administrativas confirmando que nenhum valor enviado pelo cliente altera o papel do usuário. |

O risco residual é apresentado como **estimativa**. Nenhuma redução será considerada efetiva até que os controles propostos sejam implementados, testados e validados por evidências concretas (testes automatizados, pentests e logs de produção).
