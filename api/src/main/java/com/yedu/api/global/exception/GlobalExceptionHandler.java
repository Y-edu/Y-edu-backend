package com.yedu.api.global.exception;

import com.yedu.api.global.exception.teacher.InActiveTeacherException;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {
  private static final String LOG_FORMAT = "Code : {}, Message : {}";

  @ExceptionHandler(InActiveTeacherException.class)
  public ResponseEntity handleInActiveException(InActiveTeacherException ex) {
    log.error(LOG_FORMAT, ex.getCode(), ex.getStackTrace());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getCode());
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity handleApplicationException(ApplicationException ex) {
    log.error(LOG_FORMAT, ex.getCode(), ex.getMessage());
    return ResponseEntity.internalServerError().body(ex.getCode());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleInternalServerException(Exception ex) {
    // 스택트레이스 상 ResourceHttpRequestHandler에서 발생한 경우
    if (ex.getStackTrace() != null) {
      for (StackTraceElement element : ex.getStackTrace()) {
        if (element.getClassName().contains("ResourceHttpRequestHandler")) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 정적 리소스 요청입니다.");
        }
      }
    }
    log.error(LOG_FORMAT, "500", ex.getStackTrace());
    log.error("errorMessage : {}", ex.getMessage());
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<Map<String, String>>> handleValidationException(
      MethodArgumentNotValidException ex) {
    List<Map<String, String>> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()))
            .toList();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
