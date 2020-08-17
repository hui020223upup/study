package com.study.interceptor;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author: whh
 * @date: 2020.08.17
 **/
public class RequestBodyContextHolder {

    private static final String SECURITY_CONTEXT_ATTRIBUTES = "SECURITY_CONTEXT_REQUEST_BODY";

    public static void setContext(Object context) {
        RequestContextHolder.currentRequestAttributes().setAttribute(
                SECURITY_CONTEXT_ATTRIBUTES,
                context,
                RequestAttributes.SCOPE_REQUEST);
    }

    public static Object get() {
        return RequestContextHolder.currentRequestAttributes()
                .getAttribute(SECURITY_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }
}
