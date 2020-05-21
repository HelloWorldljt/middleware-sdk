package com.xiangshang360.middleware.sdk.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AESAndMD5Util {

    private static final Logger LOG = LoggerFactory.getLogger(AESAndMD5Util.class);

    // 编码类型
    private static Charset charset = Charset.forName("UTF-8");

    /**
     * AES加密
     * 
     * @param content 加密明文
     * @return 返回base64编码
     */
    public static String encrypt(String content, String aesKey) {
        try{
            Cipher aesECB = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(charset), "AES");
            aesECB.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = aesECB.doFinal(content.getBytes(charset));
            Base64 base64 = new Base64();
            String data = base64.encodeToString(result);
            return data.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");  
        }catch(Exception e){
            LOG.error("AES加密报错。content = "+content);
            return null;
        }
    }

    /**
     * AES解密
     * 
     * @param content 需要解密的密文
     * @return 解密之后的内容
     */
    public static String decrypt(String content, String aesKey) {
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(charset), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = Base64.decodeBase64(content);
            return new String(cipher.doFinal(result), charset); // 解密
        }catch(Exception e){
            LOG.error("AES解密报错。content = "+content);
            return null;
        }
    }

    /**
     * 对传入参数进行加密
     * 
     * @param param 为排序参数
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String md5(Map<String, String> param, String md5Key) {
        if (param == null || param.size() == 0) {
            return null;
        }
        String msg = null;
        
        Set<Entry<String, String>> set = param.entrySet();
        List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(set);

        // 对传入参数排序
        Collections.sort(list, new Comparator<Entry<String, String>>() {
            @Override
            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });

        // 对传入参数拼接字符串
        StringBuilder sb = new StringBuilder();
        Entry<String, String> entry;
        for (int i=0, j=list.size(); i<j; i++) {
            entry = list.get(i);
            sb.append(entry.getKey()).append(entry.getValue());
        }
        
        // 加入MD5KEY
        sb.append(md5Key);
        
        msg = sb.toString();
        LOG.info("拼接后的字符串：" + msg);
        try{
            return md5(msg);
        }catch(Exception e){
            LOG.error("MD5加密报错。param = "+param);
            return null;
        }
    }
    
    /**
     * md5加密
     * 
     * @param inputText
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String md5(String inputText) {
        try{
            return md5Encrypt(inputText, "md5");
        }catch(Exception e){
            LOG.error("MD5加密报错。inputText = "+inputText);
            return null;
        }
    }
    
    /**
     * md5或者sha-1加密
     * 
     * @param inputText 要加密的内容
     * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static String md5Encrypt(String inputText, String algorithmName) throws NoSuchAlgorithmException {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        MessageDigest m = MessageDigest.getInstance(algorithmName);
        m.update(inputText.getBytes(charset));
        byte s[] = m.digest();
        return hex(s);
    }

    /**
     * 返回十六进制字符串
     * 
     * @param arr
     * @return
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

}
