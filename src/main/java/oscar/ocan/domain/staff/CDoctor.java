//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.24 at 10:52:14 PM EDT 
//


package oscar.ocan.domain.staff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}CYes"/>
 *         &lt;element ref="{}CNo"/>
 *         &lt;element ref="{}CNone_available"/>
 *         &lt;element ref="{}CContact_Information_"/>
 *         &lt;element ref="{}CLast_Seen_"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cYes",
    "cNo",
    "cNoneAvailable",
    "cContactInformation",
    "cLastSeen"
})
@XmlRootElement(name = "CDoctor")
public class CDoctor {

    @XmlElement(name = "CYes", required = true)
    protected String cYes;
    @XmlElement(name = "CNo", required = true)
    protected String cNo;
    @XmlElement(name = "CNone_available", required = true)
    protected String cNoneAvailable;
    @XmlElement(name = "CContact_Information_", required = true)
    protected String cContactInformation;
    @XmlElement(name = "CLast_Seen_", required = true)
    protected String cLastSeen;

    /**
     * Gets the value of the cYes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCYes() {
        return cYes;
    }

    /**
     * Sets the value of the cYes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCYes(String value) {
        this.cYes = value;
    }

    /**
     * Gets the value of the cNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNo() {
        return cNo;
    }

    /**
     * Sets the value of the cNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNo(String value) {
        this.cNo = value;
    }

    /**
     * Gets the value of the cNoneAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNoneAvailable() {
        return cNoneAvailable;
    }

    /**
     * Sets the value of the cNoneAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNoneAvailable(String value) {
        this.cNoneAvailable = value;
    }

    /**
     * Gets the value of the cContactInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCContactInformation() {
        return cContactInformation;
    }

    /**
     * Sets the value of the cContactInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCContactInformation(String value) {
        this.cContactInformation = value;
    }

    /**
     * Gets the value of the cLastSeen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLastSeen() {
        return cLastSeen;
    }

    /**
     * Sets the value of the cLastSeen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLastSeen(String value) {
        this.cLastSeen = value;
    }

}