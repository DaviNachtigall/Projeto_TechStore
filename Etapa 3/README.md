# Etapa 3 — Projeto de uma Arquitetura Segura

Esta pasta contém a documentação da **Etapa 3** do projeto **TechStore**, que dá continuidade à análise realizada nas Etapas 1 e 2. Nesta etapa, os riscos prioritários identificados anteriormente são transformados em **requisitos de segurança, vulnerabilidades catalogadas e decisões de arquitetura**, resultando na proposta de uma arquitetura segura para o sistema.

## Conteúdo

### [`01-projeto-de-uma-arquitetura-segura.md`](01-projeto-de-uma-arquitetura-segura.md)

Define os requisitos de segurança derivados dos riscos prioritários da Etapa 2, apresenta os critérios de verificação de cada requisito e relaciona os riscos às vulnerabilidades catalogadas por meio de referências CWE. Também apresenta as principais decisões de arquitetura e a rastreabilidade entre riscos, requisitos, vulnerabilidades e decisões.

### [`02-diagrama-da-arquitetura-segura.md`](02-diagrama-da-arquitetura-segura.md)

Apresenta a proposta de arquitetura segura do TechStore, descrevendo os principais componentes envolvidos na proteção do sistema, como HTTPS/TLS, WAF, rate limiting, backend, autenticação, autorização, regras de negócio, banco de dados, gateway de pagamento e logs de auditoria.

### [`03-arquitetura-segura-techstore.md`](03-arquitetura-segura-techstore.md)

Apresenta o diagrama da arquitetura segura do TechStore utilizando **Mermaid**, permitindo visualizar os componentes da solução e seus relacionamentos. O arquivo também contém uma legenda explicando a função dos principais componentes representados.

### [`04-decioes-de-arquitetura.md`](04-decioes-de-arquitetura.md)

Detalha as principais decisões arquiteturais adotadas para tratar os riscos críticos selecionados na Etapa 2. São apresentadas decisões relacionadas ao controle de autorização, à separação entre autenticação e autorização administrativa e à validação dos preços no servidor, além da relação entre riscos, requisitos, vulnerabilidades e decisões.

## Resumo

Juntos, esses quatro documentos respondem à pergunta central da **Etapa 3**: **como o TechStore deve ser estruturado para reduzir os riscos de segurança identificados nas etapas anteriores?**

Partindo dos riscos priorizados na Etapa 2, o grupo definiu requisitos de segurança verificáveis, relacionou-os a vulnerabilidades conhecidas, estabeleceu decisões arquiteturais e propôs uma arquitetura segura para o TechStore. A solução concentra os controles críticos no backend e utiliza mecanismos como autorização, validação de dados, proteção contra requisições abusivas, logs de auditoria e comunicação segura com o gateway de pagamento.
