# Plano de Melhoria para Captura e Tratamento de Exceções

Este documento descreve uma estratégia para refatorar o tratamento de exceções no projeto `Pork-Backend`, visando um código mais limpo, manutenível e que facilite a depuração de erros.

## Objetivos

1.  **Centralizar o Tratamento de Erros:** Evitar blocos `try-catch` genéricos espalhados pelos controllers.
2.  **Padronizar Respostas de Erro:** Enviar uma resposta JSON consistente para todos os erros da API.
3.  **Criar Exceções Específicas:** Usar exceções customizadas para representar erros de negócio específicos.
4.  **Melhorar o Logging:** Registrar informações detalhadas do erro no servidor para facilitar a depuração, sem expor detalhes sensíveis ao cliente.

---

## Plano de Ação

### 1. Criar uma Estrutura de Resposta de Erro Padrão (DTO)

O primeiro passo é definir um DTO padrão para todas as respostas de erro. Isso garante que o frontend (ou qualquer cliente da API) sempre saiba como interpretar um erro.

**Arquivo a ser criado:** `src/main/java/com/devchaves/Pork_backend/DTO/ErrorResponseDTO.java`

```java
package com.devchaves.Pork_backend.DTO;

import java.time.Instant;

// Usar @JsonInclude(JsonInclude.Include.NON_NULL) para omitir campos nulos
public class ErrorResponseDTO {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Construtores, Getters e Setters
    
    public ErrorResponseDTO(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
```

### 2. Criar Exceções Customizadas para Regras de Negócio

Em vez de usar exceções genéricas como `RuntimeException`, vamos criar exceções específicas para cada cenário de erro. Isso torna o código da camada de serviço mais expressivo.

**Sugestão:** Crie um novo pacote `exception/types` para organizar as exceções.

**Exemplo de arquivo a ser criado:** `src/main/java/com/devchaves/Pork_backend/exception/types/ResourceNotFoundException.java`

```java
package com.devchaves.Pork_backend.exception.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**Outras exceções que podem ser criadas:**
*   `BadRequestException.java` (para validações de negócio que falham)
*   `UserAlreadyExistsException.java` (para tentativa de registro com email duplicado)
*   `InvalidCredentialsException.java` (para falhas de login)

### 3. Aprimorar o `GlobalExceptionHandler`

Este é o ponto central da nossa estratégia. Vamos aprimorar o `GlobalExceptionHandler` para capturar as novas exceções customizadas e outras exceções comuns do Spring, retornando nosso `ErrorResponseDTO` padrão.

**Arquivo a ser melhorado:** `src/main/java/com/devchaves/Pork_backend/exception/GlobalExceptionHandler.java`

```java
package com.devchaves.Pork_backend.exception;

import com.devchaves.Pork_backend.DTO.ErrorResponseDTO;
import com.devchaves.Pork_backend.exception.types.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handler para nossa exceção customizada de "Não Encontrado"
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        logger.warn("Resource not found: {} at path {}", ex.getMessage(), request.getRequestURI());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handler para erros de validação de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        logger.warn("Validation error: {} at path {}", errors, request.getRequestURI());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            errors,
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handler genérico para qualquer outra exceção não tratada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, HttpServletRequest request) {
        // Loga o erro completo no servidor para depuração
        logger.error("Unhandled exception occurred at path {}", request.getRequestURI(), ex);
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocorreu um erro inesperado. Tente novamente mais tarde.", // Mensagem genérica para o cliente
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Adicionar outros handlers conforme necessário para:
    // - AccessDeniedException (falha de autorização)
    // - AuthenticationException (falha de autenticação)
    // - DataIntegrityViolationException (erros de constraint do banco)
}
```

### 4. Refatorar as Camadas de Serviço e Controle

Agora, devemos usar as novas exceções na camada de serviço e remover a lógica de tratamento de erro dos controllers.

**Arquivos a serem melhorados:**
*   `UserService.java`
*   `ExpensesService.java`
*   `MetasService.java`
*   E seus respectivos `Controller`s.

**Exemplo de refatoração no `UserService.java`:**

```java
// ANTES
public UserEntity findById(String id) {
    return userRepository.findById(id).orElse(null); // Retorna nulo, forçando o controller a verificar
}

// DEPOIS
public UserEntity findById(String id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado."));
}
```

Com isso, o `UserController` fica muito mais limpo. Ele não precisa mais verificar se o usuário é nulo. Se o usuário não for encontrado, a exceção `ResourceNotFoundException` será lançada e o `GlobalExceptionHandler` irá capturá-la automaticamente.

### 5. Adicionar Validação nos DTOs

Use as anotações de validação do `jakarta.validation` nos seus DTOs de entrada e a anotação `@Valid` nos métodos do controller. O `GlobalExceptionHandler` que criamos já saberá como tratar os erros de validação.

**Arquivo a ser melhorado:** `src/main/java/com/devchaves/Pork_backend/DTO/RegisterRequestDTO.java`

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String name,
        
        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "Formato de email inválido")
        String email, 
        
        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password
) {}
```

**No `AuthController.java`:**

```java
import jakarta.validation.Valid;

// ...
@PostMapping("/register")
public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO body) {
    // ... o código continua aqui
}
```

Seguindo estes passos, seu sistema de tratamento de exceções se tornará muito mais poderoso, padronizado e fácil de manter.
