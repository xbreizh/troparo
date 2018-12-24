package org.troparo.consumer.impl;


import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(value="transactionManager")
    public List<Book> getBooks() {
        System.out.println("getting in dao");
        return sessionFactory.getCurrentSession().createQuery("from Book").getResultList();
    }
}
