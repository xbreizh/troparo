package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static java.lang.Math.toIntExact;

@Named("bookDAO")
public class BookDAOImpl implements BookDAO {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Class cl = Book.class;
    private String request;

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Book> getBooks() {
        logger.info("getting in dao");
        try {
            return sessionFactory.getCurrentSession().createQuery("from Book", Book.class).getResultList();
        }catch(Exception e){
            return null;
        }

    }

    @Override
    public boolean addBook(Book book) {
        logger.info("Book from dao: " + book);
        try {
            sessionFactory.getCurrentSession().persist(book);
        } catch (Exception e) {
            System.err.println("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Book getBookById(int id) {
        logger.info("in the dao: "+id);
        request = "From Book where id = :id";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("id", id);
        try {
            return (Book) query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }



    @Override
    public boolean existingISBN(String isbn) {
        logger.info("in the dao: "+isbn);
        request = "From Book where isbn = :isbn";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        if(query.getResultList().size() !=0){
            logger.info("records found: "+query.getResultList().size());
            return true;
        }else{
            logger.info("no record found for that isbn: "+isbn);
            return false;
        }
    }

    @Override
    public List<Book> getBooksByCriterias(HashMap<String, String> map) {
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
        request = "From Book ";
        request +=criterias;
        logger.info("request: "+request);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        Query query =sessionFactory.getCurrentSession().createQuery(request, Book.class);
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
    public boolean updateBook(Book book) {
        logger.info("Book from dao: " + book.getTitle());
        logger.info("Book from dao: " + book.getAuthor());
        try {
            sessionFactory.getCurrentSession().update(book);
        } catch (Exception e) {
            System.err.println("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Book book) {
        logger.info("Trying to delete" + book);
        try {
            sessionFactory.getCurrentSession().delete(book);
        } catch (Exception e) {
            System.err.println("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int getAvailable(String isbn) {
        Query query = sessionFactory.getCurrentSession().createQuery("select count(*) from Book where isbn = :isbn");
        query.setParameter("isbn", isbn);
        long count = (long) query.getSingleResult();
        logger.info("result found: "+count);
        int i = toIntExact(count);
        logger.info("count: "+i);
        return i;
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        List<Book> list = new ArrayList<>();
        request = "From Book where isbn = :isbn";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        try {
                return (Book) query.getResultList().get(0);
        }catch(Exception e){
            return null;
        }
    }

}
