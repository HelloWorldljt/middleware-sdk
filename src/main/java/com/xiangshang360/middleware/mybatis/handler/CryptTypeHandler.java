package com.xiangshang360.middleware.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.xiangshang360.middleware.mybatis.type.CryptType;
import com.xiangshang360.middleware.sdk.crypt.AESCryptUtils;

/**
 * 加密解密字段，自定义mybatis类型处理类
 * 
 * @author YuanZhiQiang
 *
 */
@Deprecated
@MappedTypes({ CryptType.class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class CryptTypeHandler extends BaseTypeHandler<CryptType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			CryptType parameter, JdbcType jdbcType) throws SQLException {
		String x = encrypt(parameter.getValue());
		ps.setString(i, x);
	}

	@Override
	public CryptType getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		String x = rs.getString(columnName);
		if (x != null) {
			return new CryptType(decrypt(x));
		} else {
			return null;
		}
	}

	@Override
	public CryptType getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		String x = rs.getString(columnIndex);
		if (x != null) {
			return new CryptType(decrypt(x));
		} else {
			return null;
		}
	}

	@Override
	public CryptType getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String x = cs.getString(columnIndex);
		if (x != null) {
			return new CryptType(decrypt(x));
		} else {
			return null;
		}
	}

	/**
	 * 加密方法
	 * 
	 * @param value
	 * @return
	 */
	private String encrypt(String value) {
		value = AESCryptUtils.encrypt(value);
		return value;
	}

	/**
	 * 解密方法
	 * 
	 * @param value
	 * @return
	 */
	private String decrypt(String value) {
		value = AESCryptUtils.decrypt(value);
		return value;
	}

}