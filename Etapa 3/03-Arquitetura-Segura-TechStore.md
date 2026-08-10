# Arquitetura Segura — TechStore

```mermaid
flowchart TD

    U[Cliente / Usuário]
    W[Interface Web]
    TLS[HTTPS / TLS]
    WAF[WAF / Rate Limiting]

    API[Backend / API]
    AUTH[Serviço de Autenticação]
    AUTHZ[Serviço de Autorização]
    RULES[Regras de Negócio]
    PRICE[Validação de Preço no Servidor]

    DB[(Banco de Dados)]
    LOG[Logs de Auditoria / Monitoramento]
    PAY[Gateway de Pagamento Externo]

    U --> W
    W --> TLS
    TLS --> WAF
    WAF --> API

    API --> AUTH
    AUTH --> DB

    API --> AUTHZ
    AUTHZ --> DB

    API --> RULES
    RULES --> PRICE
    PRICE --> DB

    RULES --> PAY
    PAY --> RULES

    API --> LOG
    AUTH --> LOG
    AUTHZ --> LOG
    RULES --> LOG

    DB -.-> LOG
```

## Legenda

* **Cliente / Usuário:** usuário que acessa a plataforma.
* **Interface Web:** camada de interação com o sistema.
* **HTTPS / TLS:** proteção da comunicação.
* **WAF / Rate Limiting:** proteção contra requisições abusivas e ataques automatizados.
* **Backend / API:** processamento das requisições.
* **Serviço de Autenticação:** validação da identidade do usuário.
* **Serviço de Autorização:** validação das permissões do usuário.
* **Regras de Negócio:** processamento das operações da plataforma.
* **Validação de Preço no Servidor:** garantia da integridade dos valores utilizados no checkout.
* **Banco de Dados:** armazenamento dos dados da aplicação.
* **Gateway de Pagamento Externo:** processamento das transações financeiras.
* **Logs de Auditoria / Monitoramento:** registro e acompanhamento de eventos relevantes de segurança.
