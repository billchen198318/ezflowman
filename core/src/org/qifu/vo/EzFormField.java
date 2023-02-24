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
package org.qifu.vo;

public class EzFormField implements java.io.Serializable {
	private static final long serialVersionUID = 3790515958607287967L;
	
	private String id;
	private String dataType;
	private String perDataProId;
	private String text; // xml 的 value	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getPerDataProId() {
		return perDataProId;
	}
	
	public void setPerDataProId(String perDataProId) {
		this.perDataProId = perDataProId;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "EzFormField [id=" + id + ", dataType=" + dataType + ", perDataProId=" + perDataProId + ", text=" + text
				+ "]";
	}
	
}
