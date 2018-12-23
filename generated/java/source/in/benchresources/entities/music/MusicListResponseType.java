
package in.benchresources.entities.music;

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
 *         &lt;element name="composer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://benchresources.in/entities/music}MovieListType"/>
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
    "composer",
    "movieListType"
})
@XmlRootElement(name = "MusicListResponseType")
public class MusicListResponseType {

    @XmlElement(required = true)
    protected String composer;
    @XmlElement(name = "MovieListType", required = true)
    protected MovieListType movieListType;

    /**
     * Gets the value of the composer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComposer() {
        return composer;
    }

    /**
     * Sets the value of the composer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComposer(String value) {
        this.composer = value;
    }

    /**
     * Gets the value of the movieListType property.
     * 
     * @return
     *     possible object is
     *     {@link MovieListType }
     *     
     */
    public MovieListType getMovieListType() {
        return movieListType;
    }

    /**
     * Sets the value of the movieListType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovieListType }
     *     
     */
    public void setMovieListType(MovieListType value) {
        this.movieListType = value;
    }

}
