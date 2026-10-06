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
