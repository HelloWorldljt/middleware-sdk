package com.xiangshang360.middleware.sdk.serial;

/**
 * ID枚举类,管理生成的ID,一种代表一个业务,如果有新的业务就需要新建一个枚举值<br>
 * prefix：100做测试使用，范围：101<= prefix <=921
 * 
 * @author YuanZhiQiang
 *
 */
public enum SerialNumberEnum {

	// 测试使用
	FOR_TEST(100),
	// 存管银行交互生成订单
	BANK_DESPOSITORY_ORDER_ID(101),
	// 资产系统借款人ID
	ASSETS_BORROWER_ID(102),
	// 理财人ID
	SERVICE_LENDER_ID(103),
	// 虚拟产品 ID
	SERVICE_AUT_PRODUCT_ID(104),
	// 实体产品 ID
	SERVICE_ACT_PRODUCT_ID(105),
	// 放款提现pay流水号
	LOAN_WITHDRAWS_BUS_ORDER_NO(106), 
    // 个人提现pay流水号 thirdLogNo
    PERSON_WITHDRAWS_BUS_ORDER_NO(107),
	// 投资交易流水号
	INVEST_THIRD_LOG_NO(108),
	// 奖励交易流水号
	REWARD_THIRD_LOG_NO(109),
	// 冻结或者解冻交易流水号
	FREEZE_OR_THAW_THIRD_LOG_NO(110),
	//放款交易流水号
	LOAN_THIRD_LOG_NO(111),
	//银行项目编号
	BANK_PRODUCT_ID(112),
	//代偿还款交易流水号
	COMPENSATORY_REPAY_THIRD_LOG_NO(113),
	//风险金平账交易流水号
	RISK_BALANCE_THIRD_LOG_NO(114),
	//资产流标交易流水号
	LOAN_FAIL_THIRD_LOG_NO(115),
	//上传文件
	FIEL_UPLOAD(116),
	//借款人还款交易流水号
	BORROWER_REPAY_THIRD_LOG_NO(117),
	//存管订单编号
	DEPOSITORY_ORDER_NO(118),
	//自有资金转账到借款人代偿还款
	SELF_2_BORROW_THIRD_LOG_NO(119),
	//下载文件
	FIEL_DOWNLOAD(120),
	// 企业借款编号
	ENTERPRISE_LOAN_NUMBER(121),
	//分布式事务序号
	TRANS_EVENT(122),

	// 企业用户编号
    MICRO_ENTERPRISE_USER_SEQ(123), MICRO_ENTERPRISE_SEQ(124), MICRO_ENTERPRISE_BUS_SEQ(125),

    //opm-系统营收转账-orderFormId
    OPM_SRTA_ORDER_FORM_ID(126),
	USER_RED_BAG(130),

	// 签约订单生成BUSORDERNO
	SIGN_MATCH_ORDER_SEQ(127),
	SIGN_AGREEMENT_SEQ(128),
	SIGN_JOIN_PLAN_SEQ(129),
	// 前端统一订单编号
	BOOKING_ORDER_NO(131),

	// 银行不明来账处理流水号
	UNKNOWN_ACCOUNT_HANDLE_NO(132),

	// 资产匹配申请编号
	ASSET_MATCH_NO(133),
	// 资金匹配编号
	CAPITAL_MATCH_NO(134),
	// 匹配流程批次号
	MICRO_MATCH_BATCH_NO(135),
	// 匹配结果编号
	MICRO_MATCH_RESULT_NO(136),
	// 自动投标授权记录编号
	AUTO_BID_ORDER_NO(137),
	// 虚账户操作流水号
	VIRTUAL_ACCOUNT_HANDLE_NO(138),
	// 短网址关键字序号
	SHORT_URL_KEY_NO(150),
	//授权转出
	AUT_TRANSFER_EXIT_NO(151),
	// 微服务-合同协议管理-文件编号
	MICRO_AGREEMENT_FILE_NO(160),
	// 微服务-合同协议管理-事件编号
	MICRO_AGREEMENT_EVENT_NO(161),
	// 微服务-流标流水号
	ASSET_FAIL_SERIAL_NO(162),

    //提现单号
    WITHDRAW_NO(170),
    //平安银行api项目，请求银行流水号码
    MICRO_PINGAN_API_BANK_SEQ(171),
    //充值单号
    RECHARGE_NO(172),
    //绑卡流水号
    BIND_CARD_SEQ(173),
    //解绑流水号
    UNBIND_CARD_SEQ(174),
    FINANCING_BUSINESS(175),
    // 存管业务订单号
    BANK_DEPOSITORY_BUSINESS_NO(176),
	// 代偿归还流水号
	MICRO_OVERDUE_RETURN_SEQ(177),
	// 主投购买冻结账户流水号
	ACT_BUY_FREEZE_ACCOUNT_SEQ(178),
	//订单中心请求交易流水号
	MICRO_ORDER_TRANSACTION_SEQ(179),
	//订单取消挂牌交易流水号
	ORDER_CANCEL_LIST_TRANSACTION_SEQ(180),
	// 还款微服务交易流水号
	MICRO_REPAY_TRANSACTION_SEQ(181),
	    
    // micro-pay签约订单号
    MICRO_PAY_SIGN_SEQ(182),
	// micro-pay划扣银行签约订单号
	MICRO_PAY_BANK_SEQ(183),
	// micro-pay划扣订单号
	MICRO_PAY_DEDUCT_ORDER_SEQ(184),

	// micro-operation 序列号
	MICRO_OPERATION_NO(185),
	// 向大侠下发召唤奖励流水号
	REDBAG_BROKER_CALL_SEQ(186),
	// mocro-member交易流水号
	MICRO_MEMBER_TRANSACTION_SEQ(187),

	/**
	 * 采购单服务生成的业务订单号
	 */
	MICRO_BOOKING_BUS_ORDER_NO(188),

	/**
	 * 受托支付划转流水号
	 */
	MICRO_TRUSTFUL_TRANS_SEQ(189),

	/**
	 * 批量下发站内信批次号
	 */
	MICRO_NOTICE_BATCH_SYS_MSG_SEQ(190),
    ;
	/**
	 * 生成ID的业务前缀
	 */
	private int prefix;

	/**
	 * 序列域长度<br>
	 * 前缀长度(3)+8+序列长度（len）=生成ID总长度,例如len=8,生成ID总长度为19<br>
	 * 每日最大生成序列总数量：99999999个
	 */
	private int len = 8;

	private SerialNumberEnum(int prefix) {
		this.prefix = prefix;
	}

	public int getPrefix() {
		return prefix;
	}

	public int getLen() {
		return len;
	}

}
