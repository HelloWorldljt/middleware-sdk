package com.xiangshang360.middleware.sdk.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public final class CalculateUtil {

	public static final String FORMATTER_EG1 = "#.##";// 格式化一,保留两位,小数点末尾为0则不显示
	public static final String FORMATTER_EG2 = "###0.00";// 格式化二,保留两位,小数点末尾为0需要显示
	public static final String FORMATTER_EG3 = "#,###.##";// 格式化三,保留两位,美式格式化,小数点末尾为0则不显示

	/* 默认规则格式 */
	public static final int DEFAULT_SCALE = 2;
	/* 计算规则格式 */
	public static final int COMPUTE_SCALE = 6;
	/* 比例规则格式 */
	public static final int RATIO_SCALE = 16;
	/* 误差值 */
	public static final BigDecimal ERROR_RATE = new BigDecimal("0.01");



	/**
	 * 计算相加结果(按照a、b最大精度)
	 * 
	 * @param one
	 *            数字1
	 * @param two
	 *            数字2
	 * @return
	 */
	public static BigDecimal add(BigDecimal one, BigDecimal two) {
		return one.add(two);
	}
	
	/**
	 *  多数值 计算相加结果
	 * @param objs
	 * @return
	 */
	public static BigDecimal add(BigDecimal... objs) {
		BigDecimal result = BigDecimal.ZERO;
		for(BigDecimal obj:objs){
			result = result.add(obj);
		}
		return result;
	}

	/**
	 * 两数字相加，结果取四舍五入精度
	 * 
	 * @param one
	 *            数字1
	 * @param two
	 *            数字2
	 * @param scale
	 *            精度
	 * @return
	 */
	public static BigDecimal addForRoundHalfUp(BigDecimal one, BigDecimal two, int scale) {
		return setScaleForRoundHalfUp(add(one, two), scale);
	}

	/**
	 * 计算相减结果(按照a、b最大精度)
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal one, BigDecimal two) {
		return one.subtract(two);
	}

	/**
	 * 两数字相减，结果取四舍五入精度
	 * 
	 * @param one
	 *            数字1
	 * @param two
	 *            数字2
	 * @param scale
	 *            精度
	 * @return
	 */
	public static BigDecimal subtractForRoundHalfUp(BigDecimal one, BigDecimal two, int scale) {
		return setScaleForRoundHalfUp(subtract(one, two), scale);
	}

	/**
	 * 计算相乘结果(按照a、b最大精度)
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal one, BigDecimal two) {
		return one.multiply(two);
	}
	
	/**
	 *  多数值 计算相乘结果
	 * @param objs
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal... objs) {
		BigDecimal result = BigDecimal.ONE;
		for(BigDecimal obj:objs){
			result = result.multiply(obj);
		}
		return result;
	}

	/**
	 * 计算相乘结果(按照a、b最大精度)
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static BigDecimal multiplyForRoundHalfUp(BigDecimal one, BigDecimal two, int scale) {
		return setScaleForRoundHalfUp(multiply(one, two), scale);
	}

	/**
	 * 计算相除结果,默认精确32位（超过部分使用银行家算法舍入）
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static BigDecimal division(BigDecimal one, BigDecimal two) {
		BigDecimal result = division(one, two, 32);
		return result;
	}

	/**
	 * 计算相除结果(按照指定精度参数)
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @param scale
	 *            精度，超过部分使用银行家算法舍入
	 * @return
	 */
	public static BigDecimal division(BigDecimal one, BigDecimal two, int scale) {
		return one.divide(two, scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * 除法取整
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static int divisionFloor(BigDecimal one, BigDecimal two) {
		BigDecimal result = division(one, two);
		result.setScale(0, RoundingMode.DOWN);
		return result.intValue();
	}

	/**
	 * 判断a是否大于b a > b 返回 TRUE a < b 返回 FALSE a = b 返回 FALSE
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static Boolean isLargeThan(BigDecimal one, BigDecimal two) {
		if (one == null || two == null) {
			return null;
		}
		if (one.compareTo(two) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断a是否大于等于b<br>
	 * a > b 返回 TRUE<br>
	 * a = b 返回 TRUE<br>
	 * a < b 返回 FALSE<br>
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static Boolean isLargeOrEqualThan(BigDecimal one, BigDecimal two) {
		if (one == null || two == null) {
			return null;
		}
		int result = one.compareTo(two);
		if (result > 0) {
			return true;
		} else if (result == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断a是否小于b a < b 返回 TRUE a > b 返回 FALSE a = b 返回 FALSE
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static Boolean isLessThan(BigDecimal one, BigDecimal two) {
		if (one == null || two == null) {
			return null;
		}
		if (one.compareTo(two) < 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断a是否小于等于b<br>
	 * a < b 返回 TRUE <br>
	 * a = b 返回 TRUE a > b 返回 FALSE <br>
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static Boolean isLessOrEqualThan(BigDecimal one, BigDecimal two) {
		if (one == null || two == null) {
			return null;
		}
		int result = one.compareTo(two);
		if (result < 0) {
			return true;
		} else if (result == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断a是否等于b<br>
	 * a < b 返回 FALSE a > b 返回 FALSE<br>
	 * a = b 返回 TRUE <br>
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static Boolean isEqualThan(BigDecimal one, BigDecimal two) {
		if (one == null || two == null) {
			return false;
		}
		int result = one.compareTo(two);
		if (result ==0) {
			return true;
		} else if (result < 0) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * 判断a是否约等于b，精度取到小数点后三位 a = b 返回 TRUE a > b 返回 FALSE a < b 返回 FALSE
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @param scale
	 *            精度，超过部分直接截取
	 * @param errorRate
	 *            允许误差范围
	 * @return
	 */
	public static Boolean isApproximatelyEquals(BigDecimal one, BigDecimal two, int scale, BigDecimal errorRate) {
		if (one == null || two == null) {
			return null;
		}
		BigDecimal min = CalculateUtil.add(one, errorRate);
		BigDecimal max = CalculateUtil.subtract(one, errorRate);
		min = min.setScale(scale, RoundingMode.DOWN);
		max = max.setScale(scale, RoundingMode.DOWN);
		two = two.setScale(scale, RoundingMode.DOWN);
		if ((min.compareTo(two) == 0) || (max.compareTo(two) == 0) || (min.compareTo(two) == 1 && max.compareTo(two) == -1)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 比较两个债权百分比是否相同(直接截取保留16位精度判断，内误差值为零)
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @param scale
	 * @return
	 */
	public static Boolean isApproximatelyEqualsForRate(BigDecimal one, BigDecimal two) {
		return isApproximatelyEquals(one, two, 16, BigDecimal.ZERO);
	}

	/**
	 * 比较两个金额是否相同(直接截取保留6位精度判断，内误差值为零)
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static Boolean isApproximatelyEqualsForAmount(BigDecimal one, BigDecimal two) {
		return isApproximatelyEquals(one, two, 6, BigDecimal.ZERO);
	}
	
	/**
	 * 比较两个值是否相等
	 * @param one
	 * @param two
	 * @return
	 */
	public static Boolean isEquals(BigDecimal one, BigDecimal two){
		return one.compareTo(two)==0;
	}

	/**
	 * 返回最小的数值
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static BigDecimal min(BigDecimal one, BigDecimal two) {
		if (isLargeThan(one, two)) {
			return two;
		} else {
			return one;
		}
	}

	/**
	 * 返回最大的数值
	 * 
	 * @param one
	 *            比较数字1
	 * @param two
	 *            比较数字2
	 * @return
	 */
	public static BigDecimal max(BigDecimal one, BigDecimal two) {
		if (isLargeThan(one, two)) {
			return one;
		} else {
			return two;
		}
	}
	/**
	 * 银行家取舍
	 * @param input
	 * @param scale
	 * @return
	 */
	public static BigDecimal setScale(BigDecimal input,int scale){
		return input.setScale(scale, RoundingMode.HALF_EVEN);
	}

	public static BigDecimal setScaleDown(BigDecimal input,int scale){
		return input.setScale(scale, RoundingMode.DOWN);
	}
	
	/**
	 * 设置债权持有比率精度（直接截取保留16位精度），新交易系统设计的表持有比率最小保留小数点后16位
	 * 
	 * @param input
	 *            待转换对象
	 * @return
	 */
	public static BigDecimal setScaleForBizRate(BigDecimal input) {
		return input.setScale(16, RoundingMode.DOWN);
	}

	/**
	 * 设置金额精度（直接截取保留6位精度），用于保存新交易系统的表，新交易系统设计的表金额最小保留小数点后6位
	 * 
	 * @param input
	 *            待转换对象
	 * @return
	 */
	public static BigDecimal setScaleForBizAmount(BigDecimal input) {
		return input.setScale(6, RoundingMode.DOWN);
	}

	/**
	 * 根据设置的精度，四舍五入转换精度
	 * 
	 * @param input
	 *            待转换对象
	 * @param scale
	 *            保留精度
	 * @return
	 */
	public static BigDecimal setScaleForRoundHalfUp(BigDecimal input, int scale) {
		return new BigDecimal(format(input, scale));
	}

	/**
	 * 根据设置的精度，四舍五入转换精度,一般用于用户交互显示，一般建议金额保留小数后面2位
	 * 
	 * @param input
	 *            待转换对象
	 * @param scale
	 *            精度
	 * @return
	 */
	public static String format(BigDecimal input, int scale) {
		return input.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 判断是否小于0
	 * 
	 * @param input
	 * @return
	 */
	public static Boolean isLessThanZero(BigDecimal input) {
		return isLessThan(input, BigDecimal.ZERO);
	}

	/**
	 * 判断是否大于0
	 * 
	 * @param input
	 * @return
	 */
	public static Boolean isLargeThanZero(BigDecimal input) {
		return isLargeThan(input, BigDecimal.ZERO);
	}
	
	/**
	 * 获取double值
	 * @param input
	 * @param scale
	 * @return
	 * @author chenzheng
	 * @date 2016年10月8日
	 */
	public static Double getDoubleVal(BigDecimal input, int scale) {
		if (null == input) {
			return 0d;
		} else {
			return setScale(input, scale).doubleValue();
		}
	}
	
	/**
	 * bigDecimalToString
	 * @param tem 0.0000转为0   null转为0 
	 * @return String  
	 * @throws
	 */
	public static String bigDecimalToString(BigDecimal tem) {
    	String str = "0";
    	if (Objects.equals(tem, null)) {
    		return str;
    	}
    	if (CalculateUtil.isEquals(tem, BigDecimal.ZERO)) {
    		return str;
    	}
    	return tem.toString();
    }
	/**
	 * 两个Integer的值比较大小
	 * @author xienian
	 * @param one
	 * @param two
	 * @return
	 * @date 2017-10-24
	 */
	public static Boolean isLargeThanAnother(Integer one, Integer two){
		if(one == null || two == null){
			return null;
		}
		
		if(one > two){
			return true;
		}
		return false;
	}

	/**
	 * 相加(d1+d2)
	 * @param d1 乘数1
	 * @param d2 乘数2
	 * @param scale 保留位数
	 * @return double类型
	 */
	public static double doubleSumAdd(double d1, double d2, int scale) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.add(bd2).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * 多个数字相加(保留两位数字 (银行家算法))
	 * @param dd 数字
	 * @return double类型
	 */
	public static double doubleSumAdd(double... dd) {
		BigDecimal result = BigDecimal.ZERO;
		for (double n : dd) {
			result = result.add(new BigDecimal(Double.toString(n)));
		}
		return result.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * double 相减(d1-d2)(银行家算法)
	 * @param d1 减数
	 * @param d2 被减数
	 * @param scale 保留位数
	 * @return double类型
	 */
	public static double doubleSubtract(double d1, double d2, int scale) {
		BigDecimal b = new BigDecimal(Double.toString(d1)).subtract(new BigDecimal(Double.toString(d2)));
		return b.setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * double 相乘(d1*d2) (银行家算法)
	 * @param d1 乘数1
	 * @param d2 乘数2
	 * @param scale 保留位数
	 * @return double类型
	 */
	public static double doubleMultiply(double d1, double d2, int scale) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.multiply(bd2).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * double 相除(d1/d2)(银行家算法)
	 * @param d1 除数
	 * @param d2 被除数
	 * @param scale 保留位数
	 * @return double类型
	 */
	public static double doubleDivide(double d1, double d2, int scale) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		BigDecimal bd3 = bd1.divide(bd2, 15, RoundingMode.HALF_EVEN);
		return bd3.setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * 格式化数字(银行家算法)
	 * @param d1 数字
	 * @param scale 保留位数
	 * @return double类型
	 */
	public static double formatNumber(double d1, int scale) {
		BigDecimal bd = new BigDecimal(Double.toString(d1));
		return bd.setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * 格式化数字(银行家算法)
	 * @param d1 数字
	 * @param formatter 格式
	 * @return String类型
	 */
	public static String formatNumber(double d1, String formatter) {
		BigDecimal bd = new BigDecimal(Double.toString(d1));
		double dd = bd.setScale(10, RoundingMode.HALF_EVEN).doubleValue();
		DecimalFormat df = new DecimalFormat(formatter);
		return df.format(dd);
	}

	/**
	 * 格式化数字
	 * @param bd 数字
	 * @param formatter 格式
	 * @return String类型
	 */
	public static String formatNumber(BigDecimal bd, String formatter) {
		double dd = bd.setScale(10, RoundingMode.DOWN).doubleValue();
		DecimalFormat df = new DecimalFormat(formatter);
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(dd);
	}
}
