package org.troparo.business.contract;

import org.troparo.model.Loan;

import java.util.HashMap;
import java.util.List;


public interface LoanManager {

    String addLoan(Loan loan);

    List<Loan> getLoans();

    Loan getLoanById(int id);

    List<Loan> getLoansByCriterias(HashMap<String, String> map);

    String renewLoan(Loan loan);

    String terminate(int id);
}