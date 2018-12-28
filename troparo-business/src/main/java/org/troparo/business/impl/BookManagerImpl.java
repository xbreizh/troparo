package org.troparo.business.impl;


import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Transactional
@Named
public class BookManagerImpl implements BookManager {

    @Inject
    BookDAO bookDAO;

    @Override
    public String addBook(Book book) {
        String exception =null;
        if (book.getTitle().length() < 2 || book.getTitle().length() > 200) {
            return exception="Title should have between 3 and 200 characters";
        }
        if (book.getAuthor().length() < 2 || book.getAuthor().length() > 200) {
            return exception="Author should have between 3 and 200 characters";
        }
        System.out.println("in the ");
        bookDAO.addBook(book);

        return exception;
    }
}
