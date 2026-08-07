# Considerações Finais

## Etapa 2: Análise, Priorização e Tratamento de Riscos com o NIST CSF

### Riscos considerados mais importantes

Os riscos mais críticos identificados foram **R04 (Information Disclosure via IDOR)**, **R06 (Elevation of Privilege)**, **R01 (Spoofing/Credential Stuffing)** e **R02 (Tampering no checkout)**, todos classificados como **Crítico**. Esses riscos compartilham uma característica em comum: decorrem de falhas de **confiança indevida em dados enviados pelo cliente** — seja o ID de um recurso, o papel de um usuário, a identidade de quem se autentica, ou o preço de um produto — em vez de validações realizadas de forma confiável no servidor.

### Razões que determinaram a priorização

A priorização considerou principalmente a **abrangência do dano** (quantos usuários/dados são afetados de uma só vez) e a **facilidade de exploração**. Por isso, R04 (IDOR) ficou em primeiro lugar: exige pouquíssimo esforço técnico do atacante e compromete a base de dados completa de clientes. Riscos com impacto mais concentrado ou que dependem de maior capacidade técnica do atacante (como R05, Denial of Service) foram posicionados em níveis de prioridade mais baixos, sem deixar de ser tratados.

### Estratégias de tratamento predominantes

Todos os seis riscos foram tratados com a estratégia de **Reduzir**, já que em nenhum caso foi identificada a necessidade de eliminar a funcionalidade afetada (login, checkout, consulta de perfil, painel administrativo) — em todos os casos, existem controles técnicos viáveis que corrigem a causa raiz sem comprometer a experiência legítima do usuário. O risco de Denial of Service (R05) também incorporou, de forma complementar, a estratégia de **Compartilhar**, por meio da contratação de um serviço terceirizado de proteção contra ataques distribuídos.

### Funções do NIST mais relevantes para o sistema

As funções **Protect** e **Detect** apareceram na maioria dos riscos, refletindo a necessidade prioritária de implementar salvaguardas técnicas (validações server-side, MFA, rate limiting) e de monitorar tentativas de exploração em tempo real. A função **Govern** foi menos recorrente, mas essencial nos riscos R03 e R06, onde a causa raiz está associada a decisões de design que deveriam ter sido definidas como política técnica desde o início (por exemplo, "o papel do usuário nunca é aceito vindo do cliente").

### Controles considerados essenciais

- Revalidação de preços e permissões **exclusivamente no backend**, nunca confiando em dados enviados pelo cliente.
- Verificação de posse de recursos (autorização) em todos os endpoints que retornam dados pessoais.
- Autenticação multifator e limitação de tentativas de login.
- Registro de logs auditáveis e imutáveis para toda transação financeira.
- Proteção de infraestrutura (rate limiting e WAF) contra picos de tráfego malicioso.

### Principais dificuldades encontradas

A principal dificuldade foi **diferenciar risco de ameaça e de ataque** ao transformar os itens da Etapa 1 em eventos de risco mensuráveis, além de justificar de forma consistente valores de probabilidade e impacto que refletissem o contexto real do TechStore, e não apenas uma escolha arbitrária. Também houve dificuldade em decidir o nível de granularidade dos controles propostos, evitando recomendações genéricas.

### Limitações da avaliação

Esta avaliação é uma **estimativa qualitativa**, baseada na arquitetura descrita na Etapa 1 e em cenários hipotéticos de exploração, sem dados reais de produção (como volume real de tentativas de login ou tráfego histórico de checkout). Os valores de probabilidade e impacto poderão ser recalibrados à medida que o sistema for implementado e monitorado. Além disso, os controles propostos ainda não foram implementados nesta etapa, portanto os níveis de risco residual são projeções, não resultados confirmados.

### Pontos que precisarão ser detalhados nas próximas etapas

- Implementação efetiva dos controles propostos e execução dos testes de verificação indicados no plano de tratamento.
- Definição detalhada da política de retenção de logs (prazo, formato, local de armazenamento) para o controle de R03.
- Escolha e configuração específica do serviço de WAF/anti-DDoS para R05, incluindo custos e SLAs.
- Validação formal (por exemplo, via testes de penetração) para confirmar os níveis de risco residual estimados.
- Revisão periódica da priorização à medida que novos dados de uso real do sistema estiverem disponíveis.
