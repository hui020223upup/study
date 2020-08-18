package com.study.interceptor;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author: whh
 * @date: 2020.08.18
 **/
public class LogContextHolder {

    private static final String LOG_CONTEXT_REQUEST_BEGIN_TIME = "LOG_CONTEXT_REQUEST_BEGIN_TIME";

    private static final String LOG_CONTEXT_REQUEST_BODY = "LOG_CONTEXT_REQUEST_BODY";

    private static final String LOG_CONTEXT_RESPONSE_BODY = "LOG_CONTEXT_RESPONSE_BODY";

    public static void setRequestBody(Object context) {
        RequestContextHolder.currentRequestAttributes().setAttribute(
                LOG_CONTEXT_REQUEST_BODY,
                context,
                RequestAttributes.SCOPE_REQUEST);
    }

    public static Object getRequestBody() {
        return RequestContextHolder.currentRequestAttributes()
                .getAttribute(LOG_CONTEXT_REQUEST_BODY, RequestAttributes.SCOPE_REQUEST);
    }

    public static void setResponseBody(Object context) {
        RequestContextHolder.currentRequestAttributes().setAttribute(
                LOG_CONTEXT_RESPONSE_BODY,
                context,
                RequestAttributes.SCOPE_REQUEST);
    }

    public static Object getResponseBody() {
        return RequestContextHolder.currentRequestAttributes()
                .getAttribute(LOG_CONTEXT_RESPONSE_BODY, RequestAttributes.SCOPE_REQUEST);
    }

    public static void setBeginTime(Object context) {
        RequestContextHolder.currentRequestAttributes().setAttribute(
                LOG_CONTEXT_REQUEST_BEGIN_TIME,
                context,
                RequestAttributes.SCOPE_REQUEST);
    }

    public static Object getBeginTime() {
        return RequestContextHolder.currentRequestAttributes()
                .getAttribute(LOG_CONTEXT_REQUEST_BEGIN_TIME, RequestAttributes.SCOPE_REQUEST);
    }
}
