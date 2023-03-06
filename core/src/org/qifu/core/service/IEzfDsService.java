package org.qifu.core.service;

import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.service.IBaseService;
import org.qifu.core.entity.EzfDs;

public interface IEzfDsService<T, E> extends IBaseService<EzfDs, String> {
	
	public Map<String, String> getSelectMap(boolean pleaseSelect) throws ServiceException, Exception;
	
}
