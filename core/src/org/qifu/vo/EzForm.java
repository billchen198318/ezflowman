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

import java.util.ArrayList;
import java.util.List;

public class EzForm implements java.io.Serializable {
	private static final long serialVersionUID = -6303829278492712581L;
	
	private String formId;
	
	private List<EzFormField> fields = new ArrayList<EzFormField>();
	
	private List<EzFormGrid> grids = new ArrayList<EzFormGrid>();

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<EzFormField> getFields() {
		return fields;
	}

	public void setFields(List<EzFormField> fields) {
		this.fields = fields;
	}

	public List<EzFormGrid> getGrids() {
		return grids;
	}

	public void setGrids(List<EzFormGrid> grids) {
		this.grids = grids;
	}

	@Override
	public String toString() {
		return "EzForm [formId=" + formId + ", fields=" + fields + ", grids=" + grids + "]";
	}
		
}
