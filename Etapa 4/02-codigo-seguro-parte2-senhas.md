# Código Seguro e Testes de Segurança — Parte 2

## Etapa 4: Prática 2 e Considerações Finais

## Prática 2 — Armazenamento Seguro de Senhas

### Risco e requisito relacionados
- **Risco:** R01 — Spoofing / Credential Stuffing, Etapa 2 (ligado também à ameaça T01 da Etapa 1).
- **Requisito novo:** **RS04** — o sistema deve armazenar senhas usando uma função de hash lenta e com *salt*, para que um eventual vazamento do banco não exponha as senhas reais dos usuários.

Esse requisito complementa os três definidos na Etapa 3, aplicando o mesmo princípio de "nunca confiar em dado sensível sem proteção adequada" — desta vez à senha, e não ao preço ou à permissão.

### Testes definidos antes da implementação

| Teste | Entrada ou ação | Resultado esperado |
| --- | --- | --- |
| **TS01** | Um usuário se cadastra com a senha `"minhaSenha123"` | O valor salvo no banco não é a senha em texto puro nem um hash igual para todos os usuários com a mesma senha (cada hash deve ser único, mesmo com senha repetida) |
| **TS02** | O banco de dados vaza (cenário simulado) e um atacante tenta usar uma *rainbow table* para descobrir a senha original a partir do hash salvo | O ataque não deve ter sucesso em tempo viável, pois o hash usa *salt* único e uma função lenta (dificulta força bruta em massa) |

### Implementação atual (identificada como incompleta)

`PasswordUtil.java` usa SHA-256 sem *salt*:

```java
public static String hash(String senhaPura) {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] bytes = digest.digest(senhaPura.getBytes(StandardCharsets.UTF_8));
    // ...
}
```

Isso falha no **TS01**: duas contas com a mesma senha geram exatamente o mesmo hash, e o algoritmo é rápido demais, facilitando ataques de força bruta em massa (falha também no **TS02**).

### Correção proposta (pseudocódigo)

```java
public static String hash(String senhaPura) {
    byte[] salt = gerarSaltAleatorio(16); // salt único por usuário
    byte[] hash = PBKDF2(senhaPura, salt, iteracoes = 100_000);
    return salt + ":" + hash; // salt é salvo junto do hash
}

public static boolean confere(String senhaPura, String hashSalvo) {
    salt, hashOriginal = separar(hashSalvo);
    hashCalculado = PBKDF2(senhaPura, salt, iteracoes = 100_000);
    return hashCalculado == hashOriginal;
}
```

A ideia central: usar um algoritmo lento (PBKDF2, bcrypt ou Argon2) combinado com um *salt* aleatório gerado para cada usuário, salvo junto ao hash no banco.

### Resultado esperado

Mesmo que o banco de dados seja vazado, senhas iguais entre usuários diferentes não geram o mesmo hash, e o custo computacional para tentar descobrir a senha original em massa se torna muito alto, reduzindo a probabilidade de sucesso do risco R01 (Spoofing).

### Referência OWASP

- OWASP Cheat Sheet Series — *Password Storage Cheat Sheet*
- OWASP ASVS — Categoria V2 (Authentication)

---

## Considerações finais

O código do TechStore já implementa corretamente as três decisões de arquitetura da Etapa 3 (autorização no backend, papel validado no servidor e preço revalidado no servidor). A revisão para esta etapa identificou um ponto de melhoria fora dessas três decisões — o armazenamento de senhas sem *salt* — que foi usado como segunda prática, mostrando tanto uma prática já correta (autorização) quanto uma prática que precisa ser corrigida (senhas), com testes definidos antes de qualquer mudança no código.
