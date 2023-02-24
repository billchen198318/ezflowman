package org.qifu.vo;

import java.util.ArrayList;
import java.util.List;

public class EzForm implements java.io.Serializable {
	private static final long serialVersionUID = -6303829278492712581L;
	
	private String formId;
	
	private List<EzFormField> fields = new ArrayList<EzFormField>();
	
	private List<EzFormRecord> records = new ArrayList<EzFormRecord>();

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

	public List<EzFormRecord> getRecords() {
		return records;
	}

	public void setRecords(List<EzFormRecord> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "EzForm [formId=" + formId + ", fields=" + fields + ", records=" + records + "]";
	}
	
}
