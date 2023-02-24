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
