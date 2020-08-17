package com.study.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description:
 */
public class HttpUtils {

    public static String getBodyString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String getIp(HttpServletRequest request) {
        String ip = getIpRaw(request);
        return ip != null && ip.indexOf(",") > 0 ? ip.split(",")[0].trim() : ip;
    }

    private static String getIpRaw(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        } else {
            ip = request.getHeader("X-Real-IP");
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            } else {
                ip = request.getHeader("Proxy-Client-IP");
                if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                    return ip;
                } else {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                        return ip;
                    } else {
                        ip = request.getRemoteAddr();
                        return ip == null && ip.length() == 0 ? null : ip;
                    }
                }
            }
        }
    }

    public static String getCookie(HttpServletRequest request) {
        return request.getHeader("Cookie");
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    public static String getParam(HttpServletRequest request) {
        try {
            StringBuffer ret = new StringBuffer();
            Iterator<Map.Entry<String, String[]>> iterator = request.getParameterMap().entrySet().iterator();
            if (iterator.hasNext()) {
                appendParam(ret, iterator.next());
            }
            while (iterator.hasNext()) {
                ret.append('&');
                appendParam(ret, iterator.next());
            }
            return ret.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void appendParam(StringBuffer ret, Map.Entry<String, String[]> entry) throws UnsupportedEncodingException {
        String name = entry.getKey();
        ret.append(name).append("=");
        appendParamValue(ret, entry.getValue());
    }

    private static void appendParamValue(StringBuffer ret, String[] value) throws UnsupportedEncodingException {
        if (value != null && value.length > 0) {
            ret.append(URLEncoder.encode(value[0], "UTF-8"));
            for (int i = 1; i < value.length; i++) {
                ret.append(',').append(URLEncoder.encode(value[i], "UTF-8"));
            }
        }
    }
}
