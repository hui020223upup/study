package com.study.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

/**
 * @author whh
 * @date 2020.07.23
 */
public class DesUtil {

    private static final String algorithm = "DES";
    private static final String defaultCharset = "UTF-8";

    public static String decrypt(String encrypted, String keyStr) throws Exception {
        byte[] input = Base64.decodeBase64(encrypted);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(keyStr));
        byte[] output = cipher.doFinal(input);
        String rawString = new String(output, defaultCharset);
        return rawString;
    }

    public static String encrypt(String rawString, String keyStr) throws Exception {
        byte[] input = rawString.getBytes(defaultCharset);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(keyStr));
        byte[] output = cipher.doFinal(input);
        return Base64.encodeBase64URLSafeString(output);
    }

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(defaultCharset));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        return keyFactory.generateSecret(dks);
    }


}
