# Etapa 1 — Casos de Abuso e Modelagem de Ameaças com STRIDE

Esta pasta contém a documentação de segurança referente à **Etapa 1** do projeto **TechStore**, uma plataforma de e-commerce. A análise identifica o sistema, mapeia as principais ameaças usando o modelo **STRIDE** e detalha cenários práticos de ataque (casos de abuso).

## Conteúdo

### [`01-identificacao-e-descricao-do-sistema.md`](./01-identificacao-e-descricao-do-sistema.md)
Apresenta a identificação do sistema (nome, equipe, repositório e justificativa da escolha), a descrição funcional do TechStore (problema resolvido, perfis de usuário, principais funcionalidades e dados sensíveis manipulados), os ativos críticos a proteger, os pontos de interação que compõem a superfície de ataque, e a visão geral da arquitetura e do fluxo de dados entre cliente, backend e gateway de pagamento.

### [`02-modelagem-de-ameacas-stride.md`](./02-modelagem-de-ameacas-stride.md)
Traz a tabela de modelagem de ameaças aplicando o modelo **STRIDE** (Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service e Elevation of Privilege) aos componentes do sistema, identificando 6 ameaças (T01–T06) — desde ataques de força bruta no login até elevação de privilégio no painel administrativo — com seus respectivos impactos.

### [`03-casos-de-abuso.md`](./03-casos-de-abuso.md)
Detalha seis casos de abuso concretos derivados da modelagem de ameaças, um para cada categoria do STRIDE:
- **CA01** — Alteração maliciosa do valor de um produto durante o checkout (Tampering).
- **CA02** — Acesso indevido a contas de clientes usando credenciais vazadas de outros sites, também conhecido como *Credential Stuffing* (Spoofing).
- **CA03** — Negação da autoria de uma compra por falta de registros que comprovem quem a realizou (Repudiation).
- **CA04** — Acesso indevido a dados de outros clientes por meio de uma vulnerabilidade IDOR (*Insecure Direct Object Reference*) na API de perfil de usuário (Information Disclosure).
- **CA05** — Sobrecarga da página de pagamento durante um período de alto tráfego, deixando o site fora do ar (Denial of Service).
- **CA06** — Obtenção indevida de acesso ao painel administrativo por meio da alteração do tipo de usuário na requisição (Elevation of Privilege).

## Resumo

Juntos, esses três documentos formam a base da análise de segurança do TechStore: primeiro descrevem **o que** o sistema é e **o que** precisa ser protegido, depois **quais ameaças** existem segundo o STRIDE, e por fim **como, na prática**, um atacante poderia explorá-las.
