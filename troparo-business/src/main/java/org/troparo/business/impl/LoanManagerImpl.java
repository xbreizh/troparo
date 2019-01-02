package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.troparo.business.contract.LoanManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Transactional
@Named
public class LoanManagerImpl implements LoanManager {
    private final int loanDuration = 28;
    private final int renewDuration = 28;
    private final int maxLoanDuration = loanDuration + renewDuration;
    @Inject
    LoanDAO loanDAO;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String exception = "";

    @Override
    public String addLoan(Loan loan) {
        loan.setStartDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.getStartDate());
        cal.add(Calendar.DATE, loanDuration);
        loan.setEndDate(cal.getTime());
        if (loan.getMember() == null) {
            return "invalid member";
        }
        if (loan.getBook() == null) {
            return "invalid book";
        }
        loanDAO.addLoan(loan);
        return null;
    }

    @Override
    public List<Loan> getLoans() {
        return loanDAO.getLoans();
    }

    @Override
    public Loan getLoanById(int id) {
        logger.info("getting id (from business): " + id);
        Loan loan = loanDAO.getLoanById(id);
        if (loan != null) {
            logger.info("loan");
            return loan;
        } else {
            logger.info("loan is probably null");
            return null;
        }
    }

    @Override
    public List<Loan> getLoansByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("?") && !entry.getValue().equals("")) {
                criterias.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("criterias: " + criterias);
        return loanDAO.getLoansByCriterias(criterias);
    }

    @Override
    public String renewLoan(Loan loan) {
        exception = "";
        Date start = loan.getStartDate();
        Date end = loan.getEndDate();

        int diffInDays = (int) ((end.getTime() - start.getTime())
                / (1000 * 60 * 60 * 24));
        logger.info("diff days is: " + diffInDays);
        if (diffInDays > loanDuration) {
            return "loan has already been renewed";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loan.getStartDate());
            cal.add(Calendar.DATE, renewDuration);
            loan.setEndDate(cal.getTime());
        }
        return exception;
    }

    @Override
    public String terminate(int id) {
        Loan loan;
        try {
            loan = loanDAO.getLoanById(id);
            loan.setEndDate(new Date());
        } catch (NullPointerException e) {
            return "loan couldn't be terminated!";
        }
        return "";
    }
}
