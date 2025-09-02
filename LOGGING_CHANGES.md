# Documentação de Alterações de Logging

Este documento detalha as adições de logging estratégico feitas na aplicação para melhorar a observabilidade e facilitar a depuração.

## 1. GlobalExceptionHandler

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/exception/GlobalExceptionHandler.java`
- **Alterações:**
    - Adicionado um `Logger` SLF4J.
    - Todos os métodos `@ExceptionHandler` agora registram a exceção ocorrida com nível `ERROR` ou `WARN`, dependendo da severidade, antes de retornar a resposta HTTP. Isso garante que todas as exceções tratadas globalmente sejam registradas.

## 2. UserService

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/services/UserService.java`
- **Alterações:**
    - **`register`**: Adicionado log no início do processo, em caso de e-mail inválido ou já existente, e no sucesso do registro e envio de e-mail.
    - **`login`**: Adicionado log para tentativas de login, falhas (e-mail inválido, usuário não verificado, senha incorreta) e sucesso na autenticação.
    - **`reenviarVerificacao`**: Adicionado log para solicitações de reenvio, casos de usuário já verificado e sucesso no envio do novo link.
    - **`enviarEmaiLRedefenirSenha`**: Adicionado log para o início da solicitação e para o envio do e-mail de redefinição.
    - **`redefinirSenha`**: Adicionado log para a tentativa de redefinição, falha por token inválido ou senhas não coincidentes, e sucesso na alteração da senha.
    - **`consultarInfo`**: Adicionado log para rastrear a consulta de informações do usuário.

## 3. ExpensesService

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/services/ExpensesService.java`
- **Alterações:**
    - **`cadastrarDespesas`**: Adicionado log para o início do cadastro, validações (usuário não verificado, valor negativo) e sucesso na criação das despesas.
    - **`atualizarDespesa`**: Adicionado log para o início da atualização, falha por despesa não encontrada e sucesso na alteração.
    - **`apagarDespesa`**: Adicionado log para o início da exclusão, tentativa de apagar despesa inexistente e sucesso na remoção.
    - **`atualizarReceita`**: Adicionado log para o início da atualização da receita, validação de valor negativo e sucesso na alteração.
    - Logs adicionados aos métodos de consulta (`consultarDespesas`, `consultarDespesasInfo`, etc.) para monitorar o acesso aos dados.

## 4. InvestmentService

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/services/InvestmentService.java`
- **Alterações:**
    - **`selecionarInvestimento`**: Adicionado log para registrar a escolha do tipo de investimento feita pelo usuário.
    - **`calcularInvestimentos`**: Adicionado log para registrar o início do cálculo e o resultado com base no perfil do usuário (HARD, MID, EASY).

## 5. MetasService

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/services/MetasService.java`
- **Alterações:**
    - **`cadastrarMetas`**: Adicionado log para o início do cadastro e para o sucesso da operação.
    - **`alterarMeta`**: Adicionado log para o início da alteração, falha por meta não encontrada e sucesso na atualização.
    - **`apagarMeta`**: Adicionado log para o início da exclusão, falha por meta não encontrada e sucesso na remoção.
    - **`consultarMetas` e `consultarMetasPaginadas`**: Adicionado log para monitorar a consulta de metas.

## 6. MailService

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/services/MailService.java`
- **Alterações:**
    - **`sendEmailToRegister`**: Adicionado log para o início do envio do e-mail, sucesso no envio e falha (capturando a exceção).

## 7. TokenService

- **Arquivo:** `src/main/java/com/devchaves/Pork_backend/services/TokenService.java`
- **Alterações:**
    - **`verificarToken`**: Adicionado log para o processo de verificação do token de e-mail, incluindo falhas por token inválido ou expirado e sucesso na verificação.
    - **`generateToken`**: Adicionado log para a geração de tokens JWT.
    - **`estaValido`**: Adicionado log para o processo de validação de tokens JWT, registrando sucessos e falhas.
