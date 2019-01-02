package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;

@Named("loanDAO")
public class LoanDAOImpl implements LoanDAO {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Class cl = Loan.class;
    private String request;

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Loan> getLoans() {
        return null;
    }

    @Override
    public boolean addLoan(Loan loan) {
        return false;
    }

    @Override
    public Loan getLoanById(int id) {
        return null;
    }

    @Override
    public Loan getLoanByIsbn(String isbn) {
        return null;
    }

    @Override
    public Loan getLoanByLogin(String login) {
        return null;
    }

    @Override
    public List<Loan> getLoansByCriterias(HashMap<String, String> map) {
        return null;
    }

    @Override
    public boolean renew(int id) {
        return false;
    }

    @Override
    public boolean terminate(Loan loan) {
        return false;
    }
}
