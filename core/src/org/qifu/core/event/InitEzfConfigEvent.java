package org.qifu.core.event;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.core.entity.EzfMap;
import org.qifu.core.logic.IEzfTaskSchedService;
import org.qifu.core.service.IEzfMapService;
import org.qifu.core.util.UserUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitEzfConfigEvent {
	protected Logger logger = LogManager.getLogger(InitEzfConfigEvent.class);
	
	@EventListener(ApplicationStartedEvent.class)
	public void afterStartup() {	
		new Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						logger.warn("init create EzfMap job.");
						UserUtils.setUserInfoForUserLocalUtilsBackgroundMode();
						try {
							IEzfMapService<EzfMap, String> ezfMapService = (IEzfMapService<EzfMap, String>) AppContext.getBean(IEzfMapService.class);
							IEzfTaskSchedService ezfTaskSchedService = (IEzfTaskSchedService) AppContext.getBean(IEzfTaskSchedService.class);
							List<EzfMap> ezfMapList = ezfMapService.selectList().getValue();
							for (EzfMap ezfMap : ezfMapList) {
								ezfTaskSchedService.scheduleTask(ezfMap.getCnfId(), new EzfTaskRunnable(ezfMap.getCnfId()), "0 0/1 * * * ?");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						UserUtils.removeForUserLocalUtils();
						logger.info("fine.");
			        }
				}, 30000
		);		
	}	
	
}
