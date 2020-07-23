package com.study.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 验签
 */
public class SignatureUtil {

    public static String getSignSourceStr(Map<String, String> paramsMap, String privateKey) {
        // 生成待签名字符串
        // 将所有参与签名的参数，以参数名称为键按照字典序进行排序，然后用&作为分隔符将所有的param=value拼接起来，得到待签名的字符串
        Collection<String> keySet = paramsMap.keySet();
        List<String> list = new ArrayList<String>(keySet);
        Collections.sort(list);
        StringBuffer buf = new StringBuffer();
        boolean first = true;
        for (String key : list) {
            if ("sign".equals(key)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                buf.append("&");
            }
            buf.append(key);
            buf.append("=");
            buf.append(paramsMap.get(key));
        }
        return buf.append(privateKey).toString();
    }

    public static String getMD5Signature(Map<String, String> paramsMap, String privateKey) {
        Map<String, String> signMap = new HashMap<>();
        signMap.putAll(paramsMap);
        //过滤null或空
        for (Iterator<Map.Entry<String, String>> it = signMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> item = it.next();
            if (StringUtils.isEmpty(item.getValue())) {
                it.remove();
            }
        }
        String signSource = getSignSourceStr(signMap, privateKey);
        return DigestUtils.md5Hex(signSource);
    }

    public static String getSHA1Signature(String src) {
        try {
            byte[] srcBytes = src.getBytes(("UTF-8"));
            byte[] dstBytes = getSHA1Signature(srcBytes);
            StringBuffer buf = new StringBuffer();
            // SHA-1生成的签名为160位二进制数, 对应20个Byte，每个Byte用2个十六进制数表示, 共转换为40个十六进制数
            for (byte b : dstBytes) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getSHA1Signature(byte[] srcBytes) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA1");
        sha.update(srcBytes);
        return sha.digest();
    }

}
