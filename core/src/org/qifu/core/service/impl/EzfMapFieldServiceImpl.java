package org.qifu.core.service.impl;

import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.EzfMapField;
import org.qifu.core.mapper.EzfMapFieldMapper;
import org.qifu.core.service.IEzfMapFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfMapFieldServiceImpl extends BaseService<EzfMapField, String> implements IEzfMapFieldService<EzfMapField, String> {
	
	@Autowired
	EzfMapFieldMapper ezfMapFieldMapper;

	@Override
	protected IBaseMapper<EzfMapField, String> getBaseMapper() {
		return this.ezfMapFieldMapper;
	}
	
}
