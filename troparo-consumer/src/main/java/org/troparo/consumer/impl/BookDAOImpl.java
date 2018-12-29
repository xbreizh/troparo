package org.troparo.consumer.impl;


import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("bookDAO")
public class BookDAOImpl implements BookDAO {
    private Class cl = Book.class;

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
        String request = "From Book where book_id = :id";

        Query query =sessionFactory.getCurrentSession().createQuery("From Book where book_id = :id", Book.class);
        query.setParameter("id", id);
        try {
            return (Book) query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}
