package org.qifu.core.logic;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.entity.EzfMap;

public interface IEzfMapperLogicService {
	
	public DefaultResult<Boolean> deleteDs(EzfDs ds) throws ServiceException, Exception;
	
	public DefaultResult<EzfMap> create(EzfMap form) throws ServiceException, Exception;
	
	public DefaultResult<EzfMap> update(EzfMap form) throws ServiceException, Exception;
	
}
