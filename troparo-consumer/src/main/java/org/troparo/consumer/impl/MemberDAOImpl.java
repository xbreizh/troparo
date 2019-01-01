package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

@Named("memberDAO")
public class MemberDAOImpl implements MemberDAO {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Class cl = Member.class;
    private String request;

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public boolean addMember(Member member) {
        logger.info("Member from dao: " + member);
        try {
            sessionFactory.getCurrentSession().persist(member);
        } catch (Exception e) {
            System.err.println("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public List<Member> getAllMembers() {
        logger.info("getting in dao");
        try {
            return sessionFactory.getCurrentSession().createQuery("from Member",cl).getResultList();
        }catch(Exception e){
            return null;
        }

    }


    @Override
    public Member getMemberById(int id) {
        logger.info("in the dao: "+id);
        request = "From Member where id = :id";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("id", id);
        try {
            return (Member) query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }



    @Override
    public boolean existingLogin(String login) {
        logger.info("in the dao: "+login);
        request = "From Member where login = :login";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("login", login);
        if(query.getResultList().size() !=0){
            logger.info("records found: "+query.getResultList().size());
            return true;
        }else{
            logger.info("no record found for that login: "+login);
            return false;
        }
    }

    @Override
    public List<Member> getMembersByCriterias(HashMap<String, String> map) {
        logger.info("map received in DAO: "+map);
        String criterias="";
        for (Map.Entry<String, String> entry : map.entrySet()
             ) {
            if(!criterias.equals("")){
                criterias+=" and ";
            }else{
                criterias+= "where ";
            }
            criterias+= entry.getKey()+" like :"+entry.getKey();
        }
        request = "From Member ";
        request +=criterias;
        logger.info("request: "+request);/*
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();*/
        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            logger.info("criteria: "+entry.getValue());
           query.setParameter(entry.getKey(), "%"+entry.getValue()+"%");
        }
        try {
            logger.info("list with criterias size: "+query.getResultList().size());
            return  query.getResultList();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean updateMember(Member member) {
        logger.info("Member from dao: " + member.getLogin());
        try {
            sessionFactory.getCurrentSession().update(member);
        } catch (Exception e) {
            System.err.println("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Member member) {
        logger.info("Trying to delete" + member);
        try {
            sessionFactory.getCurrentSession().delete(member);
        } catch (Exception e) {
            System.err.println("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean checkToken(String token) {
        logger.info("in the dao: "+token);
        request = "From Member where token = :token";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("token", token);
        if(query.getResultList().size() !=0){
            logger.info("token valid");
            return true;
        }else{
            logger.info("invalid token");
            return false;
        }
    }

    @Override
    public boolean invalidToken(String token) {
        logger.info("in the dao: "+token);
        request = "update Member set token = null where token = :token";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("token", token);
        return true;
    }


    @Override
    public Member getMemberByLogin(String login) {
        List<Member> list = new ArrayList<>();
        request = "From Member where login = :login";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("login", login);
        try {
                return (Member) query.getResultList().get(0);
        }catch(Exception e){
            return null;
        }
    }

}
