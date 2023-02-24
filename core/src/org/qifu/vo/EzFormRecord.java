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

import java.util.LinkedList;
import java.util.List;

public class EzFormRecord implements java.io.Serializable {
	private static final long serialVersionUID = -8626195011633314297L;
	
	private String gridId;
	
	private String recordId; // 不一定有值
	
	private List<EzFormRecordItem> items = new LinkedList<EzFormRecordItem>();

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public List<EzFormRecordItem> getItems() {
		return items;
	}

	public void setItems(List<EzFormRecordItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "EzFormRecord [gridId=" + gridId + ", recordId=" + recordId + ", items=" + items + "]";
	}
	
}
