package org.qifu.core.event;

import org.springframework.stereotype.Service;

@Service
public class EzfTaskRunnable implements Runnable {
	
	private String cnfId;
	
	public EzfTaskRunnable(String cnfId) {
		this.cnfId = cnfId;
	}

	@Override
	public void run() {
		System.out.println("cnfId>>>" + this.cnfId);
	}
	
}
