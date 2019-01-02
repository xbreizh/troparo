package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.loan.*;
import org.troparo.model.Loan;
import org.troparo.services.loanservice.BusinessException;
import org.troparo.services.loanservice.ILoanService;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@WebService(serviceName = "LoanService", endpointInterface = "org.troparo.services.loanservice.ILoanService",
        targetNamespace = "http://troparo.org/services/LoanService/", portName = "LoanServicePort", name = "LoanServiceImpl")
public class LoanServiceImpl implements ILoanService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private LoanManager loanManager;
    @Inject
    private BookManager bookManager;
    @Inject
    private MemberManager memberManager;

    private String exception = "";
    private List<Loan> loanList = new ArrayList<>();
    private LoanTypeOut loanTypeOut = null;
    private LoanTypeIn loanTypeIn = null;
    private LoanListType loanListType = new LoanListType();
    private Loan loan = null;

    // Create
    @Override
    public AddLoanResponseType addLoan(AddLoanRequestType parameters) throws BusinessException {
        AddLoanResponseType ar = new AddLoanResponseType();
        ar.setReturn(true);
        loanTypeIn = parameters.getLoanTypeIn();
        convertLoanTypeInIntoLoan();
        logger.info("loanManager: " + loanManager);
        exception = loanManager.addLoan(loan);
        if (!exception.equals("")) {
            logger.info(exception);
            throw new BusinessException(exception);
        }

        return ar;
    }


    // Update


    // Get One
    @Override
    public GetLoanByIdResponseType getLoanById(GetLoanByIdRequestType parameters) throws BusinessException {

        logger.info("new method added");
        GetLoanByIdResponseType rep = new GetLoanByIdResponseType();
        LoanTypeOut loanTypeOut = new LoanTypeOut();
        Loan loan = loanManager.getLoanById(parameters.getId());
        if (loan == null) {
            throw new BusinessException("no loan found with that id");
        } else {
            loanTypeOut.setId(loan.getId());
            loanTypeOut.setISBN(loan.getBook().getIsbn());
            loanTypeOut.setLogin(loan.getMember().getLogin());
            loanTypeOut.setStartDate(convertDateIntoXmlDate(loan.getStartDate()));
            loanTypeOut.setPlannedEndDate(convertDateIntoXmlDate(loan.getPlannedEndDate()));
            loanTypeOut.setEndDate(convertDateIntoXmlDate(loan.getEndDate()));
            rep.setLoanTypeOut(loanTypeOut);
        }
        return rep;
    }


    // Get All
    @Override
    public LoanListResponseType getAllLoans(LoanListRequestType parameters) throws BusinessException {
        loanList = loanManager.getLoans();
        logger.info("size list: " + loanList.size());

        LoanListResponseType loanListResponseType = new LoanListResponseType();

        convertLoanIntoLoanTypeOut();
        // add loanType to the movieListType

        loanListResponseType.setLoanListType(loanListType);
        return loanListResponseType;
    }


    @Override
    public TerminateLoanResponseType terminateLoan(TerminateLoanRequestType parameters) throws BusinessException {
        TerminateLoanResponseType ar = new TerminateLoanResponseType();
        ar.setReturn(false);
        String feedback = loanManager.terminate(parameters.getId());
        if (feedback.equals("")) {
            ar.setReturn(true);
        }
        return ar;
    }


    // Get List By Criterias
    @Override
    public GetLoanByCriteriasResponseType getLoanByCriterias(GetLoanByCriteriasRequestType parameters) throws BusinessException {
        HashMap<String, String> map = new HashMap<>();
        LoanCriterias criterias = parameters.getLoanCriterias();
        map.put("Login", criterias.getLogin().toUpperCase());
        map.put("ISBN", criterias.getISBN().toUpperCase());
        logger.info("map: " + map);

        loanList = loanManager.getLoansByCriterias(map);
        GetLoanByCriteriasResponseType brt = new GetLoanByCriteriasResponseType();
        logger.info("loanListType beg: " + loanListType.getLoanTypeOut().size());

        convertLoanIntoLoanTypeOut();

        logger.info("loanListType end: " + loanListType.getLoanTypeOut().size());
        brt.setLoanListType(loanListType);
        return brt;
    }


    @Override
    public RenewLoanResponseType renewLoan(RenewLoanRequestType parameters) throws BusinessException {
        return null;
    }


    // Delete


    // Converts Loan from Business into output
    private void convertLoanIntoLoanTypeOut() {
        loanListType.getLoanTypeOut().clear();
        for (Loan loan : loanList) {

            // set values retrieved from DAO class
            loanTypeOut = new LoanTypeOut();
            loanTypeOut.setId(loan.getId());
            loanTypeOut.setLogin(loan.getMember().getLogin());
            loanTypeOut.setISBN(loan.getBook().getIsbn());
            XMLGregorianCalendar startDate = convertDateIntoXmlDate(loan.getStartDate());
            XMLGregorianCalendar endDate = convertDateIntoXmlDate(loan.getEndDate());
            XMLGregorianCalendar plannedEndDate = convertDateIntoXmlDate(loan.getPlannedEndDate());

            loanTypeOut.setStartDate(startDate);
            loanTypeOut.setEndDate(endDate);
            loanTypeOut.setPlannedEndDate(plannedEndDate);

            logger.info("conversion done");

            // converting xml into Date

          /*  XMLGregorianCalendar xcal = xmlCalendar;
            java.util.Date dt = xcal.toGregorianCalendar().getTime();*/


            loanListType.getLoanTypeOut().add(loanTypeOut);
        }
        logger.info("loanListType end: " + loanListType.getLoanTypeOut().size());
    }


    // Converts Input into Loan for business
    private void convertLoanTypeInIntoLoan() {
        loan = new Loan();
        loan.setMember(memberManager.getMemberByLogin(loanTypeIn.getLogin().toUpperCase()));
        loan.setBook(bookManager.getBookByIsbn(loanTypeIn.getISBN().toUpperCase()));
        logger.info("conversion loanType into loan done");
    }


    private XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlCalendar;
    }


}
