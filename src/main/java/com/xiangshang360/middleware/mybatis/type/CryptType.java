package com.xiangshang360.middleware.mybatis.type;

import java.io.Serializable;

/**
 * 加密数据类型
 * 
 * @author YuanZhiQiang
 *
 */
@Deprecated
public class CryptType implements Serializable {

	private static final long serialVersionUID = -1600525219377929459L;

	private String value;

	public CryptType(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
