//
// 此檔案是由 Eclipse Implementation of JAXB, v2.3.4 所產生 
// 請參閱 https://eclipse-ee4j.github.io/jaxb-ri 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2022.01.04 於 11:28:26 AM CST 
//


package com.dsc.vo.tiptop.response;

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
 *         &lt;element ref="{}Execution"/&gt;
 *         &lt;element ref="{}ResponseContent"/&gt;
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
    "execution",
    "responseContent"
})
@XmlRootElement(name = "Response")
public class Response {

    @XmlElement(name = "Execution", required = true)
    protected Execution execution;
    @XmlElement(name = "ResponseContent", required = true)
    protected ResponseContent responseContent;

    /**
     * 取得 execution 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Execution }
     *     
     */
    public Execution getExecution() {
        return execution;
    }

    /**
     * 設定 execution 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Execution }
     *     
     */
    public void setExecution(Execution value) {
        this.execution = value;
    }

    /**
     * 取得 responseContent 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link ResponseContent }
     *     
     */
    public ResponseContent getResponseContent() {
        return responseContent;
    }

    /**
     * 設定 responseContent 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseContent }
     *     
     */
    public void setResponseContent(ResponseContent value) {
        this.responseContent = value;
    }

}
