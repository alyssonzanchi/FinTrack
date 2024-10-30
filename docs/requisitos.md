# Requisitos

## Requisitos funcionais

- [x] O sistema deve permitir que novos usuários se cadastrem;
- [x] O sistema deve permitir que os usuários façam login e logout;
- [x] O sistema deve permitir que os usuários criem, editem e excluam contas (ex: conta corrente, poupança, etc);
- [x] O sistema deve permitir que os usuários registrem receitas e despesas;
- [x] O sistema deve permitir que os usuários categorizem transações (ex: alimentação, transporte, lazer);
- [x] O sistema deve gerar relatórios de receitas e despesas por período;
- [x] O sistema deve exibir gráficos e tabelas de desempenho financeiro;
- [x] O sistema deve enviar notificações para o usuário sobre transações pendentes ou alertas de limite de conta;

## Requisitos não funcionais

- [x] O sistema deve ser capaz de processar 100 transações em menos de 2 segundos;
- [x] O sistema deve ser capaz de suportar até 10.000 usuários simultâneos;
- [x] O sistema deve possuir uma interface intuitiva e amigável, permitindo que novos usuários se familiarizem rapidamente;
- [x] O sistema deve criptografar os dados do usuário e utilizar autenticação segura;
- [x] O código deve ser modular e documentado para facilitar a manutenção e atualização do sistema;

## Regras de negócio

- [x] O usuário não pode registrar uma despesa maior que o saldo disponível da conta;
- [x] Cada transação deve ser categorizada em uma das categorias predefinidas;
- [x] O sistema deve manter um histórico de transações por pelo menos 12 meses;
- [x] Apenas usuários autenticados podem registrar transações e acessar relatórios financeiros;
