// Copyright 2018-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.filter;

import com.nextgen.pxp.spring.common.web.util.RequestIdGenerator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * This filter manages the MDC (MDC is ThreadContext in Log4j2) and adds a unique identifier to the MF_CONTEXT key in the MDC called requestId. It should manage adding any generic header
 * information as well
 * </p>
 * <p>
 * It is assumed this filter is first in the chain and will be the first/last to access MDC
 * </p>
 *
 * Note: This class is similar to LoggingFilter in mfss-common and should log the same info
 */
@Component
@Log4j2
public class LoggingFilter implements Filter, Ordered {

	private final int order;

	@Autowired
	public LoggingFilter(@Value("${logging.filter.order:-101}") int order) {
		this.order = order;
	}

	@Override
	public void init(final FilterConfig arg0) {
		// do nothing
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) {
		// everything happens in this try block to ensure we clear the MDC at the end of the request
		try {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			final HttpServletResponse httpResponse = (HttpServletResponse) response;

			// build up string to add to MDC
			final StringBuilder contextString = new StringBuilder(RequestIdGenerator.REQUEST_ID).append("=");
			final String requestId = RequestIdGenerator.getRequestIdFromRequest(httpRequest);
			contextString.append(requestId);

			// add username if it is passed in a basic auth header
			try {
				if (httpRequest.getUserPrincipal() != null) {
					final String username = httpRequest.getUserPrincipal().getName();
					if (StringUtils.isNotBlank(username)) {
						contextString.append(", username=").append(username);
					}
				}
			} catch (Exception e) {
				log.error("Error adding username to logs", e);
			}

			// call override-able method to add app specific data
			try {
				final String appContext = addApplicationData(request);
				if (StringUtils.isNotBlank(appContext)) {
					contextString.append(", ").append(appContext);
				}
			} catch (Exception e) {
				log.error("Error adding application data", e);
			}

			setMdcProperties(contextString.toString(), requestId);

			// log start for timing metrics
			long startTime = System.currentTimeMillis();
			log.info("Started processing incoming REST call from remoteHost={} remoteAddr={} method={} url={}", httpRequest.getRemoteHost(),
					httpRequest.getRemoteAddr(), httpRequest.getMethod(), httpRequest.getRequestURL());

			// continue processing the request
			String errorResponseBody = doChainFilterAndGetErrorResponseIfNeeded(request, httpResponse, chain);

			// log end for timing metrics
			long duration = System.currentTimeMillis() - startTime;
			log.info("Completed processing incoming REST call from remoteHost={} remoteAddr={} method={} url={} responseStatus={} duration={} s{}",
					httpRequest.getRemoteHost(), httpRequest.getRemoteAddr(), httpRequest.getMethod(), httpRequest.getRequestURL(), httpResponse.getStatus(),
					DurationFormatUtils.formatDuration(duration, "s.S"), errorResponseBody);
		} catch (Exception e) {
			// shouldn't ever get here if exception mappers are set up
			log.warn("Request completed with exception", e);
		} finally {
			// always clear the MDC
			ThreadContext.clearAll();
		}
	}

	private void setMdcProperties(String contextString, String requestId) {
		// MF_CONTEXT is meant to be a single property that contains all NextGen specific contextual data put in the MDC
		ThreadContext.put(RequestIdGenerator.MF_CONTEXT, contextString);

		// set the requestId directly in the MDC
		ThreadContext.put(RequestIdGenerator.REQUEST_ID, requestId);
	}

	private String doChainFilterAndGetErrorResponseIfNeeded(final ServletRequest request, final HttpServletResponse httpResponse, final FilterChain chain)
			throws IOException, ServletException {
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);
		
		String errorResponseBody = "";
		try {
			chain.doFilter(request, responseWrapper);
		} finally {
			if (isHttpStatusError(httpResponse.getStatus())) {
				byte[] copyOfResponse = responseWrapper.getContentAsByteArray();
				errorResponseBody = " errorResponseBody=" + new String(copyOfResponse, responseWrapper.getCharacterEncoding());
			}
			responseWrapper.copyBodyToResponse();
		}

		return errorResponseBody;
	}

	@Override
	public void destroy() {
		// do nothing
	}

	/**
	 * <p>
	 * This method is intended to be overridden to add application specific data to the MDC context variable.
	 * </p>
	 * <p>
	 * The String returned should be in the form: key1=value1, key2=value2, etc
	 * </p>
	 * 
	 * @param request The servlet request.
	 * @return Always returns null unless the method is overridden.
	 */
	protected String addApplicationData(final ServletRequest request) {
		return null;
	}

	private boolean isHttpStatusError(int status) {
		return status >= 400 && status < 600;
	}
}