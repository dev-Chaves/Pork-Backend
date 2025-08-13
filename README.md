# Pork-Backend

[![Ask DeepWiki](https://devin.ai/assets/askdeepwiki.png)](https://deepwiki.com/dev-Chaves/Pork-Backend)

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-blue.svg)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![Nginx](https://img.shields.io/badge/Nginx-green.svg)](https://www.nginx.com/)

Pork-Backend é o serviço de back-end para uma aplicação SaaS de finanças pessoais. Ele fornece uma API robusta para gerenciar autenticação de usuários, rastrear receitas e despesas, definir metas financeiras e obter sugestões de investimentos. Construído com Java e o framework Spring Boot, foi projetado para ser escalável e de fácil manutenção.

## ✨ Funcionalidades Principais

*   **Autenticação e Gerenciamento de Usuários**: Registro seguro com verificação de e-mail, login via JWT e gerenciamento de perfil.
*   **Rastreamento de Transações**: Operações CRUD para despesas e receitas.
*   **Gerenciamento de Investimentos**: Rastreie diferentes tipos de investimentos.
*   **Definição de Metas Financeiras**: Crie, atualize e acompanhe metas financeiras.
*   **Dashboard Financeiro**: Obtenha uma visão consolidada da saúde financeira do usuário.
*   **Segurança**: Autenticação baseada em JWT, redefinição de senha segura e validação de e-mail.

## 🛠️ Stack Tecnológica

*   **Backend**: Java 21, Spring Boot 3, Spring Security
*   **Banco de Dados**: PostgreSQL
*   **Cache**: Redis
*   **ORM e Migrações**: Spring Data JPA (Hibernate), Flyway
*   **Autenticação**: JSON Web Tokens (JWT)
*   **Containerização**: Docker, Docker Compose
*   **Proxy Reverso**: Nginx
*   **CI/CD**: GitHub Actions
*   **Ferramenta de Build**: Maven

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas clássica, promovendo a separação de responsabilidades e a manutenibilidade:

*   **Controller**: Expõe a API REST para o cliente. Responsável por receber requisições HTTP e retornar respostas.
*   **Service**: Contém a lógica de negócios principal da aplicação. Orquestra as operações entre os repositórios e outros serviços.
*   **Repository**: Camada de acesso a dados, utilizando Spring Data JPA para interagir com o banco de dados.
*   **Entity**: Classes que mapeiam as tabelas do banco de dados (modelo de domínio).
*   **DTO (Data Transfer Object)**: Objetos que carregam dados entre as camadas e para o cliente, evitando a exposição das entidades internas.
*   **Config**: Classes de configuração do Spring para segurança, CORS, Redis, etc.

## 🚀 Como Executar o Projeto

Existem duas maneiras de executar a aplicação: via Docker Compose (recomendado para um ambiente de produção simulado) ou localmente via Maven (ideal para desenvolvimento).

### 1. Usando Docker Compose (Recomendado)

Este método orquestra todos os serviços necessários: a aplicação, o banco de dados, o Redis e o Nginx.

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/dev-chaves/Pork-Backend.git
    cd Pork-Backend
    ```

2.  **Crie e configure o arquivo de ambiente:**
    Copie `.env.example` para `.env` e preencha as variáveis (credenciais do banco de dados, segredo JWT, etc.).
    ```sh
    cp .env.example .env
    ```

3.  **Construa e inicie os contêineres:**
    ```sh
    docker-compose up --build -d
    ```
    A aplicação estará disponível na porta configurada no Nginx (geralmente `http://localhost`).

### 2. Executando Localmente com Maven

Este método requer que você tenha o Java, Maven, PostgreSQL e Redis instalados e em execução em sua máquina.

1.  **Pré-requisitos**: JDK 21+, Maven, PostgreSQL, Redis.

2.  **Configure a Aplicação**:
    Configure as variáveis de ambiente em sua IDE ou sistema operacional, ou modifique o arquivo `src/main/resources/application.yaml` para apontar para seus serviços locais.

3.  **Execute a aplicação:**
    Use o Maven Wrapper para iniciar o servidor.
    ```sh
    ./mvnw spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080` (ou na porta configurada).

## 📖 Endpoints da API

A API está organizada em torno de recursos RESTful.

*   **Autenticação (`/auth`)**
    *   `POST /login`: Autentica um usuário e retorna um token JWT.
    *   `POST /register`: Registra um novo usuário e envia um e-mail de verificação.
    *   `POST /resend-email`: Reenvia o e-mail de verificação.
    *   `POST /change-password`: Permite que um usuário autenticado altere sua senha.
    *   `POST /forgot-password`: Inicia o fluxo de recuperação de senha.

*   **Usuário (`/user`)**
    *   `GET /info`: Retorna informações do usuário autenticado.
    *   `PUT /update`: Atualiza as informações do perfil do usuário.
    *   `GET /dashboard`: Retorna dados consolidados para o painel financeiro.

*   **Despesas e Receitas (`/expense`)**
    *   `POST /`: Cria uma nova despesa ou receita.
    *   `GET /`: Lista todas as transações do usuário, com suporte a filtros.
    *   `DELETE /{id}`: Exclui uma transação.

*   **Investimentos (`/investment`)**
    *   `POST /`: Adiciona um novo registro de investimento.
    *   `GET /`: Lista os investimentos do usuário.
    *   `GET /methods`: Retorna os tipos de investimentos disponíveis.

*   **Metas (`/metas`)**
    *   `POST /`: Cria uma nova meta financeira.
    *   `GET /`: Lista todas as metas do usuário.
    *   `PUT /{id}`: Atualiza uma meta existente.
    *   `DELETE /{id}`: Exclui uma meta.

## 🔄 Pipeline CI/CD

Este projeto usa GitHub Actions para integração e implantação contínuas. O workflow, definido em `.github/workflows/pipeline.yaml`, automatiza o build, o push das imagens Docker para o DockerHub e a implantação em um servidor de produção a cada push para o branch `main`.