package com.xiangshang360.middleware.sdk.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务枚举类<br>
 * 枚举的名字将作为锁KEY的前缀部分，代表需要分布式锁的业务部分，generateDistributedKey方法用来生成真正的锁KEY，
 * 锁定具体业务的具体资源。<br>
 * 举例：债权还款业务，那么可以定义BUS_LOAN_REPAY,作为枚举，调用generateDistributedKey(
 * DistributedLockEnum.BUS_LOAN_REPAY,"loanId","orderId"),意义：同一笔债权还同一笔订单需要同步执行;
 * 
 * @author YuanZhiQiang
 * @date 2017年5月5日
 *
 */
public enum DistributedLockEnum {
	/**
	 * 测试使用
	 */
	FOR_TEST_NO_TRY,
	/**
	 * 测试使用2
	 */
	FOR_TEST_WITH_TIMEOUT(3, TimeUnit.SECONDS),
	/**
	 * Redis 获取当前时间服务（SDK）
	 */
	REDIS_CURRENT_TIMESTAMP(5, TimeUnit.SECONDS),

	/**
	 * 微服务放款预处理（针对定时任务的锁）
	 */
	MICRO_LOAN_PREPARE_FOR_TASK(5,TimeUnit.SECONDS),
	/**
	 * 微服务还款审核（针对定时任务的锁）
	 */
	MICRO_REPAY_AUDIT_FOR_TASK(0,TimeUnit.SECONDS),
	/**
	 * 微服务批量请求文件生成（针对定时任务的锁）
	 */
	MICRO_BATCH_REQ_FILE_GEN_AND_UPLOAD_FOR_TASK(5,TimeUnit.SECONDS),
	/**
	 * 微服务应答文件下载（针对定时任务的锁）
	 */
	MICRO_BATCH_RSP_FILE_DOWNLOAD_FOR_TASK(5,TimeUnit.SECONDS),
	/**
	 * 微服务批量应答文件回写（针对定时任务的锁）
	 */
	MICRO_BATCH_RSP_WRITEBACK_FOR_TASK(5,TimeUnit.SECONDS),
	/**
	 * 微服务批量处理触发（针对定时任务的锁）
	 */
	MICRO_BATCH_REQ_FILE_HANDLE_NOTICE_FOR_TASK(5,TimeUnit.SECONDS),
	/**
	 * 微服务放款预处理（针对申请的锁）
	 */
	MICRO_LOAN_PREPARE_FOR_APPLY(5,TimeUnit.SECONDS),
	/**
	 * 微服务放款并发锁(针对定时任务的锁)
	 */
	MICRO_START_LOAN_FOR_TASK(5, TimeUnit.SECONDS),
	/**
	 * 微服务放款消息补偿锁（针对消息MQ消息发送补偿）
	 */
	MICRO_START_LOAN_FOR_RESEND(5, TimeUnit.SECONDS),
	/**
	 * 微服务放款并发锁(针对每个申请ID的锁)
	 */
	MICRO_START_LOAN_FOR_APPLY(5, TimeUnit.SECONDS),

    /**
     * 充值(针对每个订单的锁)
     */
    RECHARGE_ORDER_NO(5, TimeUnit.SECONDS),
    /**
     * 提现(针对每个订单的锁)
     */
    WITHDRAW_ORDER_NO(5, TimeUnit.SECONDS),


	/**资产全局锁(资产id的锁)**/
	LOCK_LOAN_ASSERT(0, TimeUnit.SECONDS),

	/**还款明细同步银行(明细记录id的锁)**/
	LOCK_REPAY_DETAIL_TOBANK(0, TimeUnit.SECONDS),
	/**
	 * 微服务匹配流程定时锁
	 */
	MICRO_MATCH_FLOW_TASK(5, TimeUnit.SECONDS),

	/**
	 * 微服务匹配结果结算定时锁
	 */
	MICRO_MATCH_BALANCE_TASK(5, TimeUnit.SECONDS),

	/**
	 * 日进斗金（签约类订单）生成退出申请签约锁
	 */
	SIGN_ORDER_EXIT_APPLY(0,TimeUnit.SECONDS),

