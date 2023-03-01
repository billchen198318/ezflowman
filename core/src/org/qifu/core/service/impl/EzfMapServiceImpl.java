package org.qifu.core.service.impl;

import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.EzfMap;
import org.qifu.core.mapper.EzfMapMapper;
import org.qifu.core.service.IEzfMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfMapServiceImpl extends BaseService<EzfMap, String> implements IEzfMapService<EzfMap, String> {
	
	@Autowired
	EzfMapMapper ezfMapMapper;

	@Override
	protected IBaseMapper<EzfMap, String> getBaseMapper() {
		return this.ezfMapMapper;
	}
	
}
