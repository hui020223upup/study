package com.study.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片工具类
 *
 * @author: whh
 * @date: 2020.07.17
 **/
public class ImageUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 图片链接转为base64
     * 1、将String 的地址 转成成 URL对象
     * 2、打开连接 URL.openConnection();
     * 3、设置连接参数(请求方式、超时时间等)
     * 4、请查看response code 是否为200
     * 5、建立连接  connection.connect(); 由于后面要读取网页内容 .getInputStream()，会自动的连接，所以此处不建立连接也是可以的
     * 6、读取内容  connection.getInputStream(); 返回值式 InputStream
     * 7、将 inputStream 的信息写道  ByteArrayOutputStream 中 (使用 InputStream.read()读取 使用 ByteArrayOutputStream.write(byte[], start, end) 写入)】
     * 8、使用BASE64Encoder.encode(byte[]) 输出 String 这里的String 就是编码后的base64 字符串
     */
    public static String url2Base64(String imageUrl) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10 * 1000);
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            int len = -1;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
        } catch (Exception e) {
            log.error("url2Base64 error:imageUrl={}", imageUrl, e);
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String result = encoder.encode(outputStream.toByteArray());
        return result.replaceAll("\r|\n", "");
    }
}
