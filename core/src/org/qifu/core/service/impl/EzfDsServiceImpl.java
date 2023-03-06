package org.qifu.core.service.impl;

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.mapper.EzfDsMapper;
import org.qifu.core.service.IEzfDsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfDsServiceImpl extends BaseService<EzfDs, String> implements IEzfDsService<EzfDs, String> {
	
	@Autowired
	EzfDsMapper ezfDsMapper;
	
	@Override
	protected IBaseMapper<EzfDs, String> getBaseMapper() {
		return this.ezfDsMapper;
	}

	@Override
	public Map<String, String> getSelectMap(boolean pleaseSelect) throws ServiceException, Exception {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		List<EzfDs> dsList = this.selectList().getValue();
		for (int i = 0; dsList != null && i < dsList.size(); i++) {
			EzfDs ds = dsList.get(i);
			dataMap.put( ds.getDsId(), ds.getDsName() );
		}
		return dataMap;
	}
	
}
