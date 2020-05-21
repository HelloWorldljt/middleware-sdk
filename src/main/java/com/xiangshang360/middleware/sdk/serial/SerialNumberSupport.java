package com.xiangshang360.middleware.sdk.serial;

/**
 * 主键生成服务
 * 
 * @author YuanZhiQiang
 *
 */
public interface SerialNumberSupport {

	/**
	 * 主键自增默认步长
	 */
	public static final int DEFAULT_INCR_STEP_SIZE = 1;

	/**
	 * 根据指定的ID枚举类生成业务键，定长19位<br>
	 * 例如:10120170425000000001
	 * 
	 * @param type
	 * @return
	 */
	public long generate(SerialNumberEnum type);

	/**
	 * 根据指定的ID枚举类生成业务键，定长19位<br>
	 * 例如:20170425101000000001
	 *
	 * @param type
	 * @return
	 */
	public long generateDateBegin(SerialNumberEnum type);

	/**
	 * 根据指定的ID枚举类生成业务键，指定长度，至少19位<br>
	 * 例如:10120170425000000001
	 * 
	 * @param type
	 * @param length
	 *            指定长度，至少19位
	 * @return
	 */
	public String generate(SerialNumberEnum type, int length);

	/**
	 * 获取自增ID,根据业务和日期作为key，但是只返回自增域
	 * 注意：1、长度要结合业务设置 2、最终ID要包含日期域
	 * 
	 * @param type 自增业务，只用来区分业务不返回
	 * @param incrLength 自增域长度
	 * @return 返回自增域长度的ID，例如incrLength设置成8,则返回：000000001
	 */
	public String getIncrKey(SerialNumberEnum type, int incrLength);

	/**
	 * 根据指定的ID枚举类生成业务键，指定长度，至少19位<br>
	 * 例如:20170425101000000001
	 *
	 * @param type
	 * @param length
	 *            指定长度，至少19位
	 * @return
	 */
	public String generateDateBegin(SerialNumberEnum type, int length);

}
