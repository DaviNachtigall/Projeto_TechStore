# Código Seguro e Testes de Segurança — Parte 1

## Etapa 4: Objetivo, Verificação de Conformidade e Prática 1

### Objetivo

Esta etapa demonstra como as decisões de arquitetura da Etapa 3 se transformam em práticas concretas de implementação segura, com testes definidos **antes** da implementação.

### Verificação de conformidade com a Etapa 3

| Decisão | Risco | Situação no código | Evidência |
| --- | --- | --- | --- |
| **DA01** — Autorização no backend | R04 — IDOR | Implementado | `handlePerfil`, em `Main.java`, recusa (403) quando `idSolicitado != usuarioLogado.id` e o usuário não é ADMIN |
| **DA02** — Papel validado só no servidor | R06 — Elevation of Privilege | Implementado | O `role` nunca é lido do corpo da requisição; vem sempre do `User` associado ao token |
| **DA03** — Preço revalidado no servidor | R02 — Tampering | Implementado | `CheckoutService.finalizarCompra` usa sempre `produto.preco` do `ProductRepository`; a rota `/checkout` não recebe nenhum campo de preço |

O código está coerente com as três decisões de arquitetura. A revisão identificou, porém, que o armazenamento de senhas usa hash sem *salt*, ponto tratado na Prática 2 (documento separado).

---

## Prática 1 — Controle de Autorização (Prevenção de IDOR)

### Risco e requisito relacionados
- **Risco:** R04 — Information Disclosure (IDOR), Etapa 2.
- **Requisito:** RS01 — o backend deve verificar se o usuário autenticado tem autorização para acessar o recurso solicitado, Etapa 3.
- **Decisão de arquitetura:** DA01.

### Testes definidos antes da implementação

| Teste | Entrada ou ação | Resultado esperado |
| --- | --- | --- |
| **TS01** | Cliente autenticado (id 102) acessa `GET /perfil?id=102` (o próprio perfil) | A solicitação é permitida e os dados do próprio perfil são retornados (200) |
| **TS02** | Cliente autenticado (id 102) acessa `GET /perfil?id=101` (perfil de outro cliente), sem ser administrador | A solicitação é recusada (403) e nenhum dado do outro cliente é retornado |

### Implementação

Trecho de `Main.java` (`handlePerfil`):

```java
// Correção do IDOR: só pode ver o próprio perfil, a menos que seja administrador.
if (idSolicitado != usuarioLogado.id && !usuarioLogado.isAdmin()) {
    responderJson(exchange, 403, "{\"erro\":\"Você não tem permissão para ver este perfil\"}");
    return;
}
```

A verificação acontece **sempre no servidor**, comparando o dono do token (`usuarioLogado.id`) com o recurso solicitado (`idSolicitado`), independentemente do que a interface do cliente permita ou não enviar.

### Resultado esperado

Um usuário autenticado só consegue visualizar seus próprios dados, exceto se for administrador. Qualquer tentativa de acessar o perfil de outro cliente é bloqueada no backend, mesmo que o ID seja alterado manualmente na requisição.

### Referência OWASP

- OWASP Cheat Sheet Series — *Authorization Cheat Sheet*
- OWASP Cheat Sheet Series — *Insecure Direct Object Reference Prevention Cheat Sheet*
- OWASP ASVS — Categoria V4 (Access Control)
