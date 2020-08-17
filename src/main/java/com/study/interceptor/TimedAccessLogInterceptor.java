package com.study.interceptor;


import com.alibaba.druid.support.json.JSONUtils;
import com.study.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 日志拦截器
 */
public class TimedAccessLogInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimedAccessLogInterceptor.class);

    private static final ThreadLocal<Long> TL_BEGIN_TIME = new ThreadLocal<>();

    /**
     * save response body
     */
    public static final ThreadLocal responseTL = new ThreadLocal();

    public TimedAccessLogInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TL_BEGIN_TIME.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            long beginTime = TL_BEGIN_TIME.get();
            long endTime = System.currentTimeMillis();
            long cost = endTime - beginTime;

            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String referer = HttpUtils.getReferer(request);
            String param = HttpUtils.getParam(request);
            Object paramBodyObject = RequestBodyContextHolder.get();
            String paramBody = paramBodyObject == null ? null : JSONUtils.toJSONString(paramBodyObject);
            String cookie = HttpUtils.getCookie(request);
            String remoteIp = HttpUtils.getIp(request);

            String sc = String.valueOf(response.getStatus());

            Object responseBodyObject = responseTL.get();
            String responseBody = responseBodyObject == null ? null : JSONUtils.toJSONString(responseBodyObject);
            LOGGER.info("access=access,method={},uri={},referer={},query={},param={},paramBody={},cookie={},remoteIp={},sc={},user={},adminUser={},cost={},responseBody={}",
                    method, uri, referer, query, param, paramBody, cookie, remoteIp, sc, cost, responseBody);
        } finally {
            TL_BEGIN_TIME.remove();
        }
    }

}
