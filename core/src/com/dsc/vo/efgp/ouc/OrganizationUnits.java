//
// 此檔案是由 Eclipse Implementation of JAXB, v2.3.4 所產生 
// 請參閱 https://eclipse-ee4j.github.io/jaxb-ri 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2021.12.09 於 01:29:17 PM CST 
//


package com.dsc.vo.efgp.ouc;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}com.dsc.nana.data__transfer.OrganizationUnitForInvokingListDTO" maxOccurs="unbounded"/&gt;
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
    "comDscNanaDataTransferOrganizationUnitForInvokingListDTO"
})
@XmlRootElement(name = "organizationUnits")
public class OrganizationUnits {

    @XmlElement(name = "com.dsc.nana.data__transfer.OrganizationUnitForInvokingListDTO", required = true)
    protected List<ComDscNanaDataTransferOrganizationUnitForInvokingListDTO> comDscNanaDataTransferOrganizationUnitForInvokingListDTO;

    /**
     * Gets the value of the comDscNanaDataTransferOrganizationUnitForInvokingListDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comDscNanaDataTransferOrganizationUnitForInvokingListDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComDscNanaDataTransferOrganizationUnitForInvokingListDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComDscNanaDataTransferOrganizationUnitForInvokingListDTO }
     * 
     * 
     */
    public List<ComDscNanaDataTransferOrganizationUnitForInvokingListDTO> getComDscNanaDataTransferOrganizationUnitForInvokingListDTO() {
        if (comDscNanaDataTransferOrganizationUnitForInvokingListDTO == null) {
            comDscNanaDataTransferOrganizationUnitForInvokingListDTO = new ArrayList<ComDscNanaDataTransferOrganizationUnitForInvokingListDTO>();
        }
        return this.comDscNanaDataTransferOrganizationUnitForInvokingListDTO;
    }

}
