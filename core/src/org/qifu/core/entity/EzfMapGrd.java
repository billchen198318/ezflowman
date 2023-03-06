package org.qifu.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class EzfMapGrd implements java.io.Serializable {
	private static final long serialVersionUID = -399913644009145212L;
	
	private String oid;
    private String cnfId;
    private String gridId;
    private String recordId;
    private String dtlTbl;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    // 頁面用
    private List<EzfMapField> items = new ArrayList<EzfMapField>();
    
    @EntityPK(name = "oid", autoUUID = true)
    public String getOid() {
        return oid;
    }
    
    public void setOid(String oid) {
        this.oid = oid;
    }
    
    @EntityUK(name = "cnfId")
    public String getCnfId() {
        return cnfId;
    }
    
    public void setCnfId(String cnfId) {
        this.cnfId = cnfId;
    }
    
    @EntityUK(name = "gridId")
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
    
    public String getDtlTbl() {
        return dtlTbl;
    }
    
    public void setDtlTbl(String dtlTbl) {
        this.dtlTbl = dtlTbl;
    }
    
    @CreateUserField(name = "cuserid")
    public String getCuserid() {
        return cuserid;
    }
    
    public void setCuserid(String cuserid) {
        this.cuserid = cuserid;
    }
    
    @CreateDateField(name = "cdate")
    public Date getCdate() {
        return cdate;
    }
    
    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
    
    @UpdateUserField(name = "uuserid")
    public String getUuserid() {
        return uuserid;
    }
    
    public void setUuserid(String uuserid) {
        this.uuserid = uuserid;
    }
    
    @UpdateDateField(name = "udate")
    public Date getUdate() {
        return udate;
    }
    
    public void setUdate(Date udate) {
        this.udate = udate;
    }

	public List<EzfMapField> getItems() {
		return items;
	}

	public void setItems(List<EzfMapField> items) {
		this.items = items;
	}
	
}
