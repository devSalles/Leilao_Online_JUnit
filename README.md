# 🏷️ Regras de Negócio — Sistema de Leilão Online

Este documento descreve as principais regras de negócio implementadas no **Sistema de Leilão Online**, incluindo regras relacionadas a usuários, itens, leilões, lances, encerramento, cancelamento e transferência de propriedade.

---

## 👤 Usuário

Todo usuário deve possuir:

* ID único
* Nome obrigatório
* CPF obrigatório
* E-mail obrigatório
* Status do usuário

### Regras

* O nome do usuário não pode ser vazio ou nulo.
* O CPF deve ser único no sistema.
* O e-mail deve ser único no sistema.
* Todo usuário inicia com status `ATIVO`.
* Um usuário pode cadastrar vários itens.
* Um usuário pode criar vários leilões.
* Um usuário pode realizar vários lances.
* Um usuário bloqueado não pode criar novos leilões.
* Um usuário bloqueado não pode realizar lances.
* Um usuário não pode ser removido caso possua leilões ativos.
* Um usuário não pode ser removido caso possua itens vinculados a leilões.

---

## 📦 Item

Todo item deve possuir:

* ID único
* Nome obrigatório
* Descrição obrigatória
* Categoria obrigatória
* Valor inicial obrigatório
* Proprietário obrigatório
* Status do item

### Regras

* O nome do item não pode ser vazio.
* A descrição não pode ser vazia.
* O valor inicial deve ser maior que zero.
* Todo item pertence a apenas um proprietário.
* Um item só pode participar de um leilão por vez.
* Todo item inicia com status `DISPONIVEL`.
* Um item em leilão não pode ser editado.
* Um item vendido não pode voltar para um novo leilão.
* Um item vinculado a um leilão não pode ser excluído.
* Ao encerrar um leilão com vencedor, a propriedade do item será transferida automaticamente para o usuário vencedor.

---

## 🏷️ Leilão

Todo leilão deve possuir:

* ID único
* Item obrigatório
* Data de início
* Data de encerramento
* Status
* Maior lance atual
* Usuário vencedor, quando existir

### Regras

* Todo leilão deve estar vinculado a um único item.
* Um item não pode possuir dois leilões simultaneamente.
* A data de início deve ser posterior à data atual.
* A data de encerramento deve ser posterior à data de início.
* Todo leilão inicia com status `AGENDADO`.
* Apenas leilões `ABERTOS` aceitam lances.
* Um leilão pode ser cancelado somente se ainda não possuir lances.
* Um leilão encerrado não aceita novos lances.
* Um leilão cancelado não aceita novos lances.
* Um leilão não pode ser encerrado mais de uma vez.
* Um leilão cancelado não pode voltar para outro status.
* Ao encerrar um leilão com lances, o sistema deve definir automaticamente o vencedor.
* Caso não existam lances, o leilão será encerrado sem vencedor.

### Encerramento com vencedor

Ao finalizar um leilão com vencedor:

* O item deverá receber status `VENDIDO`.
* O proprietário do item será atualizado para o usuário vencedor.

### Encerramento sem vencedor

Ao finalizar um leilão sem vencedor:

* O item deverá voltar para status `DISPONIVEL`.
* Não haverá vencedor.
* O proprietário do item permanecerá inalterado.

---

## 💰 Lance

Todo lance deve possuir:

* Usuário obrigatório
* Leilão obrigatório
* Valor
* Data e hora do lance

### Regras

* O valor do lance deve ser maior que zero.
* O primeiro lance deve ser maior ou igual ao valor inicial do item.
* Os demais lances devem ser obrigatoriamente maiores que o maior lance atual.
* O proprietário do item não pode realizar lances em seu próprio leilão.
* Usuários bloqueados não podem realizar lances.
* Somente leilões com status `ABERTO` aceitam lances.
* Todo lance deve registrar automaticamente a data e hora da operação.
* Sempre que um novo lance válido for realizado, ele passa a ser o maior lance do leilão.
* Um lance não pode ser alterado após ser registrado.
* Um lance não pode ser excluído.

---

## 📊 Cálculo do Vencedor

O sistema deve calcular automaticamente:

* Qual usuário realizou o maior lance.
* Qual foi o valor vencedor.
* Qual item foi vendido.

### Regras

* O vencedor será sempre o usuário que realizou o maior lance válido.
* Havendo apenas um lance, ele será considerado vencedor.
* Caso não exista nenhum lance, não haverá vencedor.
* O maior lance deverá permanecer registrado após o encerramento do leilão.

---

## 🔄 Atualização de Leilão

Um leilão pode ser alterado **apenas enquanto estiver com status `AGENDADO`**.

Após o início do leilão:

* Não pode alterar as datas.
* Não pode alterar o item.
* Não pode alterar o valor inicial.
* Não pode alterar o proprietário.

