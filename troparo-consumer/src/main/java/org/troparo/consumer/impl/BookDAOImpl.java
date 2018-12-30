package org.troparo.consumer.impl;


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
    private Class cl = Book.class;
    private String request;

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Book> getBooks() {
        System.out.println("getting in dao");
        try {
            return sessionFactory.getCurrentSession().createQuery("from Book", Book.class).getResultList();
        }catch(Exception e){
            return null;
        }

    }

    @Override
    public boolean addBook(Book book) {
        System.out.println("Book from dao: " + book);
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
        System.out.println("in the dao: "+id);
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
        System.out.println("in the dao: "+isbn);
        request = "From Book where isbn = :isbn";

        Query query =sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        if(query.getResultList().size() !=0){
            System.out.println("records found: "+query.getResultList().size());
            return true;
        }else{
            System.out.println("no record found for that isbn: "+isbn);
            return false;
        }
    }

    @Override
    public List<Book> getBooksByCriterias(HashMap<String, String> map) {
        System.out.println("entering the dark dao");
        System.out.println("map: "+map);
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
        System.out.println("request: "+request);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        Query query =sessionFactory.getCurrentSession().createQuery(request, Book.class);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            System.out.println("value: "+entry.getValue());
           query.setParameter(entry.getKey(), "%"+entry.getValue()+"%");
        }
        System.out.println("query: "+query.toString());
        try {
            return  query.getResultList();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean updateBook(Book book) {
        System.out.println("Book from dao: " + book.getTitle());
        System.out.println("Book from dao: " + book.getAuthor());
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
        System.out.println("Trying to delete" + book);
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
        System.out.println("result found: "+count);
        int i = toIntExact(count);
        System.out.println("count: "+i);
        return i;
    }

    @Override
    public Book getBookByISbn(String isbn) {
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

   /* @Override
    public boolean addCopies(String isbn, int copies) {
        Book book;
        System.out.println("nb copied: "+copies);
        for(int i=0;i<copies;i++){
            book = new Book();
            book = getBookByISbn(isbn);
            book.setId(0);
            System.out.println("before new insertion: "+book);
            book.setInsert_date(new Date());
            addBook(book);
        }
        return false;
    }*/
}
