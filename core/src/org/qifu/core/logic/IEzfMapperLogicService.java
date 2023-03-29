package org.qifu.core.logic;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.entity.EzfMap;

public interface IEzfMapperLogicService {
	
	public DefaultResult<Boolean> deleteDs(EzfDs ds) throws ServiceException, Exception;
	
	public DefaultResult<EzfMap> create(EzfMap form) throws ServiceException, Exception;
	
	public DefaultResult<EzfMap> update(EzfMap form) throws ServiceException, Exception;
	
	public DefaultResult<Boolean> delete(EzfMap form) throws ServiceException, Exception;
	
	public void fillEzfMapDataForm(EzfMap dataForm) throws ServiceException, Exception;
	
	public void getFormFieldTemplateConvert2EzfMap(EzfMap form) throws ServiceException, Exception;
	
	public DefaultResult<EzfMap> prepareLoadDataNoWithFindFormOIDsOfProcess(EzfMap dataForm) throws ServiceException, Exception;
	
	public DefaultResult<EzfMap> prepareLoadDataWithFindFormOIDsOfProcess(EzfMap inpForm, EzfMap dataForm) throws ServiceException, Exception;
	
}
