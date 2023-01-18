// Copyright 2017-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends AbstractGlobalExceptionHandler {

    @Autowired
    public GlobalExceptionHandler(ObjectMapper mapper) {
        super(mapper);
    }

    @ExceptionHandler({HttpStatusCodeException.class})
    public ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, ex.getResponseHeaders(), ex.getStatusCode(), request);
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, ResponseEntity.badRequest().build().getHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleBadRequestException(IllegalArgumentException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleCheckedException(Exception ex, WebRequest request, HttpServletResponse response) {
        // clear the contents of the underlying buffer in the response object to prevent
        // appending of response data to errorResponseBody
        response.resetBuffer();
        return handleExceptionInternal(ex, null,
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build().getHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}