package com.xiangshang360.middleware.sdk.serial;


/**
 * 定义分布式事务业务类型，注意，要求按照业务执行顺序来定义，执行的先定义（码值小），后执行的后定义（码值大），会影响消息入队的优先级
 * @author XS
 *
 */
public enum TransEventType implements IntEnumInter<TransEventType> {

	/**
	 * 核心业务微服务-单笔交易订单同步银行
	 */
	ORDER_LOAN_PRETREAT(100, "放款微服务-订单放款预处理")
	;

	private int value;
	private String desc;

	private TransEventType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public int intValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.desc;
	}

}
