package com.xiangshang360.middleware.sdk.crypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具（用来处理数据库中的加密字段）<br>
 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
 * 此处使用AES-128-CBC加密模式，key需要为16位。
 * 
 * @author YuanZhiQiang
 *
 */
@Deprecated
public class AESCryptUtils {

	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	private static final String VI = "0102030405060708";

	private static final Logger logger = LoggerFactory
			.getLogger(AESCryptUtils.class);

	private static final String KEY = "comxiangshang360";

	/**
	 * 加密
	 * 
	 * @param input
	 * @return
	 */
	public static String encrypt(String input) {
		try {
			if (KEY == null) {
				System.out.print("Key为空null");
				return null;
			}
			if (KEY.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = KEY.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);// "算法/模式/补码方式"
			IvParameterSpec iv = new IvParameterSpec(VI.getBytes("ASCII"));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
			return Base64.encodeBase64String(encrypted);// 此处使用BAES64做转码功能，同时能起到2次加密的作用。
		} catch (Exception e) {
			logger.error("cncrypt exception:" + input, e);
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param input
	 * @return
	 */
	public static String decrypt(String input) {
		try {
			// 判断Key是否正确
			if (KEY == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (KEY.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = KEY.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(VI.getBytes("ASCII"));
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = Base64.decodeBase64(input);// 先用bAES64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString;
		} catch (Exception e) {
			logger.error("decrypt exception:" + input, e);
			return null;
		}
	}

	public static void main(String[] args) {
		String name = "中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国中华人民共和国";
		System.out.println("原文：" + name);
		System.out.println("原文长度：" + name.length());
		String code = AESCryptUtils.encrypt(name);
		System.out.println("密文：" + code);
		System.out.println("密文长度：" + code.length());
		String name2 = AESCryptUtils.decrypt(code);
		System.out.println("明文：" + name2);
	}
}
