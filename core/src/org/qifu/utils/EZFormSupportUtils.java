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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.vo.EzForm;
import org.qifu.vo.EzFormField;
import org.qifu.vo.EzFormGrid;
import org.qifu.vo.EzFormRecord;
import org.qifu.vo.EzFormRecordItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.underscore.U;

public class EZFormSupportUtils implements java.io.Serializable {
	private static final long serialVersionUID = -7032600196907396427L;
	
	private static final String _omitXmlDeclaration = "#omit-xml-declaration";
	
	private static final String _element_records = "records";
	private static final String _element_record = "record";
	private static final String _element_item = "item";
	
	private static final String _attribute_id = "-id";
	private static final String _attribute_dataType = "-dataType";
	private static final String _attribute_perDataProId = "-perDataProId";
	private static final String _attribute_text = "#text";
	
	public static EzForm loadFromXml(String formXmlContent) throws Exception {
		String underscoreJsonStr = U.xmlToJson( formXmlContent );
		String underscoreXmlStr = U.jsonToXml( underscoreJsonStr ); // test convert to xml		
		Map<String, Object> formFieldMap = new ObjectMapper().readValue(underscoreJsonStr, HashMap.class);
		EzForm ezform = null;
        for (Map.Entry<String, Object> fEntry : formFieldMap.entrySet()) {        	
        	String formId = fEntry.getKey();
        	if (_omitXmlDeclaration.equals(formId)) {
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
        			if (l1Map.get(_element_records) != null && l1Map.get(_element_records) instanceof Map) {
        				Map<String, Object> l2Map = (Map<String, Object>) l1Map.get(_element_records);        				
        				if ( l2Map.get(_element_record) != null && l2Map.get(_element_record) instanceof Map ) {
    						EzFormGrid eGrid = new EzFormGrid();
    						eGrid.setGridId( l1Entry.getKey() );        					
        					Map<String, Object> recordMap = (Map<String, Object>) l2Map.get(_element_record);
        					if (recordMap.get(_element_item) != null && recordMap.get(_element_item) instanceof List) { 						
        						List< Map<String, Object> > itemList = (List<Map<String, Object>>) recordMap.get(_element_item);
        						if (!CollectionUtils.isEmpty(itemList)) {
        							EzFormRecord fr = new EzFormRecord();        							
        							fr.setRecordId( StringUtils.defaultString((String)recordMap.get(_attribute_id)) );        							
        							for (Map<String, Object> itemMap : itemList) {
        								EzFormRecordItem ri = new EzFormRecordItem();
        								ri.setId( StringUtils.defaultString((String)itemMap.get(_attribute_id)) );
        								ri.setDataType( StringUtils.defaultString((String)itemMap.get(_attribute_dataType)) );
        								ri.setPerDataProId( StringUtils.defaultString((String)itemMap.get(_attribute_perDataProId)) );
        								ri.setText( StringUtils.defaultString((String)itemMap.get(_attribute_text)) );
        								fr.getItems().add(ri);
        							}
        							eGrid.getRecords().add(fr);
        							isGrid = true;
        						}
        					}
    						if (isGrid) {
    							ezform.getGrids().add(eGrid);
    						}        					
        				}   
        				
        				if ( l2Map.get(_element_record) != null && l2Map.get(_element_record) instanceof List ) { // 轉資料成拋送時, 會是多筆 List
        					List<Map<String, Object>> recordMapList = (List<Map<String, Object>>) l2Map.get(_element_record);
    						EzFormGrid eGrid = new EzFormGrid();
    						eGrid.setGridId( l1Entry.getKey() );        					
        					for (Map<String, Object> recordMap : recordMapList) {        						
            					if (recordMap.get(_element_item) != null && recordMap.get(_element_item) instanceof List) {
            						List< Map<String, Object> > itemList = (List<Map<String, Object>>) recordMap.get(_element_item);
            						if (!CollectionUtils.isEmpty(itemList)) {
            							EzFormRecord fr = new EzFormRecord();        							
            							fr.setRecordId( StringUtils.defaultString((String)recordMap.get(_attribute_id)) );        							
            							for (Map<String, Object> itemMap : itemList) {
            								EzFormRecordItem ri = new EzFormRecordItem();
            								ri.setId( StringUtils.defaultString((String)itemMap.get(_attribute_id)) );
            								ri.setDataType( StringUtils.defaultString((String)itemMap.get(_attribute_dataType)) );
            								ri.setPerDataProId( StringUtils.defaultString((String)itemMap.get(_attribute_perDataProId)) );
            								ri.setText( StringUtils.defaultString((String)itemMap.get(_attribute_text)) );
            								fr.getItems().add(ri);
            							}
            							eGrid.getRecords().add(fr);
            							isGrid = true;
            						}  
            					}
        					}
           					if (isGrid) {
        						ezform.getGrids().add(eGrid);
        					}        					
        				}
        				
        			}
        			// -------------------------------------------------------------------------------------
        			
        			// -------------------------------------------------------------------------------------
        			// 處理一般 field
        			if (!isGrid) {
        				if (l1Map.get(_attribute_id) != null && l1Map.get("-dataType") != null) {
        					String fieldId = StringUtils.defaultString((String)l1Map.get(_attribute_id));
        					if (StringUtils.isBlank(fieldId)) {
        						throw new Exception("form content field no id value.");
        					}        					
        					EzFormField ff = new EzFormField();        					
        					ff.setId( fieldId );
        					ff.setDataType( StringUtils.defaultString((String)l1Map.get(_attribute_dataType)) );
        					ff.setPerDataProId( StringUtils.defaultString((String)l1Map.get(_attribute_perDataProId)) );
        					ff.setText( StringUtils.defaultString((String)l1Map.get(_attribute_text)) );
        					ezform.getFields().add(ff);        					
        				}        				
        			}
        			// -------------------------------------------------------------------------------------
        			
        		}
        	}
        }				
		return ezform;
	}
	
	public static Map<String, Object> loadFromObject(EzForm formObj) throws Exception {
		if (null == formObj) {
			throw new Exception(BaseSystemMessage.objectNull());
		}
		if (CollectionUtils.isEmpty(formObj.getFields())) {
			throw new Exception("no field list value.");
		}
		if (StringUtils.isBlank(formObj.getFormId())) {
			throw new Exception("parameter object no form id.");
		}
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("#omit-xml-declaration", "yes");		
		Map<String, Object> formMap = new HashMap<String, Object>();
		dataMap.put(formObj.getFormId(), formMap);		
		for (EzFormField field : formObj.getFields()) {
			if (StringUtils.isBlank(field.getId())) {
				throw new Exception("form field no id.");
			}
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			formMap.put(field.getId(), fieldMap);
			fieldMap.put(_attribute_id, field.getId());
			if (!StringUtils.isBlank(field.getDataType())) {
				fieldMap.put(_attribute_dataType, field.getDataType());
			}			
			if (!StringUtils.isBlank(field.getPerDataProId())) {
				fieldMap.put(_attribute_perDataProId, field.getPerDataProId());
			}			
			if (!StringUtils.isBlank(field.getText())) {
				fieldMap.put(_attribute_text, field.getText());
			}
		}
		for (int g = 0; formObj.getGrids() != null && g < formObj.getGrids().size(); g++) {
			EzFormGrid eGrid = formObj.getGrids().get(g);
			if ( StringUtils.isBlank(eGrid.getGridId()) ) {
				throw new Exception("form grid no id.");
			}
			if (CollectionUtils.isEmpty(eGrid.getRecords())) {
				throw new Exception("form grid-" + eGrid.getGridId() + " no record list.");
			}
			Map<String, Object> gridMap = new HashMap<String, Object>();
			gridMap.put(_attribute_id, eGrid.getGridId());
			formMap.put(eGrid.getGridId(), gridMap);
			Map<String, Object> recordsMap = new HashMap<String, Object>();
			gridMap.put(_element_records, recordsMap);
			List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
			recordsMap.put(_element_record, recordList);
			for (int r = 0; r < eGrid.getRecords().size(); r++) {
				EzFormRecord record = eGrid.getRecords().get(r);
				Map<String, Object> recordMap = new HashMap<String, Object>();
				recordMap.put(_attribute_id, record.getRecordId());				
				List< Map<String, Object> > itemList = new ArrayList< Map<String, Object> >();
				for (int i = 0; i < record.getItems().size(); i++) {					
					EzFormRecordItem itemObj = record.getItems().get(i);
					if (StringUtils.isBlank(itemObj.getId())) {
						throw new Exception("form grid-" + eGrid.getGridId() + " , item index " + i + " no id.");
					}				
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put(_attribute_id, itemObj.getId());
					if (!StringUtils.isBlank(itemObj.getDataType())) {
						itemMap.put(_attribute_dataType, itemObj.getDataType());
					}				
					if (!StringUtils.isBlank(itemObj.getPerDataProId())) {
						itemMap.put(_attribute_perDataProId, itemObj.getPerDataProId());
					}
					if (!StringUtils.isBlank(itemObj.getText())) {
						itemMap.put(_attribute_text, itemObj.getText());
					}
					itemList.add(itemMap);
				}				
				recordMap.put(_element_item, itemList);
				recordList.add(recordMap);
			}			
		}
		/*
		System.out.println("##############################################################################");
		System.out.println( dataMap );
		System.out.println("##############################################################################");
		*/
		return dataMap;
	}
	
	public static String loadJsonFromObject(EzForm formObj) throws Exception {
		Map<String, Object> dataMap = loadFromObject(formObj);		
		return new ObjectMapper().writeValueAsString(dataMap);
	}
	
	public static String loadXmlFromObject(EzForm formObj) throws Exception {
		String jsonStr = loadJsonFromObject(formObj);
		return U.jsonToXml( jsonStr );
	}	
	
}
