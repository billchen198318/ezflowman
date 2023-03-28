package org.qifu.core.event;

public class EzfTaskRunnable implements Runnable {
	
	private String cnfId;
	
	public EzfTaskRunnable() {
		super();
	}
	
	public EzfTaskRunnable(String cnfId) {
		super();
		this.cnfId = cnfId;
	}
	
	@Override
	public void run() {
		System.out.println("cnfId>>>" + this.cnfId);
	}
	
}
