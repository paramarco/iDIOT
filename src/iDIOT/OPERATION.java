//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci\u00f3n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder\u00e1n si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.01.10 a las 02:07:17 PM CET 
//


package iDIOT;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SECTORIZATION">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BASIC_WIDE_DELIVERY">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="BASIC_WIDE" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="BASIC_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="WIDE_SECTORS_DELIVERY">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="WIDE_SECTORS" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="VOICE_FREQUENCY">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="ONE" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                                 &lt;element name="TWO" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="SECTOR_CONSOLIDATION" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="EXECUTIVE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="PLANNER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="CONTROL_CENTER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "sectorization"
})
@XmlRootElement(name = "OPERATION")
public class OPERATION {

    @XmlElement(name = "SECTORIZATION", required = true)
    protected OPERATION.SECTORIZATION sectorization;

    /**
     * Obtiene el valor de la propiedad sectorization.
     * 
     * @return
     *     possible object is
     *     {@link OPERATION.SECTORIZATION }
     *     
     */
    public OPERATION.SECTORIZATION getSECTORIZATION() {
        return sectorization;
    }

    /**
     * Define el valor de la propiedad sectorization.
     * 
     * @param value
     *     allowed object is
     *     {@link OPERATION.SECTORIZATION }
     *     
     */
    public void setSECTORIZATION(OPERATION.SECTORIZATION value) {
        this.sectorization = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BASIC_WIDE_DELIVERY">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="BASIC_WIDE" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="BASIC_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="WIDE_SECTORS_DELIVERY">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="WIDE_SECTORS" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="VOICE_FREQUENCY">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="ONE" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                                       &lt;element name="TWO" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="SECTOR_CONSOLIDATION" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="EXECUTIVE" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="PLANNER" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="CONTROL_CENTER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "basicwidedelivery",
        "widesectorsdelivery"
    })
    public static class SECTORIZATION {

        @XmlElement(name = "BASIC_WIDE_DELIVERY", required = true)
        protected OPERATION.SECTORIZATION.BASICWIDEDELIVERY basicwidedelivery;
        @XmlElement(name = "WIDE_SECTORS_DELIVERY", required = true)
        protected OPERATION.SECTORIZATION.WIDESECTORSDELIVERY widesectorsdelivery;

        /**
         * Obtiene el valor de la propiedad basicwidedelivery.
         * 
         * @return
         *     possible object is
         *     {@link OPERATION.SECTORIZATION.BASICWIDEDELIVERY }
         *     
         */
        public OPERATION.SECTORIZATION.BASICWIDEDELIVERY getBASICWIDEDELIVERY() {
            return basicwidedelivery;
        }

        /**
         * Define el valor de la propiedad basicwidedelivery.
         * 
         * @param value
         *     allowed object is
         *     {@link OPERATION.SECTORIZATION.BASICWIDEDELIVERY }
         *     
         */
        public void setBASICWIDEDELIVERY(OPERATION.SECTORIZATION.BASICWIDEDELIVERY value) {
            this.basicwidedelivery = value;
        }

        /**
         * Obtiene el valor de la propiedad widesectorsdelivery.
         * 
         * @return
         *     possible object is
         *     {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY }
         *     
         */
        public OPERATION.SECTORIZATION.WIDESECTORSDELIVERY getWIDESECTORSDELIVERY() {
            return widesectorsdelivery;
        }

        /**
         * Define el valor de la propiedad widesectorsdelivery.
         * 
         * @param value
         *     allowed object is
         *     {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY }
         *     
         */
        public void setWIDESECTORSDELIVERY(OPERATION.SECTORIZATION.WIDESECTORSDELIVERY value) {
            this.widesectorsdelivery = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="BASIC_WIDE" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="BASIC_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "basicwide"
        })
        public static class BASICWIDEDELIVERY {

            @XmlElement(name = "BASIC_WIDE")
            protected List<OPERATION.SECTORIZATION.BASICWIDEDELIVERY.BASICWIDE> basicwide;

            /**
             * Gets the value of the basicwide property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the basicwide property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getBASICWIDE().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link OPERATION.SECTORIZATION.BASICWIDEDELIVERY.BASICWIDE }
             * 
             * 
             */
            public List<OPERATION.SECTORIZATION.BASICWIDEDELIVERY.BASICWIDE> getBASICWIDE() {
                if (basicwide == null) {
                    basicwide = new ArrayList<OPERATION.SECTORIZATION.BASICWIDEDELIVERY.BASICWIDE>();
                }
                return this.basicwide;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="BASIC_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                "basicsector",
                "widesector"
            })
            public static class BASICWIDE {

                @XmlElement(name = "BASIC_SECTOR", required = true)
                protected String basicsector;
                @XmlElement(name = "WIDE_SECTOR", required = true)
                protected String widesector;

                /**
                 * Obtiene el valor de la propiedad basicsector.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getBASICSECTOR() {
                    return basicsector;
                }

                /**
                 * Define el valor de la propiedad basicsector.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setBASICSECTOR(String value) {
                    this.basicsector = value;
                }

                /**
                 * Obtiene el valor de la propiedad widesector.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getWIDESECTOR() {
                    return widesector;
                }

                /**
                 * Define el valor de la propiedad widesector.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setWIDESECTOR(String value) {
                    this.widesector = value;
                }

            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="WIDE_SECTORS" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="VOICE_FREQUENCY">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="ONE" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                             &lt;element name="TWO" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="SECTOR_CONSOLIDATION" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="EXECUTIVE" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="PLANNER" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="CONTROL_CENTER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "widesectors"
        })
        public static class WIDESECTORSDELIVERY {

            @XmlElement(name = "WIDE_SECTORS")
            protected List<OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS> widesectors;

            /**
             * Gets the value of the widesectors property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the widesectors property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getWIDESECTORS().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS }
             * 
             * 
             */
            public List<OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS> getWIDESECTORS() {
                if (widesectors == null) {
                    widesectors = new ArrayList<OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS>();
                }
                return this.widesectors;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="WIDE_SECTOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="VOICE_FREQUENCY">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="ONE" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *                   &lt;element name="TWO" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="SECTOR_CONSOLIDATION" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="EXECUTIVE" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="PLANNER" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="CONTROL_CENTER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
                "widesector",
                "voicefrequency",
                "sectorconsolidation",
                "controlcenter"
            })
            public static class WIDESECTORS {

                @XmlElement(name = "WIDE_SECTOR", required = true)
                protected String widesector;
                @XmlElement(name = "VOICE_FREQUENCY", required = true)
                protected OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.VOICEFREQUENCY voicefrequency;
                @XmlElement(name = "SECTOR_CONSOLIDATION")
                protected OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.SECTORCONSOLIDATION sectorconsolidation;
                @XmlElement(name = "CONTROL_CENTER")
                protected String controlcenter;

                /**
                 * Obtiene el valor de la propiedad widesector.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getWIDESECTOR() {
                    return widesector;
                }

                /**
                 * Define el valor de la propiedad widesector.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setWIDESECTOR(String value) {
                    this.widesector = value;
                }

                /**
                 * Obtiene el valor de la propiedad voicefrequency.
                 * 
                 * @return
                 *     possible object is
                 *     {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.VOICEFREQUENCY }
                 *     
                 */
                public OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.VOICEFREQUENCY getVOICEFREQUENCY() {
                    return voicefrequency;
                }

                /**
                 * Define el valor de la propiedad voicefrequency.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.VOICEFREQUENCY }
                 *     
                 */
                public void setVOICEFREQUENCY(OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.VOICEFREQUENCY value) {
                    this.voicefrequency = value;
                }

                /**
                 * Obtiene el valor de la propiedad sectorconsolidation.
                 * 
                 * @return
                 *     possible object is
                 *     {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.SECTORCONSOLIDATION }
                 *     
                 */
                public OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.SECTORCONSOLIDATION getSECTORCONSOLIDATION() {
                    return sectorconsolidation;
                }

                /**
                 * Define el valor de la propiedad sectorconsolidation.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.SECTORCONSOLIDATION }
                 *     
                 */
                public void setSECTORCONSOLIDATION(OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS.SECTORCONSOLIDATION value) {
                    this.sectorconsolidation = value;
                }

                /**
                 * Obtiene el valor de la propiedad controlcenter.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCONTROLCENTER() {
                    return controlcenter;
                }

                /**
                 * Define el valor de la propiedad controlcenter.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCONTROLCENTER(String value) {
                    this.controlcenter = value;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="EXECUTIVE" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="PLANNER" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="OPERATOR" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                    "executive",
                    "planner",
                    "operator"
                })
                public static class SECTORCONSOLIDATION {

                    @XmlElement(name = "EXECUTIVE", required = true)
                    protected String executive;
                    @XmlElement(name = "PLANNER", required = true)
                    protected String planner;
                    @XmlElement(name = "OPERATOR", required = true)
                    protected String operator;

                    /**
                     * Obtiene el valor de la propiedad executive.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEXECUTIVE() {
                        return executive;
                    }

                    /**
                     * Define el valor de la propiedad executive.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEXECUTIVE(String value) {
                        this.executive = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad planner.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPLANNER() {
                        return planner;
                    }

                    /**
                     * Define el valor de la propiedad planner.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPLANNER(String value) {
                        this.planner = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad operator.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getOPERATOR() {
                        return operator;
                    }

                    /**
                     * Define el valor de la propiedad operator.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setOPERATOR(String value) {
                        this.operator = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="ONE" type="{http://www.w3.org/2001/XMLSchema}float"/>
                 *         &lt;element name="TWO" type="{http://www.w3.org/2001/XMLSchema}float"/>
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
                    "one",
                    "two"
                })
                public static class VOICEFREQUENCY {

                    @XmlElement(name = "ONE")
                    protected float one;
                    @XmlElement(name = "TWO")
                    protected float two;

                    /**
                     * Obtiene el valor de la propiedad one.
                     * 
                     */
                    public float getONE() {
                        return one;
                    }

                    /**
                     * Define el valor de la propiedad one.
                     * 
                     */
                    public void setONE(float value) {
                        this.one = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad two.
                     * 
                     */
                    public float getTWO() {
                        return two;
                    }

                    /**
                     * Define el valor de la propiedad two.
                     * 
                     */
                    public void setTWO(float value) {
                        this.two = value;
                    }

                }

            }

        }

    }

}
