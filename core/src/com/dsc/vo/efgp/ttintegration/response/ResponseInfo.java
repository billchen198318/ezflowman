//
// 此檔案是由 Eclipse Implementation of JAXB, v2.3.4 所產生 
// 請參閱 https://eclipse-ee4j.github.io/jaxb-ri 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2022.01.12 於 11:47:49 AM CST 
//


package com.dsc.vo.efgp.ttintegration.response;

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
 *         &lt;element ref="{}SenderIP"/&gt;
 *         &lt;element ref="{}ReceiverIP"/&gt;
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
    "senderIP",
    "receiverIP"
})
@XmlRootElement(name = "ResponseInfo")
public class ResponseInfo {

    @XmlElement(name = "SenderIP", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String senderIP;
    @XmlElement(name = "ReceiverIP", required = true)
    protected String receiverIP;

    /**
     * 取得 senderIP 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderIP() {
        return senderIP;
    }

    /**
     * 設定 senderIP 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderIP(String value) {
        this.senderIP = value;
    }

    /**
     * 取得 receiverIP 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverIP() {
        return receiverIP;
    }

    /**
     * 設定 receiverIP 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverIP(String value) {
        this.receiverIP = value;
    }

}
