package org.troparo.consumer.impl;


import org.hibernate.SessionFactory;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("bookDAO")
public class BookDAOImpl implements BookDAO {

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Book> getBooks() {
        System.out.println("getting in dao");
        return sessionFactory.getCurrentSession().createQuery("from Book", Book.class).getResultList();
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
}
