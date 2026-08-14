# Eventos e Fontes de Monitoramento

## Etapa 6: Monitoramento e Detecção de Intrusões

### Eventos que deveriam ser registrados

Para que as regras de detecção possam funcionar, o sistema deve possuir registros suficientes para identificar comportamentos anormais. Os eventos devem estar relacionados aos riscos identificados na Etapa 2.

#### Eventos de autenticação

Deveriam ser registrados:

* tentativas de login;
* resultado da tentativa de login, indicando sucesso ou falha;
* conta utilizada;
* data e horário da tentativa;
* origem da requisição, quando disponível;
* ocorrência de várias tentativas em sequência;
* bloqueios ou limitações aplicados à conta.

Esses registros são principalmente relevantes para o **R01 — Spoofing**, relacionado ao caso **CA02 — Acesso Não Autorizado via Credenciais Vazadas (Credential Stuffing)**.

---

#### Eventos de acesso a perfis e dados

Deveriam ser registrados:

* usuário responsável pela requisição;
* recurso ou endpoint acessado;
* identificador do recurso solicitado;
* resultado da autorização;
* acessos permitidos;
* tentativas de acesso negadas;
* sequência de requisições realizadas pelo mesmo usuário ou origem.

Esses eventos são relevantes para o **R04 — Information Disclosure**, relacionado ao **CA04 — Extração de Dados de Clientes via Enumeração de IDs (IDOR)**.

---

#### Eventos relacionados a operações administrativas

Deveriam ser registrados:

* usuário que realizou a operação;
* endpoint administrativo acessado;
* data e horário;
* resultado da autorização;
* tentativas de acesso administrativo negadas;
* alterações realizadas em produtos ou outras informações administrativas;
* tentativas de modificar informações relacionadas ao nível de privilégio do usuário.

Esses eventos estão relacionados principalmente ao **R06 — Elevation of Privilege**, associado ao **CA06 — Elevação de Privilégio via Manipulação do Parâmetro de Papel (Role)**.

---

#### Eventos relacionados ao checkout e às transações

Deveriam ser registrados:

* identificação do usuário;
* identificação do pedido;
* produtos envolvidos;
* valores utilizados no processamento;
* resultado do checkout;
* data e horário;
* alterações ou divergências detectadas durante o processamento.

Esses registros são relevantes principalmente para o **R02 — Tampering**, relacionado ao **CA01 — Alteração Maliciosa de Valor no Checkout**, e para o **R03 — Repudiation**, relacionado ao **CA03 — Repúdio de Transação por Ausência de Logs Auditáveis**.

---

#### Eventos relacionados à disponibilidade

Também deveriam ser acompanhados:

* quantidade de requisições recebidas;
* quantidade de requisições por origem;
* taxa de requisições por período;
* erros e timeouts;
* utilização de recursos durante períodos de tráfego elevado;
* respostas de bloqueio ou limitação de requisições.

Esses eventos são relevantes para o **R05 — Denial of Service**, relacionado ao **CA05 — Indisponibilidade do Checkout em Período de Alto Tráfego**.