//
// 此檔案是由 Eclipse Implementation of JAXB, v2.3.4 所產生 
// 請參閱 https://eclipse-ee4j.github.io/jaxb-ri 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2021.12.09 於 01:29:17 PM CST 
//


package com.dsc.vo.efgp.ouc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>anonymous complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}OID"/&gt;
 *         &lt;element ref="{}id"/&gt;
 *         &lt;element ref="{}name"/&gt;
 *         &lt;element ref="{}orgUnitType"/&gt;
 *         &lt;element ref="{}orgName"/&gt;
 *         &lt;element ref="{}isMain"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "oid",
    "id",
    "name",
    "orgUnitType",
    "orgName",
    "isMain"
})
@XmlRootElement(name = "com.dsc.nana.data__transfer.OrganizationUnitForInvokingListDTO")
public class ComDscNanaDataTransferOrganizationUnitForInvokingListDTO {

    @XmlElement(name = "OID", required = true)
    protected String oid;
    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected OrgUnitType orgUnitType;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String orgName;
    protected boolean isMain;

    /**
     * 取得 oid 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOID() {
        return oid;
    }

    /**
     * 設定 oid 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOID(String value) {
        this.oid = value;
    }

    /**
     * 取得 id 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * 設定 id 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * 取得 name 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 設定 name 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 取得 orgUnitType 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link OrgUnitType }
     *     
     */
    public OrgUnitType getOrgUnitType() {
        return orgUnitType;
    }

    /**
     * 設定 orgUnitType 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link OrgUnitType }
     *     
     */
    public void setOrgUnitType(OrgUnitType value) {
        this.orgUnitType = value;
    }

    /**
     * 取得 orgName 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 設定 orgName 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgName(String value) {
        this.orgName = value;
    }

    /**
     * 取得 isMain 特性的值.
     * 
     */
    public boolean isIsMain() {
        return isMain;
    }

    /**
     * 設定 isMain 特性的值.
     * 
     */
    public void setIsMain(boolean value) {
        this.isMain = value;
    }

}
