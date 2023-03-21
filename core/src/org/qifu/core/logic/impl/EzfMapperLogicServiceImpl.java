/* 
 * Copyright 2019-2023 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.core.logic.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.EzfDs;
import org.qifu.core.entity.EzfMap;
import org.qifu.core.entity.EzfMapField;
import org.qifu.core.entity.EzfMapGrd;
import org.qifu.core.entity.EzfMapGrdTblMp;
import org.qifu.core.logic.IEzfMapperLogicService;
import org.qifu.core.service.IEzfDsService;
import org.qifu.core.service.IEzfMapFieldService;
import org.qifu.core.service.IEzfMapGrdService;
import org.qifu.core.service.IEzfMapGrdTblMpService;
import org.qifu.core.service.IEzfMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ServiceAuthority(check = false)
@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EzfMapperLogicServiceImpl extends BaseLogicService implements IEzfMapperLogicService {
	protected Logger logger = LogManager.getLogger(EzfMapperLogicServiceImpl.class);
	
	@Autowired
	IEzfDsService<EzfDs, String> ezfDsService;
	
	@Autowired
	IEzfMapService<EzfMap, String> ezfMapService;
	
	@Autowired
	IEzfMapFieldService<EzfMapField, String> ezfMapFieldService;	
	
	@Autowired
	IEzfMapGrdService<EzfMapGrd, String> ezfMapGrdService;
	
	@Autowired
	IEzfMapGrdTblMpService<EzfMapGrdTblMp, String> ezfMapGrdTblMpService;
	
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} ) 	
	@Override
	public DefaultResult<Boolean> deleteDs(EzfDs ds) throws ServiceException, Exception {
		if (null == ds || this.isBlank(ds.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		ds = this.ezfDsService.selectByPrimaryKey(ds.getOid()).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dsId", ds.getDsId());
		if ( this.ezfMapService.count(paramMap) > 0 ) {
			throw new ServiceException(ds.getDsId() + " 正在被使用,無法刪除!");
		}		
		return this.ezfDsService.delete(ds);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<EzfMap> create(EzfMap form) throws ServiceException, Exception {
		if (null == form) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		EzfDs ds = new EzfDs();
		ds.setDsId( form.getDsId() );
		ds = this.ezfDsService.selectByUniqueKey(ds).getValueEmptyThrowMessage();
		DefaultResult<EzfMap> mResult = this.ezfMapService.insert(form);
		form = mResult.getValueEmptyThrowMessage();
		for (EzfMapGrd grid : form.getGrids()) {					
			if (StringUtils.isBlank(grid.getDtlTbl())) { // 沒有detail 資料表, 不處理grid配置
				logger.warn(grid.getGridId() + " 沒有輸入 Sub table");
				continue;
			}
			grid.setCnfId( form.getCnfId() );
			grid = this.ezfMapGrdService.insert(grid).getValueEmptyThrowMessage();
			for (EzfMapField field : grid.getItems()) {
				if (StringUtils.isBlank(field.getTblField())) {
					continue;
				}
				field.setGridId(grid.getGridId());
				field.setCnfId(form.getCnfId());
				this.ezfMapFieldService.insert(field);
			}
			for (EzfMapGrdTblMp tblMp : grid.getTblmps()) {
				if (StringUtils.isBlank(tblMp.getMstFieldName()) || StringUtils.isBlank(tblMp.getDtlFieldName())) {
					throw new ServiceException(grid.getGridId() + " 沒有輸入主表與明細表where條件欄位");
				}
				tblMp.setGridId(grid.getGridId());
				tblMp.setCnfId(form.getCnfId());				
				this.ezfMapGrdTblMpService.insert(tblMp);
			}
		}
		int baseRecord = 0;
		for (EzfMapField field : form.getFields()) {
			if (StringUtils.isBlank(field.getTblField())) {
				continue;
			}
			field.setGridId(YesNo.NO);
			field.setCnfId(form.getCnfId());			
			this.ezfMapFieldService.insert(field);
			baseRecord++;
		}
		if (baseRecord < 1) {
			throw new ServiceException( "最少需輸入一筆field對應配置" );
		}
		return mResult;
	}

	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<EzfMap> update(EzfMap form) throws ServiceException, Exception {
		
		return null;
	}
	
}
