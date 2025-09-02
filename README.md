# Pork - Backend

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-blue.svg)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![Nginx](https://img.shields.io/badge/Nginx-green.svg)](https://www.nginx.com/)
[![CI/CD](https://github.com/dev-Chaves/Pork-Backend/actions/workflows/pipeline.yaml/badge.svg)](https://github.com/dev-Chaves/Pork-Backend/actions/workflows/pipeline.yaml)

Pork-Backend é o serviço de back-end para uma aplicação SaaS de finanças pessoais. Ele fornece uma API robusta para gerenciar autenticação de usuários, rastrear receitas e despesas, definir metas financeiras e obter sugestões de investimentos. Construído com Java e o framework Spring Boot, foi projetado para ser escalável, seguro e de fácil manutenção.

## ✨ Funcionalidades Principais

*   **Autenticação e Gerenciamento de Usuários**: Registro seguro com verificação de e-mail, login via JWT, redefinição de senha e gerenciamento de perfil.
*   **Rastreamento de Despesas**: Operações CRUD completas para despesas, com categorização e consultas por período.
*   **Gerenciamento de Receita**: Definição e atualização da receita mensal do usuário.
*   **Gerenciamento de Investimentos**: Permite ao usuário selecionar um perfil de investimento (fácil, médio, difícil) e calcula o valor a ser investido com base na receita.
*   **Definição de Metas Financeiras**: Crie, atualize, liste e apague metas financeiras de curto e longo prazo.
*   **Dashboard Financeiro**: Endpoint consolidado para obter uma visão geral da saúde financeira do usuário.
*   **Segurança**: Autenticação baseada em JWT, validação de e-mail, e tratamento de exceções padronizado.
*   **Observabilidade**: Logs estratégicos em toda a aplicação para rastreamento de fluxo e depuração de erros.

## 🛠️ Stack Tecnológica

*   **Backend**: Java 21, Spring Boot 3.3, Spring Security
*   **Banco de Dados**: PostgreSQL
*   **Cache**: Redis para cache de dados de sessão e consultas frequentes.
*   **ORM e Migrações**: Spring Data JPA (Hibernate) e Flyway para gerenciamento de schema do banco de dados.
*   **Autenticação**: JSON Web Tokens (JWT) com a biblioteca `java-jwt` da Auth0.
*   **Containerização**: Docker e Docker Compose.
*   **Proxy Reverso**: Nginx para balanceamento de carga e terminação SSL.
*   **CI/CD**: GitHub Actions para automação de build, teste e deploy.
*   **Ferramenta de Build**: Maven.
*   **Documentação da API**: SpringDoc (OpenAPI/Swagger).

## 🚀 Como Executar o Projeto

### Pré-requisitos

*   JDK 21 ou superior
*   Maven 3.8+
*   Docker e Docker Compose
*   Um cliente de API como Postman ou Insomnia.

### 1. Usando Docker Compose (Recomendado)

Este método orquestra todos os serviços necessários: a aplicação, o banco de dados PostgreSQL e o Redis.

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/dev-chaves/Pork-Backend.git
    cd Pork-Backend
    ```

2.  **Crie e configure o arquivo de ambiente:**
    Copie o arquivo de exemplo `.env.example` para `.env` e preencha todas as variáveis de ambiente. Elas são essenciais para a conexão com o banco de dados, o segredo do JWT, as credenciais de e-mail e as URLs da aplicação.
    ```sh
    cp .env.example .env
    ```

3.  **Construa e inicie os contêineres:**
    ```sh
    docker-compose up --build -d
    ```
    A aplicação estará disponível em `http://localhost:8080`.

### 2. Executando Localmente com Maven

Este método é ideal para desenvolvimento e depuração.

1.  **Inicie os serviços de dependência**:
    Certifique-se de que você tem instâncias do PostgreSQL e Redis rodando localmente e acessíveis com as credenciais fornecidas nas configurações.

2.  **Configure a Aplicação**:
    Modifique o arquivo `src/main/resources/application.yaml` para apontar para seus serviços locais de banco de dados e Redis.

3.  **Execute a aplicação:**
    Use o Maven Wrapper para compilar e iniciar o servidor.
    ```sh
    ./mvnw spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080`.

## 📖 Endpoints da API

Após iniciar a aplicação, a documentação completa da API gerada pelo Swagger UI pode ser acessada em:
**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Resumo dos Endpoints:

*   **Autenticação (`/auth`)**
    *   `POST /login`: Autentica um usuário e retorna um token JWT.
    *   `POST /register`: Registra um novo usuário e envia um e-mail de verificação.
    *   `POST /resend-email`: Reenvia o e-mail de verificação.
    *   `POST /forgot-password`: Envia um e-mail com link para redefinição de senha.
    *   `POST /redefine-password`: Redefine a senha usando um token válido.
    *   `GET /verify-email`: Endpoint para o qual o link de verificação de e-mail aponta.

*   **Usuário (`/user`)**
    *   `GET /info`: Retorna informações do usuário autenticado.
    *   `PUT /update-receita`: Atualiza a receita do usuário.

*   **Despesas (`/expense`)**
    *   `POST /`: Cadastra uma ou mais despesas.
    *   `GET /`: Lista todas as despesas do usuário (paginado).
    *   `GET /dashboard`: Retorna dados consolidados para o painel financeiro.
    *   `PUT /{id}`: Atualiza uma despesa existente.
    *   `DELETE /{id}`: Exclui uma despesa.

*   **Investimentos (`/investment`)**
    *   `POST /select-investment`: Define o perfil de investimento do usuário.
    *   `GET /calculate`: Calcula o valor de investimento sugerido com base no perfil.

*   **Metas (`/metas`)**
    *   `POST /`: Cadastra uma ou mais metas financeiras.
    *   `GET /`: Lista todas as metas do usuário (paginado).
    *   `PUT /{id}`: Atualiza uma meta existente.
    *   `DELETE /{id}`: Exclui uma meta.

## 🔄 Pipeline CI/CD

O projeto utiliza GitHub Actions para integração e implantação contínuas. O workflow, definido em `.github/workflows/pipeline.yaml`, automatiza os seguintes passos a cada push para o branch `main`:
1.  **Checkout do código**.
2.  **Setup do JDK 21**.
3.  **Build com Maven**.
4.  **Login no DockerHub**.
5.  **Build e Push da imagem Docker**.
