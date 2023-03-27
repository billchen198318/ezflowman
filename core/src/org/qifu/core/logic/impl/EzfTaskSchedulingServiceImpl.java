package org.qifu.core.logic.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.logic.IEzfTaskSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ServiceAuthority(check = false)
@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfTaskSchedulingServiceImpl extends BaseLogicService implements IEzfTaskSchedulingService {
	protected Logger logger = LogManager.getLogger(EzfTaskSchedulingServiceImpl.class);
	
    @Autowired
    private TaskScheduler taskScheduler;
    
    private Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
    
    public void scheduleTask(String jobId, Runnable tasklet, String cronExpression) {
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
    }
    
    public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(jobId, null);
        }
    }
    
}
