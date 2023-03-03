package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class EzfMap implements java.io.Serializable {
	private static final long serialVersionUID = 7861925800362101778L;
	
	private String oid;
    private String cnfId;
    private String cnfName;
    private String dsId;
    private String efgpPkgId;
    private String mainTbl;
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
    
    @EntityUK(name = "cnfId")
    public String getCnfId() {
        return cnfId;
    }
    
    public void setCnfId(String cnfId) {
        this.cnfId = cnfId;
    }
    
    public String getCnfName() {
        return cnfName;
    }
    
    public void setCnfName(String cnfName) {
        this.cnfName = cnfName;
    }
    
    public String getDsId() {
        return dsId;
    }
    
    public void setDsId(String dsId) {
        this.dsId = dsId;
    }
    
    public String getEfgpPkgId() {
		return efgpPkgId;
	}
    
	public void setEfgpPkgId(String efgpPkgId) {
		this.efgpPkgId = efgpPkgId;
	}
	
	public String getMainTbl() {
        return mainTbl;
    }
    
    public void setMainTbl(String mainTbl) {
        this.mainTbl = mainTbl;
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
