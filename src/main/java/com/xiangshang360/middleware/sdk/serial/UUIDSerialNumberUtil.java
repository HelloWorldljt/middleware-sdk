package com.xiangshang360.middleware.sdk.serial;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public final class UUIDSerialNumberUtil {

	private UUIDSerialNumberUtil() {
	}

	/**
	 * 生成唯一的UUID,并删除掉中划线，并全部转换成大写字母<br>
	 * 例如：302B0256D2214FD8A120860F968F578C
	 * 
	 * @return
	 */
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		// 移除中划线
		uuid = StringUtils.remove(uuid, '-');
		// 全部转换成大写字母
		return StringUtils.upperCase(uuid);
	}

}
