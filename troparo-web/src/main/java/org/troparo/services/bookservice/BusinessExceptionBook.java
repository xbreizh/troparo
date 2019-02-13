
package org.troparo.services.bookservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-02-11T17:21:21.206+01:00
 * Generated source version: 3.2.7
 */

@WebFault(name = "BusinessBookFaultType", targetNamespace = "http://troparo.org/entities/book")
public class BusinessExceptionBook extends Exception {

    private org.troparo.entities.book.BusinessBookFaultType businessBookFaultType;

    public BusinessExceptionBook() {
        super();
    }

    public BusinessExceptionBook(String message) {
        super(message);
    }

    public BusinessExceptionBook(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public BusinessExceptionBook(String message, org.troparo.entities.book.BusinessBookFaultType businessBookFaultType) {
        super(message);
        this.businessBookFaultType = businessBookFaultType;
    }

    public BusinessExceptionBook(String message, org.troparo.entities.book.BusinessBookFaultType businessBookFaultType, java.lang.Throwable cause) {
        super(message, cause);
        this.businessBookFaultType = businessBookFaultType;
    }

    public org.troparo.entities.book.BusinessBookFaultType getFaultInfo() {
        return this.businessBookFaultType;
    }
}
