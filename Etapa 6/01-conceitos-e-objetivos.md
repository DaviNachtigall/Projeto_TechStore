# Conceitos e Objetivos

## Etapa 6: Monitoramento e Detecção de Intrusões

### Objetivo

A detecção de intrusões consiste na identificação de comportamentos que possam indicar uma tentativa de ataque, uso indevido ou comprometimento do sistema. Enquanto os mecanismos de prevenção procuram impedir que uma ação maliciosa aconteça, os mecanismos de detecção procuram identificar e registrar comportamentos suspeitos para que uma resposta possa ser realizada.

No contexto do TechStore, a detecção deve complementar os controles de segurança definidos na Etapa 2. Os riscos identificados anteriormente, principalmente aqueles relacionados a **Spoofing, Tampering, Information Disclosure e Elevation of Privilege**, podem produzir eventos observáveis em registros de autenticação, requisições, acessos a recursos e operações administrativas.

Esta etapa não implementa um sistema de detecção de intrusões. O objetivo é definir um roteiro conceitual de quais eventos deveriam ser registrados, quais comportamentos poderiam gerar alertas e quais respostas iniciais seriam recomendadas.

---

### Prevenção e detecção

Prevenção e detecção possuem objetivos diferentes, embora sejam complementares.

A **prevenção** procura impedir ou dificultar a ocorrência de uma ação maliciosa. Por exemplo, o bloqueio temporário de novas tentativas após várias falhas de autenticação procura impedir ataques automatizados de força bruta ou credential stuffing.

A **detecção**, por outro lado, procura identificar que determinado comportamento está ocorrendo ou que uma tentativa de ataque pode estar acontecendo. No mesmo exemplo, o sistema pode registrar várias tentativas de login malsucedidas e gerar um alerta para a equipe responsável.

No NIST CSF 2.0, utilizado na Etapa 2, essa distinção está relacionada principalmente às funções **Protect**, **Detect** e **Respond**. Os controles de proteção procuram reduzir a probabilidade ou o impacto dos riscos, enquanto a detecção procura identificar eventos suspeitos e a resposta define as ações posteriores.