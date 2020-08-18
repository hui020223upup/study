### 1、问题提出
在问题排查时发现接口参数为@RequestBody类型的接口无法打印出请求参数，由于RequsetBody参数不会放在HttpServletRequest的Map中，因此没法通过javax.servlet.ServletRequest#getParameter获取，如果自己实现参数打印， 则需要从reqeust.getInputStream中获取JSON内容， 但是由于流只能读取一次， 所以会导致后续Spring MVC解析参数异常。

### 2、方案调研
方案一：HttpRequestWrapper+Filter，使用HttpRequestWrapper重新封装Reqeust，使用Filter将读完之后的流重新写进去，Spring MVC 需要再次读取，代码不够优雅，而且可能有些body比较大或者更特殊的body不好处理；

方案二：RequestBodyAdvice+ThreadLocal，RequestBodyAdvice可以获取解析后的@RequestBody参数对象，把其写入ThreadLocal中，在HandlerInterceptor的afterCompletion可以通过ThreadLocal获取@RequestBody参数，并在finally中将其remove，但如果在HandlerInterceptor的preHandle中出现Exception,就不会调用afterCompletion，这就导致了ThreadLocal变量不能被清理，可能造成内存泄漏的问题；

方案三：RequestBodyAdvice+RequestContextHolder，RequestContextHolder背后的原理也是ThreadLocal，不过它总会被更底层的 Servlet 的 RequestContextFilter清理掉，因此不存在泄露的问题。

### 3、方案实现
1、封装RequestContextHolder；
```java
public class LogContextHolder {

    private static final String LOG_CONTEXT_REQUEST_BEGIN_TIME = "LOG_CONTEXT_REQUEST_BEGIN_TIME";

    private static final String LOG_CONTEXT_REQUEST_BODY = "LOG_CONTEXT_REQUEST_BODY";

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

```
2、RequestBodyAdvice参数解析，往RequestContextHolder记录@RequestBody参数对象；
```java
@ControllerAdvice
public class LogRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //判断是否有此注解
        RequestBody requestBody = methodParameter.getParameterAnnotation(RequestBody.class);
        //只有为true时才会执行afterBodyRead
        return requestBody != null;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        LogContextHolder.setRequestBody(body);
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
```
3、HandlerInterceptor打印@RequestBody参数对象;
```java
public class TimedAccessLogInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimedAccessLogInterceptor.class);

    public TimedAccessLogInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogContextHolder.setBeginTime(System.currentTimeMillis());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            Object beginTimeObject = LogContextHolder.getBeginTime();
            long endTime = System.currentTimeMillis();
            long cost = endTime - Long.valueOf(String.valueOf(beginTimeObject));
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            Object paramBodyObject = LogContextHolder.getRequestBody();
            String paramBody = paramBodyObject == null ? null : JSON.toJSONString(paramBodyObject);
            LOGGER.info("method={},uri={},query={},paramBody={},cost={}", method, uri, query, paramBody cost);
        } finally {
        }
    }

}


```