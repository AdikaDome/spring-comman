// Copyright 2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.handler;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PropertyMessage {

    private String message;

    private Map<String, String> properties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
