package com.xiangshang360.middleware.sdk.util;


public final class Base64Utils {
	public static byte[] decode(String base64) {
		return Base64.decode(base64);
	}

	public static String encode(byte[] bytes) {
		return new String(Base64.encode(bytes));
	}
}