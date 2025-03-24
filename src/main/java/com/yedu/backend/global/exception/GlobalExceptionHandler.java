package com.yedu.backend.global.exception;

import com.yedu.backend.global.exception.teacher.InActiveTeacherException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleInternalServerException(Exception ex) {
        log.error(LOG_FORMAT, "500", ex.getStackTrace());
        log.error("errorMessage : {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
