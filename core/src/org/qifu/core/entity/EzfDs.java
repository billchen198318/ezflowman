package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class EzfDs implements java.io.Serializable {
	private static final long serialVersionUID = -744495999982090119L;
	
	private String oid;
    private String dsId;
    private String dsName;
    private String driverType;
    private String dbAddr;
    private String dbUser;
    private String dbPasswd;
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
    
    @EntityUK(name = "dsId")
    public String getDsId() {
        return dsId;
    }
    
    public void setDsId(String dsId) {
        this.dsId = dsId;
    }
    
    public String getDsName() {
        return dsName;
    }
    
    public void setDsName(String dsName) {
        this.dsName = dsName;
    }
    
    public String getDriverType() {
        return driverType;
    }
    
    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }
    
    public String getDbAddr() {
        return dbAddr;
    }
    
    public void setDbAddr(String dbAddr) {
        this.dbAddr = dbAddr;
    }
    
    public String getDbUser() {
        return dbUser;
    }
    
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }
    
    public String getDbPasswd() {
        return dbPasswd;
    }
    
    public void setDbPasswd(String dbPasswd) {
        this.dbPasswd = dbPasswd;
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
