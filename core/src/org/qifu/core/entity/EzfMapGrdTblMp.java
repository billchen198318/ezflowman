package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class EzfMapGrdTblMp implements java.io.Serializable {
	private static final long serialVersionUID = -5490742087059105839L;
	
	private String oid;
    private String cnfId;
    private String gridId;
    private String mstFieldName;
    private String dtlFieldName;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    @EntityPK(name = "oid", autoUUID = true)
    public String getOid() {
        return oid;
    }
    
    public void setOid(String oid) {
        this.oid = oid;
    }
    
    public String getCnfId() {
        return cnfId;
    }
    
    public void setCnfId(String cnfId) {
        this.cnfId = cnfId;
    }
    
    public String getGridId() {
        return gridId;
    }
    
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }
    
    public String getMstFieldName() {
        return mstFieldName;
    }
    
    public void setMstFieldName(String mstFieldName) {
        this.mstFieldName = mstFieldName;
    }
    
    public String getDtlFieldName() {
        return dtlFieldName;
    }
    
    public void setDtlFieldName(String dtlFieldName) {
        this.dtlFieldName = dtlFieldName;
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
	
}
