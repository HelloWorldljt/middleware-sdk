package com.xiangshang360.middleware.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.xiangshang360.middleware.sdk.crypt.PBECryptUtils;

/**
 * 加密字段类型，通过该类型处理加密和解密，实体中仍使用String定义。这个类型转换在定义mapper中的<resultMap>具体字段上使用。可以在mybatis-config.xml配置中指定别名来引用
 * 
 * @author chenrg
 * @date 2018年8月16日
 */
public class CryptValueTypeHandler extends BaseTypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
		String x = PBECryptUtils.getInstance().encrypt(parameter);
		ps.setString(i, x);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String x = rs.getString(columnName);
		return decryptResult(x);
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String x = rs.getString(columnIndex);
		return decryptResult(x);
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String x = cs.getString(columnIndex);
		return decryptResult(x);
	}

	private String decryptResult(String x) {
		if (StringUtils.isNotBlank(x)) {
			return PBECryptUtils.getInstance().decrypt(x);
		}
		return x;
	}

}