     /**
	 * 膨胀红包，拆红包加锁
	 */
	EXPAND_RED_BAG(0,TimeUnit.SECONDS),

	/**
	 * 膨胀红包，拆红包加锁
	 */
	LOTTERY_PRIZE_DRAW(0,TimeUnit.SECONDS),

	/**
	 * 获取个人印章并生成印章防并发锁
	 */
	MICRO_CFCA_LOCK_GET_PERSON_SEAL(10,TimeUnit.SECONDS),

	/**
	 * 微服务匹配过程资源锁
	 */
	MICRO_MATCH_RES_LOCK(5, TimeUnit.SECONDS),

	/**
	 * 队列出队管理器锁
	 */
	DEQUEUE_MANAGER_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 节假日爬虫锁
	 */
	@Deprecated
	SPIDER_HOLIDAY_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 主投转让申请处理锁
	 */
	ACT_TRANSFER_APPLY_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 主投撤销转让处理锁
	 */
	ACT_TRANSFER_CANCEL_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 流标申请处理锁
	 */
	ASSET_FAIL_APPLY_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 微服务，协议签章任务锁
	 */
	MICRO_AGREEMENT_TASK(0, TimeUnit.SECONDS),

	/**
	 * 微服务还款申请生成（针对定时任务的锁）
	 */
	MICRO_REPAY_APPLY_GENERATE_FOR_TASK(0,TimeUnit.SECONDS),

	/**
	 * 微服务还款生成预处理任务（针对定时任务的锁）
	 */
	MICRO_REPAY_GENERATE_PRETREATMENT_TASK(0,TimeUnit.SECONDS),

	/**
	 * 微服务代偿归还生成预处理任务（针对定时任务的锁）
	 */
	MICRO_OVERDUE_RETURN_PRETREATMENT_TASK(0,TimeUnit.SECONDS),

	/**
	 * 微服务还款同步银行（针对定时任务的锁）
	 */
	MICRO_REPAY_SYN_BANK_TASK(0,TimeUnit.SECONDS),

	/**
	 * 微服务逾期归还同步银行（针对定时任务的锁）
	 */
	MICRO_OVERDUE_SYN_BANK_TASK(5,TimeUnit.SECONDS),

	/**
	 * 微服务正常还款处理定时任务（针对定时任务的锁）
	 */
	MICRO_REPAY_PLAN_TASK(0,TimeUnit.SECONDS),

	/**
	 * 微服务处理定时任务（针对定时任务的锁）
	 */
	MICRO_OVERDUE_RETURN_TASK(0,TimeUnit.SECONDS),

	/**
	 * 微服务处理逾期预警定时任务（针对定时任务的锁）
	 */
	MICRO_OVERDUE_WARNING_TASK(0,TimeUnit.SECONDS),


	/**
	 * 微服务提前还款处理定时任务（针对定时任务的锁）
	 */
	MICRO_ADVANCE_REPAY_PLAN_TASK(0,TimeUnit.SECONDS),
	/**
	 * 微服务 原始资产放款
	 */
	MICRO_ORIGIN_ASSET_FAIL(0, TimeUnit.SECONDS),

	/**
	 * 微服务赎回生成预处理任务（针对定时任务的锁）
	 */
	MICRO_REDEEM_GENERATE_PRETREATMENT_TASK(5,TimeUnit.SECONDS),
	/**
	 * 微服务赎回后续处理任务（针对定时任务的锁）
	 */
	MICRO_REDEEM_DEAL_TASK(5,TimeUnit.SECONDS),


	/**
	 * 微服务 操作资产全局锁
	 */
	MICRO_LOCK_LOAN_ASSERT(0, TimeUnit.SECONDS),

	/**
	 * 微服务 操作垫付申请 全局锁
	 */
	MICRO_LOCK_ADVANCE_APPLY(0, TimeUnit.SECONDS),

	/**
	 * 代偿归还预处理锁
	 */
	MICRO_LOCK_RETURN_PREPARE(0, TimeUnit.SECONDS),

	/**
	 * 代偿归还同步银行锁
	 */
	MICRO_LOCK_RETURN_SYNC_BANK(0, TimeUnit.SECONDS),

