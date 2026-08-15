# Etapa 4 — Código Seguro e Testes de Segurança

Esta pasta contém a documentação da **Etapa 4** do projeto **TechStore**, que dá continuidade à Etapa 3. Aqui, as decisões de arquitetura são verificadas no código real e transformadas em práticas concretas de implementação segura, com testes definidos antes da implementação.

## Conteúdo

### [`01-codigo-seguro-parte1-autorizacao.md`](./01-codigo-seguro-parte1-autorizacao.md)
Apresenta o objetivo da etapa, a verificação de conformidade do código Java com as três decisões de arquitetura da Etapa 3 (DA01, DA02, DA03), e a **Prática 1 — Controle de Autorização (Prevenção de IDOR)**: risco e requisito relacionados, testes definidos antes da implementação (TS01 e TS02), o trecho de código responsável pela proteção, o resultado esperado e as referências da OWASP utilizadas.

### [`02-codigo-seguro-parte2-senhas.md`](./02-codigo-seguro-parte2-senhas.md)
Apresenta a **Prática 2 — Armazenamento Seguro de Senhas**: risco e requisito relacionados (incluindo o novo requisito RS04), testes definidos antes da implementação, o problema identificado no código atual (hash sem *salt*), a correção proposta em pseudocódigo, o resultado esperado, as referências da OWASP e as considerações finais da etapa.

## Resumo

A verificação mostrou que o código do TechStore já segue corretamente as três decisões de arquitetura da Etapa 3. A partir disso, foram escolhidas duas práticas de código seguro: uma já implementada corretamente (**controle de autorização**, usada como exemplo de boa prática) e outra identificada como incompleta durante a revisão (**armazenamento de senhas**, usada para demonstrar testes que falham hoje e a correção proposta).
