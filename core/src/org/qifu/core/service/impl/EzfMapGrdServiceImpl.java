package org.qifu.core.service.impl;

import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.EzfMapGrd;
import org.qifu.core.mapper.EzfMapGrdMapper;
import org.qifu.core.service.IEzfMapGrdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfMapGrdServiceImpl extends BaseService<EzfMapGrd, String> implements IEzfMapGrdService<EzfMapGrd, String> {
	
	@Autowired
	EzfMapGrdMapper ezfMapGrdMapper;

	@Override
	protected IBaseMapper<EzfMapGrd, String> getBaseMapper() {
		return this.ezfMapGrdMapper;
	}
	
}
