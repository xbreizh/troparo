package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Named
public class MemberManagerImpl implements MemberManager {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private String exception = "";

    @Inject
    MemberDAO loginDAO;

    @Override
    public String addMember(Member login) {
        exception = "";
        // checking if already existing
        if (loginDAO.existingLogin(login.getLogin())) {
            exception = "Login already existing";
            return exception;
        }
        // checking that all values are provided
        exception = checkRequiredValuesNotNull(login);
        if (!exception.equals("")) {
            return exception;
        }

        // checking that all values are valid
        exception = checkInsertion(login);
        if (!exception.equals("")) {
            return exception;
        }
        // setting dateJoin
        login.setDateJoin(new Date());
        loginDAO.addMember(login);
        logger.info("exception: " + exception);
        return exception;
    }


    private String checkInsertion(Member login) {
        if (login.getLogin().length() < 5 || login.getLogin().length() > 20){
            return exception = "Login must be 10 or 13 characters: " + login.getLogin();
        }
        if (login.getFirstName().length() < 2 || login.getFirstName().length() > 50) {
            return exception = "FirstName should have between 2 and 200 characters: " + login.getFirstName();
        }
        if (login.getLastName().length() < 2 || login.getLastName().length() > 50) {
            return exception = "LastName should have between 2 and 200 characters: " + login.getLastName();
        }
        if (login.getPassword().length() < 2 || login.getPassword().length() > 200) {
            return exception = "Password should have between 2 and 200 characters: " + login.getPassword();
        }
        if (login.getEmail().length() > 100) {
            return exception = "Email should not have more than 200 characters: " + login.getEmail();
        }

        return exception;
    }

    private String checkRequiredValuesNotNull(Member login) {

        if (login.getLogin().equals("") || login.getLogin().equals("?")) {
            return "login should be filled";
        }
        if (login.getFirstName().equals("") || login.getFirstName().equals("?")) {
            return "FirstName should be filled";
        }
        if (login.getLastName().equals("") || login.getLastName().equals("?")) {
            return "LastName should be filled";
        }
        if (login.getPassword().equals("") || login.getPassword().equals("?")) {
            return "Password should be filled";
        }
        if (login.getEmail().equals("") || login.getEmail().equals("?")) {
            return "Email should be filled";
        }
        return "";
    }

    @Override
    public List<Member> getMembers() {
        return loginDAO.getAllMembers();
    }

    @Override
    public Member getMemberById(int id) {
        logger.info("getting id (from business): " + id);
        Member login = loginDAO.getMemberById(id);
        if (login != null) {
            logger.info("login");
            return login;
        } else {
            logger.info("login is probably null");
            return null;
        }
    }

    @Override
    public List<Member> getMembersByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("?") && !entry.getValue().equals("")) {
                criterias.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("criterias: " + criterias);
        return loginDAO.getMembersByCriterias(criterias);
    }

    @Override
    public String updateMember(Member member) {
        exception = "";
        boolean receivedCriteria=false;
        if (member.getLogin().equals("") || member.getLogin().equals("?")) {
            return "you must provide an Login";
        } else {
            logger.info("member received: "+member);
        }
        List<Member> loginList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("Login", member.getLogin());
        logger.info("map size: "+map.size());
        loginList = loginDAO.getMembersByCriterias(map);
        if (loginList.size() == 0) {
            return "No Item found with that Login";
        }
        logger.info("getting list: " + loginList.size());
        for (Member b : loginList
        ) {
            if (!member.getFirstName().equals("") && !member.getFirstName().equals("?")) {
                receivedCriteria = true;
                b.setFirstName(member.getFirstName());
            }
            if (!member.getLastName().equals("") && !member.getLastName().equals("?")) {
                receivedCriteria = true;
                b.setLastName(member.getLastName());
            }
            if (!member.getPassword().equals("") && !member.getPassword().equals("?")) {
                receivedCriteria = true;
                b.setPassword(member.getPassword());
            }
            if (!member.getEmail().equals("") && !member.getEmail().equals("?")) {
                receivedCriteria = true;
                b.setEmail(member.getEmail());
            }
            if(!receivedCriteria){
                return "No criteria was passed in";
            }
            logger.info(b.getLogin());
            loginDAO.updateMember(b);
            logger.info("updated: " + b.getId());
        }

        return exception;
    }

    @Override
    public String remove(int id) {
        Member login = loginDAO.getMemberById(id);

        if (login == null) {
            return exception = "No item found";
        } else {
            loginDAO.remove(login);
        }
        return exception;
    }

}
