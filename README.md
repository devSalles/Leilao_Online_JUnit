# 🏷️ Sistema de Leilão Online

<div align="center">

## Projeto desenvolvido em Java utilizando Spring Boot

### 🎯 Foco Principal

✔ Testes Unitários com **JUnit 5**

✔ Simulação de dependências com **Mockito**

✔ Análise de cobertura utilizando **JaCoCo**

✔ Validação de regras de negócio de um sistema de leilões

### 📊 Cobertura dos Testes

## ✅ 100% de cobertura na camada Service

<br>

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-brown)
![Bean Validation](https://img.shields.io/badge/Bean%20Validation-Jakarta-success)
![Lombok](https://img.shields.io/badge/Lombok-red)
![Maven](https://img.shields.io/badge/Maven-Build-blueviolet)
![JUnit 5](https://img.shields.io/badge/JUnit-5-red)
![Mockito](https://img.shields.io/badge/Mockito-green)
![JaCoCo](https://img.shields.io/badge/JaCoCo-Code%20Coverage-yellow)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-brightgreen)

</div>

---

# 📖 Sobre o Projeto

Este projeto foi desenvolvido com o objetivo de consolidar conhecimentos em **testes unitários** aplicados a uma API REST desenvolvida com **Java** e **Spring Boot**.

A aplicação simula um sistema de **leilão online**, envolvendo o gerenciamento de usuários, itens, leilões e lances, além da implementação de regras de negócio relacionadas ao fluxo dos leilões.

O sistema possui regras para **agendamento, abertura, cancelamento e encerramento de leilões**, controle de usuários, validação de lances, definição automática do vencedor e transferência de propriedade do item quando o leilão é encerrado com um vencedor.

O principal objetivo foi validar essas regras por meio de testes unitários utilizando **JUnit 5**, **Mockito** e **JaCoCo**, garantindo maior confiabilidade e qualidade do código.

---

# 🏗️ Arquitetura do Projeto

A aplicação foi estruturada utilizando uma arquitetura organizada em camadas, separando as responsabilidades entre **Controllers, Services, DTOs, Repositories, Entities e Tests**.

A camada de negócio concentra as regras do sistema, enquanto a camada de testes possui **Factories** para facilitar a criação dos objetos utilizados nos testes unitários.

<div align="center">

<img width="846" height="1181" alt="Leilão JUnit" src="https://github.com/user-attachments/assets/efda7bc9-851e-4dc7-8ac5-0517c74d0ebc" />
  
</div>

---

# 🔄 Fluxo de Dados

<img width="951" height="1051" alt="Leilão JUnit Fluxo_De_Dados" src="https://github.com/user-attachments/assets/157f5a46-48b3-4eb5-a5a5-d2f079975509" />

---

## 🧪 Estratégia de Testes

Os testes unitários foram concentrados na camada **Service**, responsável por implementar e validar toda a lógica de negócio da aplicação.

| 🏗️ Camadas testadas | 🧪 Estratégias utilizadas |
| :--- | :--- |
| - ✅ UsuarioService<br>- ✅ ItemService<br>- ✅ LeilaoService<br>- ✅ LanceService | - ✅ Mock de Repositories<br>- ✅ Mock de Services<br>- ✅ Factories para criação de objetos<br>- ✅ Testes de sucesso e exceção<br>- ✅ Validação de regras de negócio<br>- ✅ Validação do fluxo de leilões<br>- ✅ Validação das regras de lances<br>- ✅ Validação de estados de usuários, itens e leilões<br>- ✅ Validação de valores e maior lance |

---

## 📊 Cobertura dos Testes

O projeto utiliza o **JaCoCo** para análise da cobertura dos testes unitários, garantindo que as principais regras de negócio estejam devidamente validadas.

### Relatório de Cobertura

<p align="center">
<img width="1041" height="194" alt="RelatorioJaCoCo" src="https://github.com/user-attachments/assets/5a62697d-4ae8-4b74-b719-0e2e90ada541" />

</p>
<br>
<p align="center">
<img width="667" height="149" alt="RelatorioIntellij" src="https://github.com/user-attachments/assets/657fab3a-0799-4238-b755-5d860bed543f" />
</p>


### Resultados

- ✅ **100%** de cobertura de instruções.
- ✅ **100%** de cobertura de branches.
- ✅ **100%** de cobertura de métodos.
- ✅ **100%** de cobertura das classes da camada **Service**.

--- 
# 🎯 Objetivos

- Desenvolver uma API REST para gerenciamento de leilões online utilizando Spring Boot.
- Implementar regras de negócio relacionadas a usuários, itens, leilões e lances.
- Garantir o controle dos estados dos leilões, incluindo AGENDADO, ABERTO, ENCERRADO e CANCELADO.
- Validar as regras de realização de lances, incluindo valor mínimo, maior lance e restrições de usuários.
- Implementar o cálculo automático do vencedor e a atualização do maior lance.
- Automatizar a transferência de propriedade do item ao vencedor do leilão.
- Garantir a integridade dos relacionamentos entre usuários, itens, leilões e lances.
- Validar regras de cancelamento, encerramento e atualização de leilões.
- Aplicar testes unitários para validar regras de negócio, exceções e fluxos de estados.
- Utilizar JUnit 5 e Mockito para garantir a confiabilidade e a qualidade da aplicação.
--- 

# 📁 Estrutura do Projeto

```text
LeilaoOnlineJUnit
├── src
│   ├── main
│   │   ├── java
│   │   │   └── LeilaoOnlineJUnit
│   │   │       ├── controller
│   │   │       │
│   │   │       ├── dto
│   │   │       │   ├── item
│   │   │       │   ├── lance
│   │   │       │   ├── leilao
│   │   │       │   └── usuario
│   │   │       │
│   │   │       ├── entity
│   │   │       │
│   │   │       ├── Enum
│   │   │       │
│   │   │       ├── infra
│   │   │       │   ├── core
│   │   │       │   └── exception
│   │   │       │
│   │   │       ├── repository
│   │   │       │
│   │   │       ├── service
│   │   │       │
│   │   │       └── LeilaoOnlineJUnitApplication.java
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│       └── java
│           └── LeilaoOnlineJUnit
│               ├── factory
│               │   
│               │   
│               │   
│               │   
│               │
│               ├── service
│               │   
│               │   
│               │   
│               │   
│               │
│               └── LeilaoOnlineJUnitApplicationTests.java
│
└── pom.xml
```
---

# 🏷️ Regras de Negócio

O sistema de Leilão Online possui regras de negócio responsáveis por garantir a integridade dos usuários, itens, leilões e lances, além de controlar os fluxos de criação, atualização, cancelamento e encerramento dos leilões.

## 👤 Usuário

### Regras

- O usuário deve possuir ID único.
- O nome é obrigatório e não pode ser vazio ou nulo.
- O CPF é obrigatório e deve ser único no sistema.
- O e-mail é obrigatório e deve ser único no sistema.
- Todo usuário inicia com status `ATIVO`.
- Um usuário pode cadastrar vários itens.
- Um usuário pode criar vários leilões.
- Um usuário pode realizar vários lances.
- Um usuário bloqueado não pode criar novos leilões.
- Um usuário bloqueado não pode realizar lances.
- Um usuário não pode ser removido caso possua leilões ativos.
- Um usuário não pode ser removido caso possua itens vinculados a leilões.

---

## 📦 Item

### Regras

- O item deve possuir ID único.
- O nome é obrigatório e não pode ser vazio.
- A descrição é obrigatória e não pode ser vazia.
- A categoria é obrigatória.
- O valor inicial é obrigatório e deve ser maior que zero.
- Todo item deve possuir um proprietário.
- Todo item pertence a apenas um proprietário.
- Todo item inicia com status `DISPONIVEL`.
- Um item só pode participar de um leilão por vez.
- Um item em leilão não pode ser editado.
- Um item vendido não pode participar de um novo leilão.
- Um item vinculado a um leilão não pode ser excluído.
- Ao encerrar um leilão com vencedor, a propriedade do item é transferida automaticamente para o vencedor.

---

## 🏷️ Leilão

### Regras

- Todo leilão deve possuir um ID único.
- Todo leilão deve estar vinculado a um único item.
- A data de início deve ser posterior à data atual.
- A data de encerramento deve ser posterior à data de início.
- Todo leilão inicia com status `AGENDADO`.
- Um item não pode possuir dois leilões simultaneamente.
- Apenas leilões com status `ABERTO` aceitam lances.
- Um leilão encerrado não aceita novos lances.
- Um leilão cancelado não aceita novos lances.
- Um leilão não pode ser encerrado mais de uma vez.
- Um leilão cancelado não pode voltar para outro status.
- Ao encerrar um leilão com lances, o sistema deve definir automaticamente o vencedor.
- Caso não existam lances, o leilão será encerrado sem vencedor.

### Finalização com vencedor

- O status do leilão será alterado para `ENCERRADO`.
- O maior lance será identificado automaticamente.
- O usuário responsável pelo maior lance será definido como vencedor.
- O item receberá status `VENDIDO`.
- O proprietário do item será atualizado para o usuário vencedor.

### Finalização sem vencedor

- O status do leilão será alterado para `ENCERRADO`.
- Não haverá vencedor.
- O item voltará para o status `DISPONIVEL`.
- O proprietário do item permanecerá inalterado.

---

## 💰 Lance

### Regras

- Todo lance deve possuir um usuário.
- Todo lance deve estar vinculado a um leilão.
- O valor do lance deve ser maior que zero.
- O primeiro lance deve ser maior ou igual ao valor inicial do item.
- Os demais lances devem ser obrigatoriamente maiores que o maior lance atual.
- O proprietário do item não pode realizar lances em seu próprio leilão.
- Usuários bloqueados não podem realizar lances.
- Somente leilões com status `ABERTO` aceitam lances.
- A data e hora do lance devem ser registradas automaticamente.
- Um novo lance válido passa a ser o maior lance do leilão.
- Um lance não pode ser alterado após ser registrado.
- Um lance não pode ser excluído.

---

## 📊 Cálculo do Vencedor

O sistema deve calcular automaticamente:

- O usuário que realizou o maior lance.
- O valor do lance vencedor.
- O item vendido.

### Regras

- O vencedor será sempre o usuário que realizou o maior lance válido.
- Havendo apenas um lance, ele será considerado vencedor.
- Caso não exista nenhum lance, não haverá vencedor.
- O maior lance deverá permanecer registrado após o encerramento do leilão.

---

## 🔄 Atualização de Leilão

Um leilão pode ser alterado **apenas enquanto estiver com status `AGENDADO`**.

Após o início do leilão, não é permitido alterar:

- Data de início.
- Data de encerramento.
- Item.
- Valor inicial.
- Proprietário.

Qualquer tentativa de alteração após o início do leilão deverá gerar erro.

---

## ❌ Cancelamento de Leilão

Um leilão poderá ser cancelado somente quando:

- Estiver com status `AGENDADO`.
- Não possuir nenhum lance registrado.

### Ao cancelar

- O status será alterado para `CANCELADO`.
- O item voltará para o status `DISPONIVEL`.
- O proprietário do item permanecerá inalterado.
- Nenhum novo lance poderá ser realizado.

### Não é permitido cancelar

- Leilões já encerrados.
- Leilões que possuam lances.

---

## ✅ Encerramento de Leilão

Ao encerrar um leilão, o sistema deverá:

1. Alterar o status para `ENCERRADO`.
2. Identificar automaticamente o maior lance.
3. Definir automaticamente o vencedor.
4. Atualizar o status do item.
5. Transferir a propriedade do item para o vencedor, quando houver.

### Caso existam lances

- O item passa para o status `VENDIDO`.
- O maior lance será definido como vencedor.
- O proprietário do item passa a ser o usuário vencedor.

### Caso não existam lances

- Não haverá vencedor.
- O item volta para o status `DISPONIVEL`.
- O proprietário do item permanece inalterado.

Um leilão não pode ser encerrado mais de uma vez.

---

## 📊 Status do Leilão

O sistema possui os seguintes estados:

| Status | Descrição |
| :--- | :--- |
| `AGENDADO` | Leilão criado e aguardando início. |
| `ABERTO` | Leilão disponível para recebimento de lances. |
| `ENCERRADO` | Leilão finalizado e sem possibilidade de novas alterações. |
| `CANCELADO` | Leilão cancelado antes de receber lances. |

### Fluxo de estados

- `AGENDADO` → `ABERTO`
- `AGENDADO` → `CANCELADO`
- `ABERTO` → `ENCERRADO`

Não são permitidas as seguintes transições:

- `CANCELADO` → qualquer outro estado.
- `ENCERRADO` → qualquer outro estado.

---

## ⚠️ Regras Críticas

As regras abaixo possuem maior relevância para os testes unitários da aplicação.

### Regra Crítica 1 — Primeiro lance

O primeiro lance deve ser **maior ou igual ao valor inicial do item**.

### Regra Crítica 2 — Novo maior lance

Todo novo lance deve ser **maior que o maior lance registrado anteriormente**.

### Regra Crítica 3 — Proprietário do item

O proprietário do item não pode realizar lances em seu próprio leilão.

### Regra Crítica 4 — Usuário bloqueado

Usuários bloqueados não podem:

- Criar novos leilões.
- Realizar lances.

### Regra Crítica 5 — Cancelamento

Um leilão que já possua lances não pode ser cancelado.

### Regra Crítica 6 — Encerramento

Ao encerrar um leilão:

- O maior lance deve ser identificado.
- O vencedor deve ser definido automaticamente.
- O item deve ser atualizado para `VENDIDO`.
- O proprietário do item deve ser transferido para o vencedor.

Caso não existam lances:

- Não haverá vencedor.
- O item deverá voltar para `DISPONIVEL`.
- O proprietário permanecerá inalterado.

### Regra Crítica 7 — Item em Leilão

Um item não pode participar de dois leilões simultaneamente.

**Resultado:** erro informando que o item já está vinculado a outro leilão.

---

## 🧪 Complexidade dos Testes

As regras de negócio do sistema permitem a implementação de diferentes cenários de testes unitários, incluindo:

- Testes de validação.
- Testes de fluxo de estados.
- Testes de exceções.
- Testes envolvendo `BigDecimal`.
- Testes envolvendo `LocalDateTime`.
- Testes de regras de negócio.
- Testes de atualização automática do vencedor.
- Testes de transferência de propriedade do item.
- Testes de cancelamento.
- Testes de encerramento.
- Testes de relacionamentos entre entidades.

A complexidade do domínio permite a criação de aproximadamente **90 a 130 testes unitários**, dependendo da quantidade de cenários implementados.

O conjunto de regras foi desenvolvido para proporcionar a prática de **Spring Boot, JUnit 5, Mockito e modelagem de regras de negócio**, utilizando um domínio semelhante aos encontrados em sistemas corporativos.

---

# 🌐 API REST

A API REST do sistema de Leilão Online é organizada em controladores responsáveis pelo gerenciamento dos usuários, itens, leilões e lances.

---

# 👤 Usuários

Base URL:

```text
/usuario
```

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| POST | `/salvar-usuario` | Cadastra um novo usuário. |
| PUT | `/atualizar-usuario/{idUser}` | Atualiza os dados de um usuário. |
| PATCH | `/bloquear-usuario/{idUser}` | Bloqueia um usuário. |
| PATCH | `/desbloquear-usuario/{idUsuario}` | Desbloqueia um usuário. |
| GET | `/exibir-por-id/{idUser}` | Busca um usuário pelo ID. |
| GET | `/exibir-todos` | Lista todos os usuários. |
| GET | `/exibir-por-cpf/{cpf}` | Busca um usuário pelo CPF. |
| GET | `/exibir-por-email/{email}` | Busca um usuário pelo e-mail. |
| GET | `/exibir-por-status/{statusUsuario}` | Lista usuários pelo status. |
| DELETE | `/remover-usuario/{idUsuario}` | Remove um usuário, respeitando as regras de negócio. |

---

## Cadastrar Usuário

**Endpoint**

```http
POST /usuario/salvar-usuario
```

Cadastra um novo usuário no sistema.

O usuário é criado inicialmente com status `ATIVO`, respeitando as validações de nome, CPF e e-mail.

### Corpo da requisição

```json
{
  "nome": "Bernardo Salles",
  "email": "bernardo.salles@example.com",
  "cpf": "529.982.247-25"
}
```
---

# 📦 Itens

Base URL:

```text
/item
```

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| POST | `/salvar-item` | Cadastra um novo item. |
| PUT | `/atualizar-item/{idItem}` | Atualiza os dados de um item. |
| GET | `/buscar-todos` | Lista todos os itens cadastrados. |
| GET | `/buscar-item/{idItem}` | Busca um item pelo ID. |
| GET | `/buscar-categoria/{categoria}` | Lista itens por categoria. |
| GET | `/buscar-status/{statusItem}` | Lista itens pelo status. |
| GET | `/buscar-proprietario/{proprietarioId}` | Lista itens de um determinado proprietário. |
| GET | `/buscar-nome/{nome}` | Lista itens pelo nome. |
| DELETE | `/remover-item/{idItem}` | Remove um item, respeitando as regras de negócio. |

---

## Cadastrar Item

**Endpoint**

```http
POST /item/salvar-item
```

Cadastra um novo item no sistema.

O item deve possuir nome, descrição, categoria, valor inicial e um proprietário válido.

### Corpo da requisição

```json
{
  "nomeItem": "Notebook Dell Latitude",
  "descricaoItem": "Notebook Dell Latitude 3520, Intel Core i7, 16GB RAM e SSD de 512GB.",
  "categoriaItem": "Eletrônicos",
  "valorInicialItem": 2500.00,
  "proprietarioItemId": 1
}
```

> O campo `proprietarioItemId` representa a chave estrangeira (FK) do usuário proprietário. Neste exemplo, o item será vinculado ao usuário de ID `1`.

