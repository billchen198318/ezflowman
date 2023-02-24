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
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}organizationUnits"/&gt;
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
    "organizationUnits"
})
@XmlRootElement(name = "com.dsc.nana.services.webservice.OrganizationUnitCollection")
public class ComDscNanaServicesWebserviceOrganizationUnitCollection {

    @XmlElement(required = true)
    protected OrganizationUnits organizationUnits;

    /**
     * 取得 organizationUnits 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link OrganizationUnits }
     *     
     */
    public OrganizationUnits getOrganizationUnits() {
        return organizationUnits;
    }

    /**
     * 設定 organizationUnits 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganizationUnits }
     *     
     */
    public void setOrganizationUnits(OrganizationUnits value) {
        this.organizationUnits = value;
    }

}