Qualquer tentativa de alteração após o início do leilão deverá gerar erro.

---

## ❌ Cancelamento de Leilão

Um leilão poderá ser cancelado apenas quando:

* Estiver com status `AGENDADO`.
* Não possuir nenhum lance registrado.

### Ao cancelar

* O status deverá ser alterado para `CANCELADO`.
* O item deverá voltar para status `DISPONIVEL`.
* O proprietário do item permanecerá inalterado.
* Nenhum novo lance poderá ser realizado.

### Não é permitido cancelar

* Leilões já encerrados.
* Leilões que possuam lances.

---

## ✅ Encerramento de Leilão

Quando um leilão for encerrado, o sistema deverá:

1. Alterar o status para `ENCERRADO`.
2. Identificar automaticamente o maior lance.
3. Definir automaticamente o vencedor.
4. Atualizar o status do item.
5. Transferir automaticamente a propriedade do item para o usuário vencedor, quando houver vencedor.

### Caso existam lances

* O item passa para status `VENDIDO`.
* O proprietário do item passa a ser o usuário vencedor.
* O maior lance é utilizado para definir o vencedor.

### Caso não existam lances

* O item volta para status `DISPONIVEL`.
* Não haverá vencedor.
* O proprietário permanece inalterado.

> Um leilão não pode ser encerrado mais de uma vez.

---

## 📊 Status do Leilão

O leilão possui os seguintes estados:

```text
AGENDADO
ABERTO
ENCERRADO
CANCELADO
```

### Fluxo de estados

```text
AGENDADO
   ├──> ABERTO ───> ENCERRADO
   │
   └──> CANCELADO
```

### Regras de transição

* Todo novo leilão inicia como `AGENDADO`.
* Um leilão `AGENDADO` pode ser aberto.
* Um leilão `ABERTO` pode ser encerrado.
* Um leilão `AGENDADO` pode ser cancelado.
* Um leilão `CANCELADO` não pode voltar para outro estado.
* Um leilão `ENCERRADO` não pode voltar para outro estado.

---

# ⚠️ Regras Críticas

As regras abaixo possuem maior relevância para os testes unitários do projeto.

## 1. Primeiro lance

O primeiro lance deverá ser **maior ou igual ao valor inicial do item**.

```text
Lance >= Valor Inicial
```

---

## 2. Novo maior lance

Todo novo lance deverá ser **maior que o maior lance atualmente registrado**.

```text
Novo Lance > Maior Lance Atual
```

---

## 3. Proprietário do item

O proprietário do item não poderá realizar lances em seu próprio leilão.

---

## 4. Usuário bloqueado

Usuários bloqueados não podem:

* Criar leilões.
* Realizar lances.

---

## 5. Cancelamento

Um leilão que já possua lances não poderá ser cancelado.

---

## 6. Encerramento

Ao encerrar um leilão:

* O maior lance deverá ser identificado.
* O vencedor deverá ser definido automaticamente.
* O item deverá ser atualizado para `VENDIDO`.
* O proprietário do item deverá ser transferido automaticamente para o vencedor.

### Caso não existam lances

* Não haverá vencedor.
* O item deverá voltar para `DISPONIVEL`.
* O proprietário permanecerá inalterado.

---

## 7. Item em Leilão

Um item não poderá participar de dois leilões simultaneamente.

**Resultado esperado:**

```text
Erro — item já vinculado a outro leilão.
```

---

# 🧪 Complexidade Esperada

O projeto foi desenvolvido com foco na aplicação de regras de negócio e testes unitários utilizando **Spring Boot, JUnit 5 e Mockito**.

Entre os principais cenários de teste estão:

* Testes de validação.
* Testes de fluxo de estados.
* Testes de exceções.
* Testes envolvendo `BigDecimal`.
* Testes com `LocalDateTime`.
* Testes de regras de negócio.
* Testes de atualização automática do vencedor.
* Testes de transferência de propriedade do item.
* Testes de cancelamento.
* Testes de encerramento.
* Testes de relacionamentos entre entidades.

### Quantidade esperada de testes

O domínio apresenta uma quantidade estimada de:

**90 a 130 testes unitários**, dependendo da quantidade de cenários implementados.

---

## 🎯 Objetivo das Regras de Negócio

O conjunto de regras foi definido para representar um domínio consistente de **leilões online**, permitindo praticar conceitos presentes em sistemas corporativos, como:

* Modelagem de domínio.
* Validação de regras de negócio.
* Controle de estados.
* Relacionamentos entre entidades.
* Tratamento de exceções.
* Operações transacionais.
* Testes unitários.
* Testes de comportamento.
* Uso de `BigDecimal` para valores monetários.
* Manipulação de datas com `LocalDateTime`.
* Simulação de dependências com Mockito.
* Desenvolvimento de APIs REST com Spring Boot.
