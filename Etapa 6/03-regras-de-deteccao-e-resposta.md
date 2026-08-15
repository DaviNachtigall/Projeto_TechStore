# Regras de Detecção e Resposta

## Etapa 6: Monitoramento e Detecção de Intrusões

### Regras de detecção

Foram selecionados três riscos para representar regras simples de detecção. A escolha considera riscos que possuem comportamentos relativamente claros e observáveis por meio dos registros do sistema.

As três regras selecionadas são:

* **R01 — Spoofing / Credential Stuffing**;
* **R04 — Information Disclosure / IDOR**;
* **R06 — Elevation of Privilege**.

Esses riscos também possuem relação direta com os casos de abuso CA02, CA04 e CA06 definidos na Etapa 1.

---

#### A01 — Detecção de tentativas excessivas de autenticação

| Campo                  | Descrição                                                                                                                                                                       |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Risco observado**    | **R01 — Spoofing**, relacionado ao **CA02 — Acesso Não Autorizado via Credenciais Vazadas (Credential Stuffing)**                                                               |
| **Fonte de dados**     | Logs de autenticação e tentativas de login                                                                                                                                      |
| **Condição de alerta** | O mesmo usuário ou origem apresenta muitas tentativas de login malsucedidas em um curto período, especialmente quando as tentativas ocorrem de forma repetitiva ou automatizada |
| **Resposta inicial**   | Gerar um alerta, registrar o evento e aplicar temporariamente mecanismos de limitação ou bloqueio das novas tentativas, conforme a política de segurança                        |

A regra procura identificar o comportamento característico de ataques automatizados de força bruta ou credential stuffing.

O objetivo não é considerar uma única falha de login como uma intrusão. Uma pessoa pode digitar sua senha incorretamente ocasionalmente. O alerta deve estar relacionado a um **padrão de comportamento**, como uma quantidade anormal de tentativas em um período curto.

Essa regra complementa os controles definidos para o R01 na Etapa 2, especialmente **MFA, limitação de tentativas e monitoramento de logins anômalos**.

---

#### A02 — Detecção de possível enumeração de perfis

| Campo                  | Descrição                                                                                                                                                                                                                                |
| ---------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Risco observado**    | **R04 — Information Disclosure**, relacionado ao **CA04 — Extração de Dados de Clientes via Enumeração de IDs (IDOR)**                                                                                                                   |
| **Fonte de dados**     | Logs das requisições aos endpoints de perfil e registros de autorização                                                                                                                                                                  |
| **Condição de alerta** | Um mesmo usuário ou origem realiza sucessivas requisições para identificadores de perfis diferentes, principalmente quando várias requisições resultam em acesso negado ou quando os identificadores são percorridos de forma sequencial |
| **Resposta inicial**   | Registrar o comportamento, gerar um alerta para a equipe responsável e, caso o comportamento persista, limitar temporariamente as requisições da origem e investigar a tentativa de enumeração                                           |

A regra procura identificar um comportamento compatível com a tentativa descrita no CA04, na qual um atacante modifica sucessivamente o identificador de um perfil para tentar obter informações de outros clientes.

A existência de uma requisição isolada para um perfil que não pertence ao usuário não é necessariamente suficiente para determinar uma intrusão. Por isso, a regra considera especialmente importante a **repetição e o padrão das requisições**.

A regra complementa os controles definidos para o R04 na Etapa 2, como a verificação de posse do recurso e o monitoramento de tentativas de enumeração.

---

#### A03 — Detecção de tentativas de acesso administrativo indevido

| Campo                  | Descrição                                                                                                                                                                                                   |
| ---------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Risco observado**    | **R06 — Elevation of Privilege**, relacionado ao **CA06 — Elevação de Privilégio via Manipulação do Parâmetro de Papel (Role)**                                                                             |
| **Fonte de dados**     | Logs de autenticação, autorização e acesso às rotas administrativas                                                                                                                                         |
| **Condição de alerta** | Um usuário sem privilégios administrativos tenta acessar repetidamente endpoints ou funcionalidades administrativas, ou apresenta tentativas de alterar informações relacionadas ao seu nível de privilégio |
| **Resposta inicial**   | Negar a requisição, registrar a tentativa, gerar um alerta e encaminhar o evento para investigação caso o comportamento seja repetido ou considerado anormal                                                |

Essa regra procura identificar tentativas de obtenção indevida de privilégios administrativos.

O sistema não deve considerar como válido um nível de privilégio simplesmente porque ele foi informado pelo cliente. Conforme definido na Etapa 2, a autorização deve ser determinada por informações confiáveis mantidas e verificadas pelo servidor.

A regra de detecção complementa o controle preventivo: mesmo que a tentativa de elevação seja corretamente bloqueada, o comportamento pode ser registrado e gerar um alerta para indicar uma possível tentativa de ataque.

---

### Resposta após um alerta

A geração de um alerta não significa automaticamente que ocorreu um ataque bem-sucedido. O alerta representa um comportamento que deve ser analisado.