	/**
	 * 获取红包与抽奖机会锁
	 */
	OBTAIN_REDBAG_AND_LOTTERY_LOCK(0, TimeUnit.SECONDS),
	/**
	 * 获取红包
	 */
	OBTAIN_REDBAG_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 处理主投购买
	 */
	MICRO_ACT_BUY_LOCK(0, TimeUnit.SECONDS),

    /**
     * 主动类生成冻结记录锁
     */
    ACT_ASSET_FROZEN_RECORD_TASK_LOCK(0, TimeUnit.SECONDS),

    /**
     * 主动类资产拆分锁
     */
    ACT_ASSET_FROZEN_SPLIT_TASK_LOCK(0, TimeUnit.SECONDS),
    /**
	 * 处理主投放款回调
	 */
	MICRO_ACT_LOAN_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 处理流标回调
	 */
	MICRO_ACT_FAIL_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 处理还款通知回调
	 */
	MICRO_ACT_REPAY_LOCK(0, TimeUnit.SECONDS),
	
	/**
	 * 处理主投订单投资
	 */
	MICRO_ACT_ORDER_INVEST_LOCK(1, TimeUnit.SECONDS),

	/**
	 * 不明来账清分充值
	 */
	CLEARING_RECHARGE_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 借款人提现
	 */
	BORROWER_WITHDRAW_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 应急中心上报数据生成任务，
	 */
	MICRO_EMERGENCY_REPORT_DATA_GENERATE_TASK(0, TimeUnit.SECONDS),
	/**
	 * 应急中心上报数据提交任务，
	 */
	MICRO_EMERGENCY_REPORT_DATA_SUBMIT_TASK(0, TimeUnit.SECONDS),
	/**
	 * 活动锁
	 */
	ACTIVE_ACTIVE_LOCK(5, TimeUnit.SECONDS),
	/**
	 * 委托类订单匹配锁
	 */
	AUT_ORDER_MATCH_LOCK(0, TimeUnit.SECONDS),
	/**
	 * 申请扣费锁
	 */
	ASSETS_DEDUCT_APPLY_LOCK(5,TimeUnit.SECONDS),

	/**
	 * micro-pay 请求排重锁
	 */
	MICRO_PAY_REQUEST_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 还款处理资产期数锁
	 */
	MICRO_REPAY_PHASE_NUMBER_LOCK(30,TimeUnit.SECONDS),

	/**
	 * 会员中心微服务锁 - 积分抽奖
	 *
	 */
	MICRO_MEMBER_POINT_LOTTERY_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 会员中心微服务锁 - 积分兑换
	 */
	MICRO_MEMBER_POINT_CONVERT_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 会员中心微服务锁 - 积分抽奖
	 *
	 */
	MICRO_EXPAND_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 会员中心微服务锁 - 拼图
	 *
	 */
	MICRO_MEMBER_PUZZLE_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 会员微服务-向上运动锁
	 */
	MICRO_MEMBER_SPORT_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 会员中心微服务锁 - 摇一摇
	 *
	 */
	MICRO_MEMBER_SHAKE_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 对账系统定时任务锁
	 */
	COMPARE_ACCOUNT_TASK_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 订单多退少补手续费，目前有还款扣费在使用，添加为了防止多个功能订单同时扣费使用导致扣费错误问题
     */
    ORDER_DEDUCT_FEE_APPLY_LOCK(5,TimeUnit.SECONDS),


	/**
	 * 卡券服务-投资增加委托卡券激活机会
	 */
	MICRO_COUPON_ADD_INVEST_ACTIVE_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 卡券服务-邀请注册增加委托卡券激活机会
	 */
	MICRO_COUPON_ADD_INVITE_ACTIVE_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 卡券服务-委托类卡券激活防并发锁
	 */
	MICRO_COUPON_AUT_COUPON_ACTIVE_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 用户风险测评提交结果并发锁
	 */
	USER_RISK_EVALUATION_SUBMIT_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 获取积分锁
	 */
	MICRO_MEMBER_EARN_POINT_LOCK(20, TimeUnit.SECONDS),

