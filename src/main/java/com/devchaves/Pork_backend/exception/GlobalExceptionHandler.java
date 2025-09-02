package com.devchaves.Pork_backend.exception;

import com.devchaves.Pork_backend.DTO.ErrorResponseDTO;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error("Parâmetros inválidos foram fornecidos.", ex);
        ErrorResponseDTO response = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "Parâmetros Inválidos",
            "Parâmetros inválidos, verifique os parâmetros enviados!"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        logger.error("Erro de validação nos argumentos do método.", e);
        List<String> erros = e.getBindingResult().getFieldErrors()
                .stream().map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();

        String errorMessage = "Erro de validação: " + String.join(", ", erros);

        ErrorResponseDTO response = new ErrorResponseDTO(
          HttpStatus.BAD_REQUEST.value(),
          "Erro de validação",
          errorMessage
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalState(IllegalStateException e){
        logger.error("Estado ilegal encontrado durante o processamento.", e);
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflito na regra de negócio",
                e.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFound(UsernameNotFoundException ex){
        logger.error("Tentativa de acesso com usuário não encontrado.", ex);
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Usuário não encontrado",
                "Usuário não encontrado, verifique os parâmetros"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Ocorreu uma exceção não tratada.", ex);
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro no processo",
                "Ocorreu um erro inesperado no processamento da sua solicitação. Tente novamente mais tarde."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NoHandlerFoundException e){
        logger.warn("Nenhum manipulador encontrado para a requisição: {}", e.getRequestURL());
        String message = "Recurso não encontrado: A rota " + e.getRequestURL() + " não existe.";

        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Rota não encontrada",
                message
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException e){
        logger.warn("Falha na autenticação.", e);
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Autênticação Falhou",
                "Falha na autênticação. Verifique suas credênciais"
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException e){
        logger.warn("Acesso negado.", e);
        ErrorResponseDTO response = new ErrorResponseDTO(
            HttpStatus.FORBIDDEN.value(),
            "Acesso negado",
            "Você não tem permissão para acessar esse recurso."
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Não foi possível ler a mensagem HTTP.", ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Malformada",
                "O corpo da requisição não pôde ser lido. Verifique se o JSON está bem formatado e se os nomes dos campos estão corretos."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Violação de integridade dos dados.", ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflito de Dados",
                "Ocorreu um erro ao salvar os dados, pois eles violam uma regra do banco de dados (por exemplo, um campo obrigatório está faltando ou um valor único está sendo duplicado)."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
