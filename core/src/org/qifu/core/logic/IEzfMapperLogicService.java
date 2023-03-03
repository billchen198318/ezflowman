package org.qifu.core.logic;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.EzfDs;

public interface IEzfMapperLogicService {
	
	public DefaultResult<Boolean> deleteDs(EzfDs ds) throws ServiceException, Exception;
	
}
