// Copyright 2021-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private long timestamp;
    private String requestId;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<PropertyMessage> propertyMessages;

    public List<PropertyMessage> getPropertyMessages() {
        if (propertyMessages == null) {
            propertyMessages = new ArrayList<>();
        }
        return propertyMessages;
    }
}