package org.qifu.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.qifu.vo.EzForm;
import org.qifu.vo.EzFormField;
import org.qifu.vo.EzFormRecord;
import org.qifu.vo.EzFormRecordItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.underscore.U;

public class TestXmlToJson {
	
	public static void main(String args[]) throws Exception {
		
		//String testXml = "aimi150.xml";
		//String testXml = "apmt540.xml";
		String testXml = "sign_form.xml";
		//String testXml = "Car_form.xml";
		//String testXml = "Car_form-TEST.xml";
		
		String formXmlContent = FileUtils.readFileToString(new File("/home/git5/ezflowman/doc/"+testXml), "utf-8");		
        
		/*
		JSONObject xmlJSONObj = XML.toJSONObject(formXmlContent);                
        System.out.println("--------------------------------------------------------------------");
        System.out.println( xmlJSONObj );		
        System.out.println("--------------------------------------------------------------------");
		
        String json2xml = XML.toString( xmlJSONObj );
        System.out.println( json2xml );
        */
		
		String jsonStr = U.xmlToJson( formXmlContent );
        System.out.println("--------------------------------------------------------------------");
        System.out.println( jsonStr );		
        System.out.println("--------------------------------------------------------------------");        
        
        String xmlStr = U.jsonToXml( jsonStr );
        System.out.println( xmlStr );
        
        
        
        Map<String, Object> result = new ObjectMapper().readValue(jsonStr, HashMap.class);
        System.out.println( result );
        
        // to EZFormField object
        for (Map.Entry<String, Object> fEntry : result.entrySet()) {        	
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
        	
        	
        	EzForm ezform = new EzForm();
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
        							System.out.println("is grid ===>" + l1Entry.getKey() );
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
        	
        	
        	System.out.println("--------------------------------------------------------------------");
        	System.out.println("EzForm object===>");
        	System.out.println(ezform.toString());
        	
        	
        }
        
        
        
	}
	
}
