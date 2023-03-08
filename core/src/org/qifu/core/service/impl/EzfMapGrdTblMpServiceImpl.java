package org.qifu.core.service.impl;

import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.EzfMapGrdTblMp;
import org.qifu.core.mapper.EzfMapGrdTblMpMapper;
import org.qifu.core.service.IEzfMapGrdTblMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfMapGrdTblMpServiceImpl extends BaseService<EzfMapGrdTblMp, String> implements IEzfMapGrdTblMpService<EzfMapGrdTblMp, String> {
	
	@Autowired
	EzfMapGrdTblMpMapper ezfMapGrdTblMpMapper;
	
	@Override
	protected IBaseMapper<EzfMapGrdTblMp, String> getBaseMapper() {
		return this.ezfMapGrdTblMpMapper;
	}
	
}
