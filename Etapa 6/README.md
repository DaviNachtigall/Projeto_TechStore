# Etapa 6 — Monitoramento e Detecção de Intrusões

Esta pasta contém a documentação da **Etapa 6** do projeto **TechStore**, dando continuidade aos riscos, casos de abuso e controles de segurança identificados nas etapas anteriores, definindo como o TechStore poderia **identificar comportamentos suspeitos após sua entrada em operação**, estabelecendo quais eventos devem ser registrados, quais comportamentos devem gerar alertas e quais respostas iniciais devem ser adotadas.

## Conteúdo

### [`01-conceitos-e-objetivos.md`](01-conceitos-e-objetivos.md)

Apresenta os conceitos fundamentais de **detecção de intrusões**, diferenciando mecanismos de **prevenção** e **detecção**. Também explica a finalidade do monitoramento no contexto do TechStore e relaciona a detecção aos riscos identificados nas etapas anteriores, especialmente os riscos de acesso indevido, adulteração de dados, elevação de privilégio e ataques automatizados.

### [`02-eventos-e-fontes-de-monitoramento.md`](02-eventos-e-fontes-de-monitoramento.md)

Define quais **eventos de segurança devem ser registrados e monitorados** pelo TechStore. São considerados eventos relacionados à autenticação, acesso a recursos, operações administrativas, alterações de dados, checkout, erros e comportamento anômalo. O documento também relaciona cada tipo de evento aos riscos que podem ser identificados por meio dele.

### [`03-regras-de-deteccao-e-resposta.md`](03-regras-de-deteccao-e-resposta.md)

Apresenta as **três regras de detecção** propostas para o TechStore. Cada regra especifica o risco observado, a fonte de dados utilizada, a condição que caracteriza um comportamento suspeito e a resposta inicial recomendada após a geração do alerta.

## Resumo

Esses três documentos respondem à pergunta central da **Etapa 6**: **como o TechStore poderia identificar e responder a comportamentos suspeitos durante sua operação?**

Partindo dos riscos identificados nas etapas anteriores, o grupo definiu os principais conceitos de detecção de intrusões, os eventos que deveriam ser registrados e três regras de detecção capazes de identificar comportamentos relacionados aos riscos de segurança do sistema.