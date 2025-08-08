# Pork Backend

[![Ask DeepWiki](https://devin.ai/assets/askdeepwiki.png)](https://deepwiki.com/dev-Chaves/Pork-Backend)

Pork é o serviço backend para uma aplicação SaaS de finanças pessoais. Ele fornece uma API robusta para gerenciar autenticação de usuários, rastrear receitas e despesas, definir metas financeiras e obter sugestões de investimentos. Construído com Java e o framework Spring Boot, foi projetado para ser escalável e de fácil manutenção.

## ✨ Funcionalidades

*   **Gerenciamento de Usuários**: Registro seguro de usuários com verificação de e-mail e autenticação baseada em JWT para login.
*   **Rastreamento de Despesas e Receitas**: Operações CRUD para despesas fixas e variáveis. Registre e atualize a renda mensal.
*   **Painel Financeiro**: Obtenha uma visão consolidada de todas as despesas, categorizadas em fixas e variáveis, juntamente com o total de gastos.
*   **Definição de Metas**: Crie, atualize, visualize e exclua metas financeiras.
*   **Sugestões de Investimento**: Usuários podem selecionar um perfil de investimento (Fácil, Médio, Difícil) para receber sugestões sobre quanto de sua renda investir.
*   **Documentação da API**: Swagger UI integrado para fácil exploração e teste da API.

## 🛠️ Stack Tecnológica

*   **Backend**: Java 21, Spring Boot 3
*   **Banco de Dados**: PostgreSQL
*   **Cache**: Redis 
*   **Autenticação**: Spring Security, JSON Web Tokens (JWT), Cookie
*   **ORM e Migrações**: Spring Data JPA (Hibernate), Flyway
*   **Containerização**: Docker, Docker Compose
*   **Proxy Reverso**: Nginx
*   **Ferramenta de Build**: Maven
*   **Teste de API**: Bruno

## 🚀 Começando

Para executar a aplicação em sua máquina local, siga estes passos.

### Pré-requisitos

*   [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
*   [Maven](https://maven.apache.org/download.cgi)
*   [Docker](https://docs.docker.com/get-docker/)
*   [Docker Compose](https://docs.docker.com/compose/install/)

### Instalação e Configuração

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/dev-chaves/Pork-Backend.git
    cd Pork-Backend
    ```

2.  **Crie o arquivo de ambiente:**
    Copie o arquivo de exemplo de ambiente `.env.example` para um novo arquivo chamado `.env`.
    ```sh
    cp .env.example .env
    ```

3.  **Configure suas variáveis de ambiente** no arquivo `.env`. Você precisará fornecer credenciais para seu banco de dados PostgreSQL e um segredo para geração de JWT.

    ```dotenv
    # Banco de Dados PostgreSQL
    POSTGRESQL_USER=seu_usuario_postgres
    POSTGRESQL_PASSWORD=sua_senha_postgres
    POSTGRESQL_DATABASE=pork_db

    # Java Mail Sender (para verificação de e-mail)
    SPRING_MAIL_USERNAME=seu_usuario_gmail@gmail.com
    SPRING_MAIL_PASSWORD=sua_senha_de_app_gmail

    # Segredo JWT
    SECRET=sua_chave_jwt_super_secreta
    ```

4.  **Construa e execute a aplicação usando Docker Compose:**
    Este comando irá construir as imagens Docker para a aplicação e Nginx, e iniciar os containers para o app, o banco de dados e o proxy reverso.
    ```sh
    docker-compose up --build -d
    ```

A aplicação estará acessível em `http://localhost:8080`.

## 📖 Uso da API

A API está documentada usando OpenAPI (Swagger). Uma vez que a aplicação esteja em execução, você pode acessar o Swagger UI para explorar e testar os endpoints:

*   **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`

### Teste da API com Bruno

Este repositório inclui uma coleção [Bruno](https://www.usebruno.com/) no diretório `Pork/`. Você pode importar esta coleção em seu cliente Bruno para testar facilmente os endpoints da API.

## 🏗️ Estrutura do Projeto

*   `src/main/java`: Contém o código-fonte principal da aplicação, organizado por funcionalidade (controller, service, repository, DTO, etc.).
*   `src/main/resources`: Contém arquivos de configuração (`application.yaml`), migrações de banco de dados e configuração do Nginx.
*   `src/main/resources/db/migration`: Scripts de migração SQL do Flyway para configurar o esquema do banco de dados.
*   `Dockerfile`: Define o processo de construção para o container da aplicação Spring Boot.
*   `Dockerfile.nginx`: Define a construção para o container do proxy reverso Nginx.
*   `compose.yaml`: Arquivo Docker Compose para orquestrar os serviços da aplicação, banco de dados e Nginx.
*   `pom.xml`: Arquivo de projeto Maven definindo as dependências do projeto e configuração de build.
*   `Pork/`: Contém a coleção de API Bruno para testes fáceis.

## 🔄 Pipeline CI/CD

Este projeto usa GitHub Actions para integração e implantação contínuas. O workflow está definido em `.github/workflows/pipeline.yaml` e executa os seguintes passos a cada push para o branch `main`:

1.  **Checkout do Código**: Faz o checkout da versão mais recente do repositório.
2.  **Login no DockerHub**: Autentica com o DockerHub usando secrets.
3.  **Construir e Enviar Imagens**:
    *   Constrói a imagem Docker para a aplicação Java (`pork:latest`) e envia para o DockerHub.
    *   Constrói a imagem Docker para o Nginx (`pork-nginx:latest`) e envia para o DockerHub.
4.  **Implantar no VPS**:
    *   Copia com segurança o arquivo `compose.yaml` para um servidor privado virtual (VPS) de produção.
    *   Conecta ao VPS via SSH.
    *   Baixa as imagens mais recentes do DockerHub.
    *   Para os serviços em execução atuais e inicia a nova versão usando `docker-compose up`.
