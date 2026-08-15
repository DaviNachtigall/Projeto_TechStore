# TechStore — Documentação 

Este repositório contém a documentação de segurança do **TechStore**, uma plataforma de e-commerce, desenvolvida ao longo das etapas da disciplina. Cada etapa dá continuidade à anterior, partindo da identificação de ameaças até o monitoramento do sistema em operação.

## Estrutura do repositório

| Pasta | Etapa | Status |
| --- | --- | --- |
| `Etapa 1` | Casos de Abuso e Modelagem de Ameaças com STRIDE | Concluída |
| `Etapa 2` | Análise, Priorização e Tratamento de Riscos com o NIST CSF | Concluída |
| `Etapa 3` | Projeto de uma Arquitetura Segura | Concluída |
| `Etapa 4` | Código Seguro e Testes de Segurança | Concluída |
| `Etapa 5` | — | Não executada |
| `Etapa 6` | Monitoramento e Detecção de Intrusões | Concluída |
| `Etapa 7` | — | Não executada |
| `codigo_Java` | Backend de exemplo em Java puro | Concluída |

As Etapas 5 e 7 não foram realizadas neste projeto.

## Resumo das etapas

### Etapa 1 — Casos de Abuso e Modelagem de Ameaças com STRIDE
Identifica o sistema TechStore, mapeia as principais ameaças usando o modelo STRIDE e detalha seis casos de abuso concretos (um para cada categoria: Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service e Elevation of Privilege).

### Etapa 2 — Análise, Priorização e Tratamento de Riscos com o NIST CSF
Transforma as ameaças e casos de abuso da Etapa 1 em seis riscos avaliáveis (R01 a R06), calcula a probabilidade, o impacto e o nível de cada um, define a ordem de prioridade e propõe um plano de tratamento organizado segundo as seis funções do NIST Cybersecurity Framework 2.0.

### Etapa 3 — Projeto de uma Arquitetura Segura
Transforma os três riscos mais críticos da Etapa 2 (R04, R06 e R02) em requisitos de segurança verificáveis, relaciona-os a vulnerabilidades catalogadas (CWE) e define as decisões de arquitetura que estruturam a solução, com foco em autorização, controle de privilégios e validação de valores no backend.

### Etapa 4 — Código Seguro e Testes de Segurança
Verifica se o código Java implementado está de acordo com as decisões de arquitetura da Etapa 3 e apresenta duas práticas de código seguro com testes definidos antes da implementação: controle de autorização (já implementado corretamente) e armazenamento seguro de senhas (identificado como incompleto, com correção proposta).

### Etapa 5
Não foi executada.

### Etapa 6 — Monitoramento e Detecção de Intrusões
Define como o TechStore poderia identificar comportamentos suspeitos em operação: apresenta os conceitos de prevenção e detecção, os eventos que devem ser registrados (autenticação, acesso a recursos, operações administrativas, checkout, entre outros) e três regras de detecção com a resposta inicial recomendada para cada uma.

### Etapa 7
Não foi executada.

### Backend (`codigo_Java`)
Implementação de exemplo em Java puro (sem frameworks), usada para demonstrar na prática os controles descritos nas Etapas 3 e 4, como autorização no backend, validação de preço no servidor e proteção de senhas.

## Resumo geral

O projeto percorre o ciclo de segurança do TechStore da teoria à prática: começa identificando ameaças e riscos (Etapas 1 e 2), define uma arquitetura e requisitos para tratá-los (Etapa 3), demonstra a aplicação desses requisitos no código (Etapa 4) e propõe como monitorar o sistema já em operação (Etapa 6). As Etapas 5 e 7 não foram desenvolvidas neste trabalho.