	/**
	 * 微服务受托划转支付（针对定时任务的锁）
	 */
	MICRO_TRUSTFUL_TRANS_TASK(0,TimeUnit.SECONDS),

	/**
	 * UC库用户存管信息初始化，并发锁
	 */
	MICRO_USER_DEPOSIT_INFO_INIT_LOCK(0,TimeUnit.SECONDS),

	/**
	 * 主投订单购买
	 */
	MICRO_ACT_ORDER_BUY_LOCK(0,TimeUnit.SECONDS),
	/**
	 * 天眼获取钉钉token并发锁
	 */
	MICRO_SKY_EYE_DING_TALK_TOKEN_LOCK(10, TimeUnit.SECONDS),

	/**
	 * 资产审核锁
	 */
	MICRO_LOCK_ASSET_AUDIT_SINGLE(0,TimeUnit.SECONDS),
	/**
	 * opm 审核资产，操作锁
	 */
	MICRO_LOCK_OPM_ASSET_AUDIT(0,TimeUnit.SECONDS),

	/**
	 * 进件资产锁
	 */
	MICRO_ENTRY_LOAN(0, TimeUnit.SECONDS),

	/**
	 *  进件附件定时任务锁
	 */
	MICRO_ENTRY_LOAN_ACCESSORY(0, TimeUnit.SECONDS),

	/**
	 *推送风控任务锁
	 */
	MICRO_LAS_PUSH_LOAN_RISK_AUDIT_TASK(0, TimeUnit.SECONDS),
	/**
	 * 向大侠召唤好友短信并发key
	 **/
	BROKER_AWAKE_SMS_LOCK(0, TimeUnit.SECONDS),

	/**
	 * 交易拦截锁
	 */
	BUSINESS_INTERCEPTOR_LOCK(30,TimeUnit.SECONDS),

		/**
	 * 资产审核任务锁
	 */
	MICRO_LAS_AUTO_AUDIT_REPORT_TASK(0, TimeUnit.SECONDS),
	/**
	 * 获取进件附件锁
	 */
	LAS_LOAN_ATTACH_INFO_KEY(0, TimeUnit.SECONDS),

	/**
	 * 进件流标通知的锁
	 */
	MICRO_FAIL_NOTICE(0, TimeUnit.SECONDS),
	/**
	 * 红包奖励的锁
	 */
	LENDER_SYN_ACCOUNT(0,TimeUnit.SECONDS),

	/**
	 * 天眼获取钉钉ticket并发锁
	 */
	MICRO_SKY_EYE_DING_TALK_TICKET_LOCK(10, TimeUnit.SECONDS),

	/**
	 * 部分退出审核分布式锁
	 */
	PART_EXIT_AUDIT_LOCK(10, TimeUnit.SECONDS),

	/**
	 * 部分退出申请分布式锁
	 */
	PART_EXIT_APPLY_LOCK(10, TimeUnit.SECONDS),

	;

	/**
	 * 获取锁的尝试超时时间，0不等待，拿不到锁直接返回
	 */
	private long timeout = 0L;

	/**
	 * 获取锁的尝试超时时间单位（默认：毫秒）
	 */
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

	private DistributedLockEnum() {
	}

	private DistributedLockEnum(long timeout, TimeUnit timeUnit) {
		this.timeout = timeout;
		this.timeUnit = timeUnit;
	}

	public long getTimeout() {
		return timeout;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	/**
	 * 生成分布式锁的KEY<br>
	 * 例如：FOR_TEST_NO_TRY_part1_part2_part3
	 * @param lockEnum
	 * @param keyParts
	 * @return
	 */
	public static String generateDistributedKey(DistributedLockEnum lockEnum, String... keyParts) {
		StringBuilder sb = new StringBuilder("SDK-LOCK-").append(lockEnum.name()).append("-");
		if (keyParts != null && keyParts.length > 0) {
			for (String keyPart : keyParts) {
				sb.append(keyPart);
				sb.append("-");
			}
		}
		// 去掉最后一个下划线
		return sb.substring(0, sb.length() - 1);
	}

}
