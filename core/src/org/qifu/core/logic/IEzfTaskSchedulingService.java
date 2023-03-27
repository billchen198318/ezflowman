package org.qifu.core.logic;

public interface IEzfTaskSchedulingService {
	
	public void scheduleTask(String jobId, Runnable tasklet, String cronExpression);
	
	public void removeScheduledTask(String jobId);
	
}
