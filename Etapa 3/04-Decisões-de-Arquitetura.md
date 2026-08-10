# Decisões de Arquitetura

As decisões abaixo foram definidas com base nos três riscos críticos selecionados na Etapa 2: **R04 — Information Disclosure**, **R06 — Elevation of Privilege** e **R02 — Tampering**.

## DA01 — Autorização de recursos realizada no backend

| Item                   | Descrição                                                                                                                                                                                                                                                |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Risco tratado**      | **R04 — Information Disclosure (IDOR)**                                                                                                                                                                                                                  |
| **Problema**           | Um usuário poderia modificar um identificador de perfil ou pedido e acessar dados pertencentes a outro cliente.                                                                                                                                          |
| **Decisão tomada**     | Toda requisição que acessar um recurso pertencente a um usuário deverá passar por uma verificação de autorização no backend. O servidor deverá verificar se o usuário autenticado possui direito de acessar aquele recurso antes de retornar seus dados. |
| **Componente afetado** | Backend / API e banco de dados.                                                                                                                                                                                                                          |
| **Resultado esperado** | Impedir o acesso horizontal entre contas e reduzir o risco de exposição de dados pessoais.                                                                                                                                                               |

A interface não deverá ser considerada responsável pela proteção do recurso. Mesmo que o usuário consiga modificar manualmente uma URL, parâmetro ou identificador, o backend deverá realizar novamente a verificação de autorização.

Por exemplo, se um usuário autenticado tentar acessar um recurso pertencente a outro cliente, o backend deverá verificar a relação entre o usuário autenticado e o recurso solicitado antes de retornar qualquer informação.

## DA02 — Separação entre autenticação e autorização administrativa

| Item                   | Descrição                                                                                                                                                                                                                                                                                                                                                                          |
| ---------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Risco tratado**      | **R06 — Elevation of Privilege**                                                                                                                                                                                                                                                                                                                                                   |
| **Problema**           | Um cliente poderia tentar alterar informações como `role=user` para `role=admin` e obter acesso ao painel administrativo.                                                                                                                                                                                                                                                          |
| **Decisão tomada**     | O backend deverá ignorar informações de privilégio fornecidas diretamente pelo cliente e realizar a autorização no servidor. O papel do usuário deverá ser obtido de uma fonte confiável, como o registro da conta no banco de dados ou uma informação de identidade emitida pelo próprio servidor. As rotas administrativas deverão possuir verificações de autorização próprias. |
| **Componente afetado** | Serviço de autenticação, serviço de autorização e endpoints administrativos.                                                                                                                                                                                                                                                                                                       |
| **Resultado esperado** | Impedir que um usuário comum consiga alterar seu próprio nível de privilégio e acessar funções administrativas.                                                                                                                                                                                                                                                                    |

O JWT poderá ser utilizado para representar uma sessão autenticada, mas a simples existência de um JWT assinado não substitui a autorização.

O servidor deverá garantir que as informações utilizadas para determinar o privilégio sejam confiáveis e que cada operação administrativa seja autorizada no backend.

Essa separação é importante porque autenticação responde à pergunta:

> **“Quem é o usuário?”**

Enquanto autorização responde à pergunta:

> **“O que esse usuário pode fazer?”**

## DA03 — Fonte de verdade do preço mantida no servidor

| Item                   | Descrição                                                                                                                                                                                                                                                                                |
| ---------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Risco tratado**      | **R02 — Tampering**                                                                                                                                                                                                                                                                      |
| **Problema**           | Um atacante poderia modificar o preço enviado pela interface antes de chegar ao backend.                                                                                                                                                                                                 |
| **Decisão tomada**     | O backend não deverá confiar no preço enviado pelo cliente para determinar o valor da cobrança. O identificador do produto e a quantidade poderão ser recebidos da requisição, mas o preço deverá ser consultado no banco de dados e o total deverá ser calculado novamente no servidor. |
| **Componente afetado** | Backend, serviço de regras de negócio, banco de dados e integração com o gateway de pagamento.                                                                                                                                                                                           |
| **Resultado esperado** | Impedir que a alteração de parâmetros HTTP permita a compra de produtos por valores manipulados.                                                                                                                                                                                         |

