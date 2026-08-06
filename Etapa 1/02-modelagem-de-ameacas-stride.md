# Modelagem de Ameaças com STRIDE

## Etapa 1: Casos de Abuso e Modelagem de Ameaças com STRIDE

Com base nos princípios de segurança (derivar requisitos não funcionais de ameaças, mapear vulnerabilidades e projetar uma arquitetura segura), estruturou-se a seguinte análise:

| **ID** | **Categoria STRIDE** | **Componente ou Ativo** | **Ameaça Identificada** | **Possível Impacto** |
| --- | --- | --- | --- | --- |
| **T01** | **Spoofing** (Falsificação de identidade) | Autenticação / Login | Atacante realiza ataques de Força Bruta ou utiliza credenciais vazadas (*Credential Stuffing*) para acessar a conta do cliente. | Acesso não autorizado a dados pessoais e realização de compras fraudulentas. |
| **T02** | **Tampering** (Adulteração de dados) | Carrinho de Compras / API | Atacante intercepta a requisição do checkout e altera o valor do produto (ex: de R$ 5.000,00 para R$ 1,00) antes de enviar ao servidor. | Prejuízo financeiro direto e inconsistência na contabilização de vendas. |
| **T03** | **Repudiation** (Repúdio) | Histórico de Pedidos | Usuário alega que não realizou uma transação financeira devido à ausência de registros auditáveis (*logs*) na plataforma. | Dificuldades jurídicas, custos de suporte e perda financeira com cancelamentos (*chargebacks*). |
| **T04** | **Information Disclosure** (Exposição de informação) | Banco de Dados / API | Endpoint /api/users/1 expõe dados pessoais de outros clientes sem validar a permissão do usuário logado (Vulnerabilidade IDOR). | Vazamento em massa de dados sensíveis, causando violação da LGPD e penalidades legais. |
| **T05** | **Denial of Service** (Negação de serviço) | Servidor Web / Checkout | Envio massivo de requisições maliciosas na rota de checkout durante eventos de alto tráfego (ex: Black Friday). | Indisponibilidade do sistema, queda do serviço e perda de vendas imediatas. |
| **T06** | **Elevation of Privilege** (Elevação de privilégio) | Painel Administrativo | Um cliente comum altera o parâmetro role=user para role=admin no corpo da requisição HTTP. | Acesso completo ao sistema, permitindo alteração no catálogo, exclusão de dados e roubo de informações. |
