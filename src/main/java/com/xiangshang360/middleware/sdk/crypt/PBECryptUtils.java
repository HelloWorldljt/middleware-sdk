package com.xiangshang360.middleware.sdk.crypt;

import com.xiangshang360.middleware.config.ConfigFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.MessageDigest;

/**
 * PBE加密算法
 *
 * @author zhangli
 */
public class PBECryptUtils {

	private static final Logger LOG = LoggerFactory.getLogger(PBECryptUtils.class);

	private static final PBECryptUtils SINGLETON = new PBECryptUtils();

	/**
	 * JAVA6支持以下任意一种算法 <br>
	 * PBEWITHMD5ANDDES<br>
	 * PBEWITHMD5ANDTRIPLEDES<br>
	 * PBEWITHSHAANDDESEDE <br>
	 * PBEWITHSHA1ANDRC2_40 <br>
	 * PBKDF2WITHHMACSHA1<br>
	 */
	private static final String ALGORITHM = "PBEWithMD5AndDES";

	/**
	 * 迭代次数
	 */
	private static final int ITERATION_COUNT = 100;

	/**
	 * 编码格式
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * 加密算法检测使用的明文
	 */
	private static final String DBCRYPT_PBE_TEST_ARGOT_PLAIN = ConfigFactory.getString("dbcrypt.pbe.test.argot.plain");

	/**
	 * 加密算法检测使用的密文
	 */
	private static final String DBCRYPT_PBE_TEST_ARGOT_CIPHER = ConfigFactory.getString("dbcrypt.pbe.test.argot.cipher");

	/**
	 * 加密算法使用的口令KEY，需要在环境变量中设置
	 */
	public static final String DBCRYPT_PBE_PWD_KEY = "DBCRYPT_PBE_PWD";

	/**
	 * PBE加密的密钥
	 */
	private static Key PBE_KEY;

	private PBECryptUtils() {
		super();
		initCryptAlgorithm();
	}

	/**
	 * 初始化加解密算法所需配置
	 */
	private void initCryptAlgorithm() {
		// 从环境变量获取
		String dbCryptPbePwd = System.getenv(DBCRYPT_PBE_PWD_KEY);
		if (StringUtils.isBlank(dbCryptPbePwd)) {
			LOG.error("Can't read secret-key for PBE from system environment.");
			throw new RuntimeException("Can't read secret-key for PBE from system environment.");
		}
		// 密钥彩礼转换
		PBEKeySpec keySpec = new PBEKeySpec(dbCryptPbePwd.toCharArray());
		try {
			// 实例化
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// 生成密钥
			PBE_KEY = keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			throw new RuntimeException("Init crypt algorithm Exception.", e);
		}

	}

	public static PBECryptUtils getInstance() {
		return SINGLETON;
	}

	/**
	 * 对数据进行加密
	 *
	 * @param in
	 *            待加密字符串
	 * @return 加密后的字符串
	 */
	public String encrypt(String in) {
		try {
			// 数据byte[]
			byte[] input = in.getBytes(CHARSET);

			// 计算盐
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input);
			in = DatatypeConverter.printHexBinary(md5.digest(input)).toLowerCase();
			StringBuilder sb = new StringBuilder(128);
			for (int i = 0; i < in.length(); i += 4) {
				sb.append(in.charAt(i));
			}
			byte[] salt = sb.toString().getBytes(CHARSET);

			// 加密数据
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, PBE_KEY, paramSpec);
			byte[] data = cipher.doFinal(input);

			sb.append(Base64.encodeBase64String(data));
			// 加密后的字符串去掉制表符、换行符
			return sb.toString().trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对数据进行解密
	 *
	 * @param value
	 *            待解密字符串
	 * @return 解密后的字符串，解密如果失败，返回原字符串
	 */
	public String decrypt(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		if (StringUtils.length(value) < 8) {
			return value;
		}

		try {
			// 先解密得到盐
			String saltEncrypt = StringUtils.substring(value, 0, 8);
			byte[] salt = saltEncrypt.getBytes(CHARSET);

			// base解密
			String dataEncrypt = StringUtils.substring(value, 8);
			byte[] data = Base64.decodeBase64(dataEncrypt);
			if (null == data || data.length == 0) {
				return value;
			}

			// PEB解密
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, PBE_KEY, paramSpec);
			data = cipher.doFinal(data);

			return new String(data, CHARSET);
		} catch (Exception e) {
			return value;
		}
	}

	/**
	 * 使用预设的明文和密文检测环境变量中的加密算法口令是否正确
	 */
	public static void testCryptEnv(){
		// 密钥是否正确检测
		String argotPlain = DBCRYPT_PBE_TEST_ARGOT_PLAIN;
		String argotCipher = DBCRYPT_PBE_TEST_ARGOT_CIPHER;
		if (StringUtils.isBlank(argotPlain) || StringUtils.isBlank(argotCipher)) {
			throw new RuntimeException("Can't find argot pair config.");
		}
		String plainTextEnvironment = PBECryptUtils.getInstance().decrypt(argotCipher);
		if (!StringUtils.equals(plainTextEnvironment, argotPlain)) {
			throw new RuntimeException("The crypt secret-key incorrect.");
		}
	}

	public static void main(String[] args) {
		String x = "aaa";
		String d = PBECryptUtils.getInstance().encrypt(x);
		System.out.println("---:"+d);
		String s = PBECryptUtils.getInstance().decrypt(d);
		System.out.println("---:"+s);

	}
}