O fluxo deverá seguir, de forma simplificada:

```text
Cliente
   ↓
Envia produto + quantidade
   ↓
Backend
   ↓
Consulta preço oficial no banco
   ↓
Calcula novamente o total
   ↓
Valida estoque, descontos e frete
   ↓
Gera valor final confiável
   ↓
Gateway de pagamento
```

Assim, mesmo que o cliente envie:

```text
produto = 123
quantidade = 1
preco = 1.00
```

o backend não deverá utilizar `preco = 1.00` como fonte de verdade.

O servidor deverá consultar o preço oficial do produto e calcular o valor correto antes de autorizar a cobrança.

## Relação entre os riscos, requisitos, vulnerabilidades e decisões

Para manter a rastreabilidade entre as etapas do trabalho, as decisões da arquitetura foram relacionadas diretamente aos riscos e requisitos definidos anteriormente.

| Risco da Etapa 2                 | Requisito de Segurança                                           | Vulnerabilidade Catalogada                                        | Decisão de Arquitetura                                               |
| -------------------------------- | ---------------------------------------------------------------- | ----------------------------------------------------------------- | -------------------------------------------------------------------- |
| **R04 — Information Disclosure** | **RS01** — Verificar autorização do recurso no backend           | **CWE-639 — Authorization Bypass Through User-Controlled Key**    | **DA01** — Autorização de recursos realizada no backend              |
| **R06 — Elevation of Privilege** | **RS02** — Autorização administrativa exclusivamente no servidor | **CWE-602 — Client-Side Enforcement of Server-Side Security**     | **DA02** — Separação entre autenticação e autorização administrativa |
| **R02 — Tampering**              | **RS03** — Validar preço e total no backend                      | **CWE-472 — External Control of Assumed-Immutable Web Parameter** | **DA03** — Fonte de verdade do preço mantida no servidor             |

Essa relação mantém a continuidade entre as etapas:

```text
Etapa 1
Ameaça / Caso de Abuso
        ↓
Etapa 2
Risco + Priorização + Controle
        ↓
Etapa 3
Requisito de Segurança
        ↓
Vulnerabilidade Catalogada
        ↓
Decisão de Arquitetura
        ↓
Arquitetura Segura
```

## Considerações sobre a Arquitetura

A arquitetura proposta concentra as principais decisões de segurança no backend, pois os dados recebidos da interface web podem ser modificados pelo usuário antes de chegar ao servidor.

Os três principais pontos de proteção são:

1. **Controle de autorização:** o usuário autenticado não poderá acessar recursos de outros usuários apenas alterando seus identificadores.
2. **Controle de privilégios:** o cliente não poderá determinar seu próprio nível de acesso ou transformar uma conta comum em administrativa.
3. **Integridade das transações:** valores críticos, especialmente preços e totais de pedidos, serão determinados e validados no servidor.

A arquitetura também mantém mecanismos complementares identificados nas etapas anteriores, como HTTPS/TLS, logs de auditoria, rate limiting, WAF e integração segura com o gateway de pagamento.

As decisões apresentadas não eliminam todos os riscos do TechStore. Elas representam medidas arquiteturais direcionadas aos três riscos selecionados e deverão ser complementadas por implementação, testes e evidências nas etapas posteriores.

A principal dificuldade desta etapa foi transformar ameaças abstratas em mecanismos concretos de arquitetura. A solução adotada foi manter a rastreabilidade entre as etapas, partindo dos riscos críticos identificados anteriormente e derivando requisitos verificáveis, vulnerabilidades relacionadas e decisões arquiteturais específicas.
