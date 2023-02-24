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
