package com.company.controller;

import com.company.exception.DuplicatedNameException;
import com.company.exception.EntityNotFoundException;
import com.company.exception.InvalidFieldException;
import com.company.model.Error;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({InvalidFieldException.class, EntityNotFoundException.class, JsonMappingException.class})
    public ResponseEntity<Error> serviceException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(DuplicatedNameException.class)
    public ResponseEntity<Error> duplicatedKeyException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<Error> defaultException(Exception ex) {
        log.error("Well, this is an unexpected behavior, please contact the support team", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<Error> buildResponseEntity(HttpStatus status, Exception ex) {
        Error error = buildError(status.value(), ex);
        return new ResponseEntity<>(error, status);
    }

    private Error buildError(int status, Exception ex) {
        HttpServletRequest request =    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String baseUrl = request.getRequestURL().substring(0,request.getRequestURL().indexOf(request.getContextPath())+request.getContextPath().length());
        return Error.builder()
                .status(status)
                .message(ex.getMessage())
                .urlDocumentation(baseUrl.toString() + "/swagger-ui.html")
                .build();
    }
}
