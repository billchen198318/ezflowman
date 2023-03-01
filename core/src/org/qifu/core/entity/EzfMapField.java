package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class EzfMapField implements java.io.Serializable {
	private static final long serialVersionUID = 2560053098612401927L;
	
	private String oid;
    private String cnfId;
    private String gridId;
    private String formField;
    private String tblField;
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
    
    public String getFormField() {
        return formField;
    }
    
    public void setFormField(String formField) {
        this.formField = formField;
    }
    
    public String getTblField() {
        return tblField;
    }
    
    public void setTblField(String tblField) {
        this.tblField = tblField;
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
