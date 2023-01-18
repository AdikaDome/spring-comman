// Copyright 2017-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.util;

import com.nextgen.pxp.spring.common.web.exception.MissingRequestIdException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class RequestIdGenerator {

    public static final String REQUEST_ID = "requestId";

    public static final String MF_CONTEXT = "MF_CONTEXT";

    public static String generateRequestID() {
        return StringUtils.remove(UUID.randomUUID().toString(), "-");
    }

    public static String getRequestIdFromMDC() {
        String requestId = ThreadContext.get(RequestIdGenerator.REQUEST_ID);
        if (StringUtils.isBlank(requestId)) {
            throw new MissingRequestIdException();
        }
        return requestId;
    }

    public static String getRequestIdFromRequest(HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID);
        if (StringUtils.isBlank(requestId)) {
            requestId = RequestIdGenerator.generateRequestID();
        }
        return requestId;
    }
}