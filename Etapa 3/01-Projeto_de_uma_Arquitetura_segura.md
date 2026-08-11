# Etapa 3 — Projeto de uma Arquitetura Segura

## Objetivo

O objetivo desta etapa é transformar os riscos e controles definidos anteriormente em requisitos de segurança e decisões de arquitetura.

A proposta apresentada considera os principais riscos identificados na Etapa 2, especialmente os riscos relacionados à exposição indevida de informações, alteração de dados, elevação de privilégios, comprometimento de contas e indisponibilidade do serviço.

## Requisitos de Segurança

Com base nos riscos considerados prioritários na Etapa 2, foram definidos três requisitos de segurança específicos e verificáveis.

| ID       | Risco de origem                     | Requisito de segurança                                                                                                                                                                             | Critério de verificação                                                                                                             |
| -------- | ----------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| **RS01** | R04 — Information Disclosure (IDOR) | O sistema deverá verificar, no backend, se o usuário autenticado possui autorização para acessar o recurso solicitado antes de retornar dados pessoais ou informações de pedidos.                  | Uma requisição autenticada tentando acessar dados pertencentes a outro usuário deverá ser recusada pelo servidor.                   |
| **RS02** | R06 — Elevation of Privilege        | O sistema deverá determinar as permissões do usuário exclusivamente a partir de informações confiáveis mantidas e validadas no servidor, não aceitando alterações de função enviadas pelo cliente. | Uma requisição contendo `"role": "admin"` enviada por um usuário comum deverá continuar sendo tratada com as permissões de cliente. |
| **RS03** | R02 — Tampering                     | O sistema deverá revalidar no backend o preço e demais valores utilizados no checkout antes de autorizar o pagamento.                                                                              | Uma tentativa de enviar um preço diferente do valor registrado no banco de dados deverá ser recusada pelo backend.                  |

Os requisitos foram escolhidos a partir dos riscos mais relevantes identificados na Etapa 2. O R04 foi priorizado por apresentar a maior pontuação, enquanto R06 e R02 representam riscos críticos relacionados à autorização e à integridade financeira da plataforma.

## Vulnerabilidades Catalogadas

Os requisitos de segurança foram relacionados a vulnerabilidades e categorias reconhecidas em referências de segurança de software.

| Risco                            | Vulnerabilidade ou categoria                                  | Referência utilizada | Relação com o sistema                                                                                                                                                                         |
| -------------------------------- | ------------------------------------------------------------- | -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **R04 — Information Disclosure** | CWE-639 — Authorization Bypass Through User-Controlled Key    | CWE                  | Está relacionada ao cenário de IDOR, no qual um usuário consegue utilizar um identificador controlado por ele para acessar recursos pertencentes a outra conta.                               |
| **R06 — Elevation of Privilege** | CWE-602 — Client-Side Enforcement of Server-Side Security     | CWE                  | Relaciona-se à situação em que uma regra de segurança ou autorização é aplicada somente no cliente, permitindo que um atacante manipule informações que deveriam ser validadas pelo servidor. |
| **R02 — Tampering**              | CWE-472 — External Control of Assumed-Immutable Web Parameter | CWE                  | Relaciona-se à situação em que parâmetros enviados pelo usuário são tratados como confiáveis ou imutáveis, permitindo a manipulação de valores utilizados pelo sistema.                       |

Essas vulnerabilidades foram selecionadas porque representam diretamente as condições identificadas nos riscos R04, R06 e R02 da Etapa 2.

O uso dessas referências também permite relacionar os riscos identificados pelo grupo com categorias reconhecidas de vulnerabilidades de software.

## Decisões de Arquitetura

As decisões de arquitetura foram definidas considerando os riscos prioritários e os controles propostos anteriormente.

## DA01 — Autorização obrigatória no backend

| Item | Descrição |
| --- | --- |
| **Risco tratado** | R04 — Information Disclosure (IDOR) |
| **Decisão** | Toda solicitação de acesso a informações pessoais, pedidos ou outros recursos pertencentes a usuários deve passar por uma verificação de autorização no backend. |
| **Motivo** | A interface do sistema não deve ser considerada um mecanismo de segurança. Mesmo que o usuário consiga alterar um identificador na URL ou na requisição, o servidor deve verificar se aquele recurso realmente pertence ao usuário autenticado. |
| **Componente afetado** | API de backend e camada responsável pela autorização das requisições. |
| **Resultado esperado** | Impedir que um usuário autenticado consiga acessar dados pertencentes a outras contas por meio da manipulação de identificadores. |

## DA02 — Controle de privilégios determinado no servidor

| Item | Descrição |
| --- | --- |
| **Risco tratado** | R06 — Elevation of Privilege |
| **Decisão** | As permissões do usuário devem ser determinadas por informações confiáveis, mantidas e validadas no servidor. Valores de `role` enviados diretamente pelo cliente não devem ser utilizados para conceder privilégios. |
| **Motivo** | Informações recebidas do cliente podem ser manipuladas. Alterar `"role": "user"` para `"role": "admin"` em uma requisição não pode resultar na concessão de privilégios administrativos. |
| **Componente afetado** | Middleware de autenticação e autorização do backend, além das rotas administrativas. |
| **Resultado esperado** | Impedir que clientes comuns obtenham privilégios administrativos por meio da manipulação das requisições. |

## DA03 — Revalidação de valores no servidor

| Item | Descrição |
| --- | --- |
| **Risco tratado** | R02 — Tampering |
| **Decisão** | O backend deve obter e validar os preços dos produtos a partir de uma fonte confiável antes de calcular o valor final da compra e encaminhá-lo ao gateway de pagamento. |
| **Motivo** | O preço apresentado ou enviado pela interface do cliente não deve ser considerado confiável. O cliente pode modificar os parâmetros da requisição, mas essa alteração não pode modificar o valor real da compra. |
| **Componente afetado** | Serviço de checkout, regras de negócio, banco de dados de produtos e integração com o gateway de pagamento. |
| **Resultado esperado** | Impedir que um cliente altere artificialmente o preço de um produto ou o valor total da compra antes da autorização do pagamento. |

## Relação com os Riscos e Controles Anteriores

As decisões apresentadas nesta etapa dão continuidade direta ao tratamento definido na Etapa 2.

| Risco                            | Controle definido na Etapa 2                                                                 | Requisito / Decisão relacionada na Etapa 3 |
| -------------------------------- | -------------------------------------------------------------------------------------------- | ------------------------------------------ |
| **R04 — IDOR**                   | Verificação de posse do recurso em endpoints que retornam dados de usuários.                 | RS01 / DA01                                |
| **R06 — Elevação de privilégio** | Validação do papel do usuário exclusivamente a partir de informações confiáveis no servidor. | RS02 / DA02                                |
| **R02 — Alteração de preço**     | Revalidação obrigatória do preço no backend antes do pagamento.                              | RS03 / DA03                                |
