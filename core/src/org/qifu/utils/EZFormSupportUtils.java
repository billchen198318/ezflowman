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
package org.qifu.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.vo.EzForm;
import org.qifu.vo.EzFormField;
import org.qifu.vo.EzFormRecord;
import org.qifu.vo.EzFormRecordItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.underscore.U;

public class EZFormSupportUtils implements java.io.Serializable {
	private static final long serialVersionUID = -7032600196907396427L;
	
	public static EzForm loadFromXml(String formXmlContent) throws Exception {
		String underscoreJsonStr = U.xmlToJson( formXmlContent );
		String underscoreXmlStr = U.jsonToXml( underscoreJsonStr ); // test convert to xml		
		Map<String, Object> formFieldMap = new ObjectMapper().readValue(underscoreJsonStr, HashMap.class);
		EzForm ezform = null;
        for (Map.Entry<String, Object> fEntry : formFieldMap.entrySet()) {        	
        	String formId = fEntry.getKey();
        	if ("#omit-xml-declaration".equals(formId)) {
        		continue;
        	}
        	if (StringUtils.isBlank(formId)) {
        		throw new Exception("no find form Id.");
        	}
        	Map<String, Object> fMap = (Map<String, Object>) fEntry.getValue();
        	if (MapUtils.isEmpty(fMap)) {
        		throw new Exception("form content no field.");
        	}     
        	ezform = new EzForm();
        	ezform.setFormId(formId);        	
        	for (Map.Entry<String, Object> l1Entry : fMap.entrySet()) {
        		if (l1Entry.getValue() != null && l1Entry.getValue() instanceof Map) {        			
        			Map<String, Object> l1Map = (Map<String, Object>) l1Entry.getValue();
        			
        			// -------------------------------------------------------------------------------------
        			// 處理grid
        			boolean isGrid = false;
        			if (l1Map.get("records") != null && l1Map.get("records") instanceof Map) {
        				Map<String, Object> l2Map = (Map<String, Object>) l1Map.get("records");
        				if ( l2Map.get("record") != null && l2Map.get("record") instanceof Map ) {
        					Map<String, Object> recordMap = (Map<String, Object>) l2Map.get("record");
        					if (recordMap.get("item") != null && recordMap.get("item") instanceof List) {        						
        						List< Map<String, Object> > itemList = (List<Map<String, Object>>) recordMap.get("item");
        						if (!CollectionUtils.isEmpty(itemList)) {
        							//System.out.println("is grid ===>" + l1Entry.getKey() );
        							EzFormRecord fr = new EzFormRecord();
        							fr.setGridId( l1Entry.getKey() );
        							fr.setRecordId( StringUtils.defaultString((String)recordMap.get("-id")) );        							
        							for (Map<String, Object> itemMap : itemList) {
        								EzFormRecordItem ri = new EzFormRecordItem();
        								ri.setId( StringUtils.defaultString((String)itemMap.get("-id")) );
        								ri.setDataType( StringUtils.defaultString((String)itemMap.get("-dataType")) );
        								ri.setPerDataProId( StringUtils.defaultString((String)itemMap.get("-perDataProId")) );
        								ri.setText( StringUtils.defaultString((String)itemMap.get("#text")) );
        								fr.getItems().add(ri);
        							}
        							ezform.getRecords().add(fr);
        							isGrid = true;
        						}            					
        					}
        				}        				
        			}
        			// -------------------------------------------------------------------------------------
        			
        			// -------------------------------------------------------------------------------------
        			// 處理一般 field
        			if (!isGrid) {
        				if (l1Map.get("-id") != null && l1Map.get("-dataType") != null) {
        					String fieldId = StringUtils.defaultString((String)l1Map.get("-id"));
        					if (StringUtils.isBlank(fieldId)) {
        						throw new Exception("form content field no id value.");
        					}        					
        					EzFormField ff = new EzFormField();        					
        					ff.setId( fieldId );
        					ff.setDataType( StringUtils.defaultString((String)l1Map.get("-dataType")) );
        					ff.setPerDataProId( StringUtils.defaultString((String)l1Map.get("-perDataProId")) );
        					ff.setText( StringUtils.defaultString((String)l1Map.get("#text")) );
        					ezform.getFields().add(ff);        					
        				}        				
        			}
        			// -------------------------------------------------------------------------------------
        			
        		}
        	}
        }				
		return ezform;
	}

}
