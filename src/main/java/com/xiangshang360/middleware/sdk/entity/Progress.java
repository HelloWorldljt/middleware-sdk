package com.xiangshang360.middleware.sdk.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * 进度条实体
 * @author  tanyuanpeng
 */
public class Progress {

	private String name;
	private long total;
	private long processed;
	private long success;
	private long fail;

	public Progress(long total) {
		super();
		this.total = total;
	}

	public Progress(String name, long total) {
		this.name = name;
		this.total = total;
	}

	public String currentRate() {
		double rate = processed * 1.0d / total;
		double r = (rate * 10000) / 100;
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(this.name)){
			sb.append("task [").append(this.name).append("] ");
		}
		sb.append(String.format("%s%%[%s/%s],ok:[%s],fail:[%s]",r,processed,total,success,fail));
		return sb.toString();
	}

	public void incrProcessed() {
		processed++;
	}

	public void incrSuccess() {
		success++;
	}

	public void incrFail() {
		fail++;
	}

	// -------------getter and setter

	public long getTotal() {
		return total;
	}

	public long getProcessed() {
		return processed;
	}

	public long getSuccess() {
		return success;
	}

	public long getFail() {
		return fail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setProcessed(long processed) {
		this.processed = processed;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	public void setFail(long fail) {
		this.fail = fail;
	}
}
