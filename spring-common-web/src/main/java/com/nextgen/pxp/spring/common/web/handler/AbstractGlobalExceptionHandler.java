// Copyright 2021-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextgen.pxp.spring.common.web.util.RequestIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

public class AbstractGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${set.error.response.message.with.third.party.json.error:false}")
    private boolean useNotParsableJsonError;

    private final ObjectMapper mapper;

    public AbstractGlobalExceptionHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse er;
        if (body instanceof ErrorResponse) {
            er = (ErrorResponse) body;
        } else {
            er = initErrorResponse(status, (ServletWebRequest) request);
        }

        if (ex instanceof HttpStatusCodeException) {
            processMessage((HttpStatusCodeException) ex, er);
        } else if (ex instanceof MethodArgumentNotValidException) {
            processMessage((MethodArgumentNotValidException) ex, er);
        } else {
            er.setMessage(ex.getMessage());
        }
        return super.handleExceptionInternal(ex, er, headers, status, request);
    }

    protected ErrorResponse initErrorResponse(HttpStatus status, ServletWebRequest request) {
        ErrorResponse er = new ErrorResponse();
        er.setStatus(status.value());
        er.setPath(request.getRequest().getRequestURL().toString());
        er.setTimestamp(new Date().getTime());
        er.setError(status.getReasonPhrase());
        er.setRequestId(ThreadContext.get(RequestIdGenerator.REQUEST_ID));
        return er;
    }

    private void processMessage(HttpStatusCodeException sce, ErrorResponse er) {
        String body = sce.getResponseBodyAsString();
        if (StringUtils.isBlank(body)) {
            er.setMessage(sce.getStatusText());
            return;
        }

        if (useNotParsableJsonError) {
            er.setMessage(body);
        }
        try {
            ErrorResponse incomingEr = mapper.readValue(body, ErrorResponse.class);
            if (incomingEr != null && StringUtils.isNotBlank(incomingEr.getMessage())) {
                er.setMessage(incomingEr.getMessage());
            }
        } catch (Exception ignored) {
        }
    }

    private void processMessage(MethodArgumentNotValidException ex, ErrorResponse rv) {
        StringBuilder message = new StringBuilder();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getDefaultMessage()).append(", ");
        }
        message = new StringBuilder(message.substring(0, message.lastIndexOf(",")));
        if (StringUtils.isEmpty(message.toString())) {
            message = new StringBuilder("One or more input values were invalid");
        }
        rv.setMessage(message.toString());
    }
}