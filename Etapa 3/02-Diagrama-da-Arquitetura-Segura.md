# Diagrama da Arquitetura Segura

A arquitetura segura proposta mantém a estrutura geral do TechStore apresentada na Etapa 1, porém adiciona controles explícitos de autenticação, autorização, validação de regras de negócio, registros de auditoria e proteção da infraestrutura.

## Arquitetura proposta

```text
                         ┌─────────────────────┐
                         │   Cliente / Usuário  │
                         └──────────┬──────────┘
                                    │
                                    │ HTTPS / TLS
                                    ▼
                         ┌─────────────────────┐
                         │    Interface Web    │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   WAF / Rate Limit  │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    Backend / API    │
                         └──────┬──────┬───────┘
                                │      │
                 ┌──────────────┘      └──────────────┐
                 ▼                                     ▼
        ┌──────────────────┐                 ┌──────────────────┐
        │   Autenticação   │                 │   Autorização    │
        └────────┬─────────┘                 └────────┬─────────┘
                 │                                    │
                 └────────────────┬───────────────────┘
                                  ▼
                       ┌─────────────────────┐
                       │   Banco de Dados    │
                       └──────────┬──────────┘
                                  │
                                  │
                         ┌────────▼────────┐
                         │ Regras de       │
                         │ Negócio         │
                         └───────┬─────────┘
                                 │
                         ┌───────▼──────────┐
                         │ Validação de     │
                         │ Preço no Servidor│
                         └───────┬──────────┘
                                 │
                                 ▼
                       ┌─────────────────────┐
                       │ Gateway de Pagamento│
                       └─────────────────────┘

        ┌───────────────────────────────────────────┐
        │       Logs / Auditoria / Monitoramento    │
        └───────────────────────────────────────────┘
```

## Descrição dos componentes

**Cliente / Usuário:**

Interage com a plataforma por meio da interface web. As informações enviadas pelo cliente são consideradas não confiáveis até que sejam validadas pelo backend.

**Interface Web:**

Apresenta catálogo, carrinho, checkout e funcionalidades de gerenciamento. A interface não é responsável por decisões finais de segurança.

**HTTPS/TLS:**

Protege a comunicação entre o navegador e o sistema contra interceptação e alteração durante o transporte.

**WAF / Rate Limiting:**

Atua como uma camada adicional de proteção antes do backend, ajudando a limitar requisições abusivas e reduzir ataques automatizados e sobrecarga.

**Backend / API:**

É o principal ponto de aplicação das regras de segurança. Todas as requisições recebidas devem ser tratadas como potencialmente manipuláveis.

**Serviço de Autenticação:**

Responsável por verificar a identidade do usuário e estabelecer sua sessão autenticada.

**Serviço de Autorização:**

Determina se o usuário autenticado possui permissão para executar determinada operação ou acessar determinado recurso.

**Regras de Negócio:**

Executam as operações do e-commerce sem confiar em informações críticas enviadas pelo cliente.

**Validação de Preço no Servidor:**

Consulta os valores oficiais dos produtos no banco de dados e calcula novamente o valor do pedido antes da comunicação com o gateway de pagamento.

**Banco de Dados:**

Armazena informações de usuários, produtos, preços, pedidos e demais dados necessários ao funcionamento da plataforma.

**Gateway de Pagamento Externo:**

Processa as transações financeiras e recebe do backend somente os valores que foram previamente validados.

**Logs de Auditoria / Monitoramento:**

Registram eventos relevantes, como autenticações, tentativas de acesso negadas, alterações críticas e operações de checkout, permitindo investigação posterior.

## Fluxo seguro de uma operação

1. O cliente acessa a interface web.
2. A comunicação ocorre utilizando HTTPS/TLS.
3. As requisições passam pela camada de proteção e chegam à API.
4. O backend autentica o usuário quando necessário.
5. O backend verifica a autorização para o recurso ou operação solicitada.
6. As regras de negócio processam a solicitação.
7. Dados críticos, como preço e permissões, são obtidos ou validados no servidor.
8. No checkout, o backend consulta o preço oficial no banco de dados e calcula novamente o total.
9. Somente após a validação o backend envia a operação ao gateway de pagamento.
10. Os eventos relevantes são registrados nos logs de auditoria.

Dessa forma, a arquitetura não considera a interface do cliente, parâmetros HTTP ou campos enviados pelo navegador como fontes confiáveis para decisões críticas de segurança.
