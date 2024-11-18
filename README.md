### 📒 API REST - Agenda de Contatos
Uma API REST para gerenciar uma agenda de contatos com funcionalidades para adicionar, buscar, atualizar e remover contatos, telefones e endereços.

## 🚀 Tecnologias Utilizadas
* Java 17

* Spring Boot
  
* Spring Data JPA
  
* PostgreSQL

* Swagger para documentação

* JUnit para testes unitários

## 📝 Requisitos
Funcionalidades Básicas
Contatos

* Adicionar um contato com nome e sobrenome.

* Listar todos os contatos.

* Buscar contato por palavra-chave (nome ou sobrenome).

* Atualizar os dados de um contato.

* Remover um contato da agenda.

* Remover todos os contatos.

* Telefones

* Adicionar um telefone a um contato (com DDD e número).
  
* Listar todos os telefones de um contato.
  
* Atualizar os dados de um telefone.
  
* Exibir informações detalhadas de um telefone.
  
* Remover um telefone de um contato.
  
* Endereços

* Adicionar um endereço a um contato (com CEP, logradouro, número, cidade e estado).

* Listar todos os endereços de um contato.

* Atualizar os dados de um endereço.

* Exibir informações detalhadas de um endereço.

* Remover um endereço de um contato.

## Requisitos Bônus

* Paginação para listar contatos, telefones e endereços.

* Documentação da API com Swagger.

* Validações usando Spring Validation (não permitir dados nulos ou vazios).

* Testes unitários para os serviços.

## Regras de Negócio

RN1: Não permitir adicionar um contato com os mesmos dados de outro contato existente.

RN2: Não permitir adicionar um telefone duplicado para o mesmo contato.

RN3: Não permitir adicionar um endereço duplicado para o mesmo contato.

## 🛠️ Pré-requisitos

* Docker e Docker Compose (para o banco de dados PostgreSQL)

* Java 17+

* Maven 3.8+
