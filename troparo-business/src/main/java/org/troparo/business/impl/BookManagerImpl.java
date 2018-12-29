package org.troparo.business.impl;


import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public List<Book> getBooks() {
        return bookDAO.getBooks();
    }

    @Override
    public Book getBookById(int id) {
        System.out.println("getting id (from business): "+id);
        Book book = bookDAO.getBookById(id);
        if(book!=null) {
            System.out.println("book");
            return book;
        }else{
            System.out.println("book is probably null");
            return null;
        }
    }

    @Override
    public List<Book> getBooksByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map.entrySet()
        ) {
           if(!entry.getValue().equals("?") && !entry.getValue().equals("")){
               criterias.put(entry.getKey(), entry.getValue());
           }
        }
        System.out.println("criterias: "+criterias);
        return bookDAO.getBooksByCriterias(criterias);
    }
}
