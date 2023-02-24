//
// 此檔案是由 Eclipse Implementation of JAXB, v2.3.4 所產生 
// 請參閱 https://eclipse-ee4j.github.io/jaxb-ri 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2021.12.09 於 01:29:17 PM CST 
//


package com.dsc.vo.efgp.ouc;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.dsc.vo.efgp.ouc package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OID_QNAME = new QName("", "OID");
    private final static QName _Id_QNAME = new QName("", "id");
    private final static QName _Name_QNAME = new QName("", "name");
    private final static QName _Value_QNAME = new QName("", "value");
    private final static QName _OrgName_QNAME = new QName("", "orgName");
    private final static QName _IsMain_QNAME = new QName("", "isMain");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.dsc.vo.efgp.ouc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ComDscNanaServicesWebserviceOrganizationUnitCollection }
     * 
     */
    public ComDscNanaServicesWebserviceOrganizationUnitCollection createComDscNanaServicesWebserviceOrganizationUnitCollection() {
        return new ComDscNanaServicesWebserviceOrganizationUnitCollection();
    }

    /**
     * Create an instance of {@link OrganizationUnits }
     * 
     */
    public OrganizationUnits createOrganizationUnits() {
        return new OrganizationUnits();
    }

    /**
     * Create an instance of {@link ComDscNanaDataTransferOrganizationUnitForInvokingListDTO }
     * 
     */
    public ComDscNanaDataTransferOrganizationUnitForInvokingListDTO createComDscNanaDataTransferOrganizationUnitForInvokingListDTO() {
        return new ComDscNanaDataTransferOrganizationUnitForInvokingListDTO();
    }

    /**
     * Create an instance of {@link OrgUnitType }
     * 
     */
    public OrgUnitType createOrgUnitType() {
        return new OrgUnitType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "OID")
    public JAXBElement<String> createOID(String value) {
        return new JAXBElement<String>(_OID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "id")
    public JAXBElement<BigInteger> createId(BigInteger value) {
        return new JAXBElement<BigInteger>(_Id_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "value")
    public JAXBElement<BigInteger> createValue(BigInteger value) {
        return new JAXBElement<BigInteger>(_Value_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "orgName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createOrgName(String value) {
        return new JAXBElement<String>(_OrgName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "isMain")
    public JAXBElement<Boolean> createIsMain(Boolean value) {
        return new JAXBElement<Boolean>(_IsMain_QNAME, Boolean.class, null, value);
    }

}
