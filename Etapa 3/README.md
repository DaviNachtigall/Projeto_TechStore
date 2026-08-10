Etapa 3 — Projeto de uma Arquitetura Segura

Esta pasta contém a documentação da Etapa 3 do projeto TechStore, dando continuidade às ameaças identificadas na Etapa 1 e aos riscos, prioridades e controles definidos na Etapa 2. Nesta etapa, os riscos mais relevantes são transformados em requisitos de segurança, vulnerabilidades catalogadas e decisões de arquitetura, resultando em uma proposta de arquitetura segura para o sistema.

Conteúdo
01-projeto-de-uma-arquitetura-segura.md

Apresenta os requisitos de segurança definidos a partir dos riscos prioritários da Etapa 2, os critérios utilizados para verificar cada requisito e as vulnerabilidades relacionadas, utilizando referências CWE. Também apresenta as decisões de arquitetura e a relação entre riscos, requisitos, vulnerabilidades e decisões de segurança.

02-diagrama-da-arquitetura-segura.md

Apresenta a arquitetura segura proposta para o TechStore, descrevendo os principais componentes de segurança e o fluxo protegido entre cliente, interface web, WAF, backend, autenticação, autorização, regras de negócio, banco de dados, logs e gateway de pagamento.

03-arquitetura-segura-techstore.md

Contém a representação visual da arquitetura segura do TechStore utilizando Mermaid, permitindo visualizar os componentes do sistema e suas relações. O arquivo também apresenta uma legenda explicando a função de cada componente.

04-decioes-de-arquitetura.md

Detalha as principais decisões arquiteturais adotadas para tratar os riscos críticos selecionados. São apresentadas as decisões relacionadas ao controle de autorização, à separação entre autenticação e autorização administrativa e à validação dos preços no servidor, além da rastreabilidade entre riscos, requisitos, vulnerabilidades e decisões.

Resumo

Juntos, esses documentos respondem à pergunta central da Etapa 3: como o TechStore deve ser estruturado para reduzir os riscos de segurança identificados nas etapas anteriores?

A partir dos riscos priorizados na Etapa 2, o grupo definiu requisitos de segurança verificáveis, relacionou-os a vulnerabilidades conhecidas, estabeleceu decisões arquiteturais e propôs uma arquitetura segura que concentra os controles críticos no backend, mantendo mecanismos como autorização, validação de dados, proteção contra requisições abusivas, logs de auditoria e comunicação segura com o gateway de pagamento.