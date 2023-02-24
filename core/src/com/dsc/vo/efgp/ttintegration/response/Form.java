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
 *         &lt;element ref="{}StatSlip"/&gt;
 *         &lt;element ref="{}PlantID"/&gt;
 *         &lt;element ref="{}ProgramID"/&gt;
 *         &lt;element ref="{}SourceFormID"/&gt;
 *         &lt;element ref="{}SourceFormNum"/&gt;
 *         &lt;element ref="{}Date"/&gt;
 *         &lt;element ref="{}Time"/&gt;
 *         &lt;element ref="{}Status"/&gt;
 *         &lt;element ref="{}FormCreatorID"/&gt;
 *         &lt;element ref="{}FormOwnerID"/&gt;
 *         &lt;element ref="{}TargetFormID"/&gt;
 *         &lt;element ref="{}TargetSheetNo"/&gt;
 *         &lt;element ref="{}ProcessSNo"/&gt;
 *         &lt;element ref="{}Description"/&gt;
 *         &lt;element ref="{}ActionStatus"/&gt;
 *         &lt;element ref="{}ActionDescribe"/&gt;
 *         &lt;element ref="{}TPServerIP"/&gt;
 *         &lt;element ref="{}TPServerEnv"/&gt;
 *         &lt;element ref="{}SourceFormKey3"/&gt;
 *         &lt;element ref="{}SourceFormKey4"/&gt;
 *         &lt;element ref="{}SourceFormKey5"/&gt;
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
    "statSlip",
    "plantID",
    "programID",
    "sourceFormID",
    "sourceFormNum",
    "date",
    "time",
    "status",
    "formCreatorID",
    "formOwnerID",
    "targetFormID",
    "targetSheetNo",
    "processSNo",
    "description",
    "actionStatus",
    "actionDescribe",
    "tpServerIP",
    "tpServerEnv",
    "sourceFormKey3",
    "sourceFormKey4",
    "sourceFormKey5"
})
@XmlRootElement(name = "Form")
public class Form {

    @XmlElement(name = "StatSlip", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String statSlip;
    @XmlElement(name = "PlantID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String plantID;
    @XmlElement(name = "ProgramID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String programID;
    @XmlElement(name = "SourceFormID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sourceFormID;
    @XmlElement(name = "SourceFormNum", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sourceFormNum;
    @XmlElement(name = "Date", required = true)
    protected String date;
    @XmlElement(name = "Time", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String time;
    @XmlElement(name = "Status", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String status;
    @XmlElement(name = "FormCreatorID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String formCreatorID;
    @XmlElement(name = "FormOwnerID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String formOwnerID;
    @XmlElement(name = "TargetFormID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String targetFormID;
    @XmlElement(name = "TargetSheetNo", required = true)
    protected String targetSheetNo;
    @XmlElement(name = "ProcessSNo", required = true)
    protected String processSNo;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "ActionStatus", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String actionStatus;
    @XmlElement(name = "ActionDescribe", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String actionDescribe;
    @XmlElement(name = "TPServerIP", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String tpServerIP;
    @XmlElement(name = "TPServerEnv", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String tpServerEnv;
    @XmlElement(name = "SourceFormKey3", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sourceFormKey3;
    @XmlElement(name = "SourceFormKey4", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sourceFormKey4;
    @XmlElement(name = "SourceFormKey5", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sourceFormKey5;

    /**
     * 取得 statSlip 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatSlip() {
        return statSlip;
    }

    /**
     * 設定 statSlip 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatSlip(String value) {
        this.statSlip = value;
    }

    /**
     * 取得 plantID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlantID() {
        return plantID;
    }

    /**
     * 設定 plantID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlantID(String value) {
        this.plantID = value;
    }

    /**
     * 取得 programID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgramID() {
        return programID;
    }

    /**
     * 設定 programID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgramID(String value) {
        this.programID = value;
    }

    /**
     * 取得 sourceFormID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceFormID() {
        return sourceFormID;
    }

    /**
     * 設定 sourceFormID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFormID(String value) {
        this.sourceFormID = value;
    }

    /**
     * 取得 sourceFormNum 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceFormNum() {
        return sourceFormNum;
    }

    /**
     * 設定 sourceFormNum 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFormNum(String value) {
        this.sourceFormNum = value;
    }

    /**
     * 取得 date 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * 設定 date 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * 取得 time 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTime() {
        return time;
    }

    /**
     * 設定 time 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTime(String value) {
        this.time = value;
    }

    /**
     * 取得 status 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * 設定 status 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * 取得 formCreatorID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormCreatorID() {
        return formCreatorID;
    }

    /**
     * 設定 formCreatorID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormCreatorID(String value) {
        this.formCreatorID = value;
    }

    /**
     * 取得 formOwnerID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormOwnerID() {
        return formOwnerID;
    }

    /**
     * 設定 formOwnerID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormOwnerID(String value) {
        this.formOwnerID = value;
    }

    /**
     * 取得 targetFormID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetFormID() {
        return targetFormID;
    }

    /**
     * 設定 targetFormID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetFormID(String value) {
        this.targetFormID = value;
    }

    /**
     * 取得 targetSheetNo 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetSheetNo() {
        return targetSheetNo;
    }

    /**
     * 設定 targetSheetNo 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetSheetNo(String value) {
        this.targetSheetNo = value;
    }

    /**
     * 取得 processSNo 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessSNo() {
        return processSNo;
    }

    /**
     * 設定 processSNo 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessSNo(String value) {
        this.processSNo = value;
    }

    /**
     * 取得 description 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * 設定 description 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * 取得 actionStatus 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionStatus() {
        return actionStatus;
    }

    /**
     * 設定 actionStatus 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionStatus(String value) {
        this.actionStatus = value;
    }

    /**
     * 取得 actionDescribe 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionDescribe() {
        return actionDescribe;
    }

    /**
     * 設定 actionDescribe 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionDescribe(String value) {
        this.actionDescribe = value;
    }

    /**
     * 取得 tpServerIP 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTPServerIP() {
        return tpServerIP;
    }

    /**
     * 設定 tpServerIP 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTPServerIP(String value) {
        this.tpServerIP = value;
    }

    /**
     * 取得 tpServerEnv 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTPServerEnv() {
        return tpServerEnv;
    }

    /**
     * 設定 tpServerEnv 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTPServerEnv(String value) {
        this.tpServerEnv = value;
    }

    /**
     * 取得 sourceFormKey3 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceFormKey3() {
        return sourceFormKey3;
    }

    /**
     * 設定 sourceFormKey3 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFormKey3(String value) {
        this.sourceFormKey3 = value;
    }

    /**
     * 取得 sourceFormKey4 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceFormKey4() {
        return sourceFormKey4;
    }

    /**
     * 設定 sourceFormKey4 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFormKey4(String value) {
        this.sourceFormKey4 = value;
    }

    /**
     * 取得 sourceFormKey5 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceFormKey5() {
        return sourceFormKey5;
    }

    /**
     * 設定 sourceFormKey5 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFormKey5(String value) {
        this.sourceFormKey5 = value;
    }

}