Após a detecção de um evento suspeito, a regra de detecção gera um alerta e as evidências relacionadas ao evento são registradas. Em seguida, o evento é analisado para determinar se realmente se trata de um incidente de segurança. 

Caso necessário, são realizadas ações de contenção para limitar seus efeitos. Depois disso, o evento pode ser investigado de forma mais detalhada, permitindo identificar sua causa e extensão. 

Por fim, são aplicadas as medidas de correção ou mitigação necessárias e o resultado de todo o processo é documentado para fins de acompanhamento e futuras análises.

#### 1. Registrar o evento

As informações relevantes devem ser preservadas para permitir a análise posterior. Entre elas estão o usuário envolvido, horário, origem da requisição, recurso acessado, resultado da operação e demais informações disponíveis no registro.

#### 2. Analisar o alerta

A equipe responsável deve verificar se o comportamento pode ser explicado por uma atividade legítima ou se existem indícios de tentativa de ataque.

Essa etapa é importante para evitar que todos os alertas sejam tratados automaticamente como incidentes.

#### 3. Conter o comportamento, quando necessário

Caso exista evidência suficiente de atividade maliciosa, podem ser aplicadas medidas como:

* limitação temporária de requisições;
* bloqueio temporário;
* encerramento de sessão;
* impedimento de novas tentativas;
* restrição da origem do tráfego;
* suspensão temporária de uma conta comprometida.

A medida utilizada deve ser compatível com a natureza e a gravidade do evento.

#### 4. Investigar

A equipe deve verificar:

* quais recursos foram acessados;
* quais ações foram realizadas;
* se houve tentativa de exploração;
* se alguma ação foi bem-sucedida;
* quais usuários ou dados podem ter sido afetados;
* se outros eventos relacionados ocorreram anteriormente.

#### 5. Corrigir ou mitigar

Após a investigação, devem ser aplicadas as correções necessárias. Isso pode incluir alteração de configurações, correção de código, reforço de controles de acesso ou atualização das regras de monitoramento.

#### 6. Documentar o resultado

O incidente ou falso positivo deve ser registrado para permitir a análise posterior e o aprimoramento das regras de segurança.

---

### Relação com os riscos identificados

As regras propostas estão diretamente relacionadas aos riscos priorizados na Etapa 2:

| Regra   | Risco                        | Caso de abuso              | Comportamento observado                        |
| ------- | ---------------------------- | -------------------------- | ---------------------------------------------- |
| **A01** | R01 — Spoofing               | CA02 — Credential Stuffing | Muitas tentativas de autenticação malsucedidas |
| **A02** | R04 — Information Disclosure | CA04 — IDOR                | Requisições repetidas para diferentes perfis   |
| **A03** | R06 — Elevation of Privilege | CA06 — Manipulação de Role | Tentativas de acesso a funções administrativas |

Essas regras não substituem os controles preventivos definidos anteriormente. Elas atuam como uma camada complementar de monitoramento.

Por exemplo, no R01, o MFA e a limitação de tentativas são mecanismos de proteção, enquanto o registro de tentativas malsucedidas e a geração de alertas são mecanismos de detecção.

Da mesma forma, no R06, o controle de autorização impede que um usuário comum obtenha privilégios administrativos, enquanto o registro de tentativas de acesso administrativo pode permitir a identificação de um possível ataque.

---

### Limitações

As regras apresentadas são propostas conceituais, não um sistema de detecção implementado. Os limiares de alerta (quantidade de tentativas, intervalo de tempo) precisariam ser ajustados com base no comportamento real da aplicação.

Alguns comportamentos legítimos podem se assemelhar a maliciosos (ex: várias tentativas de login por senha esquecida). Por isso, um alerta não comprova um ataque por si só e deve ser analisado antes de virar incidente.

Por ter caráter conceitual, esta etapa não usou ferramentas de verificação como o ZAP nem logs reais do TechStore — as regras foram derivadas apenas dos riscos e casos de abuso das Etapas 1 e 2.

---

### Conclusão

A detecção de intrusões complementa os mecanismos de prevenção ao permitir que comportamentos suspeitos sejam identificados depois que o sistema entra em operação.

Para o TechStore, os registros de autenticação, acesso a dados, operações administrativas, transações e requisições são importantes para identificar sinais relacionados aos riscos levantados na análise STRIDE.

As três regras propostas demonstram como os riscos podem ser transformados em comportamentos observáveis:

1. **R01 — Spoofing:** identificar tentativas excessivas de autenticação;
2. **R04 — Information Disclosure:** identificar possível enumeração de perfis;
3. **R06 — Elevation of Privilege:** identificar tentativas de acesso administrativo indevido.

A detecção deve estar associada a uma resposta adequada: registrar o evento, analisar o alerta, conter o comportamento quando necessário, investigar a ocorrência e documentar o resultado.

Dessa forma, a Etapa 6 complementa as etapas anteriores do projeto ao transformar parte dos riscos identificados em **regras práticas de monitoramento e resposta**.
