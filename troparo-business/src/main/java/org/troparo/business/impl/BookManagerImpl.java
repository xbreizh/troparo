package org.troparo.business.impl;


import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Transactional
@Named
public class BookManagerImpl implements BookManager {

    private String exception =null;

    @Inject
    BookDAO bookDAO;

    @Override
    public String addBook(Book book) {
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

    @Override
    public String updateBook(Book book) {
        if(book.getIsbn().equals("")|| book.getIsbn().equals("?")){
            return "you must provide an ISBN";
        }else{
            System.out.println(book.getTitle());
            System.out.println(book.getAuthor());
        }
        List<Book> bookList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("ISBN", book.getIsbn());
        bookList = bookDAO.getBooksByCriterias(map);
        if(bookList.size()==0){
            return "No Item found with that ISBN";
        }
        System.out.println("getting list: "+bookList.size());
        for (Book b: bookList
             ) {
            if(!book.getTitle().equals("")&& !book.getTitle().equals("?")){
                b.setTitle(book.getTitle());
            }
            if(!book.getAuthor().equals("")&& !book.getAuthor().equals("?")){
                b.setAuthor(book.getAuthor());
            }
            if(!book.getEdition().equals("")&& !book.getEdition().equals("?")){
                b.setEdition(book.getEdition());
            }
            if(!book.getPublication().equals("")&& !book.getPublication().equals("?")){
                b.setPublication(book.getPublication());
            }
            if(book.getNbPages()!=0){
                b.setNbPages(book.getNbPages());
            }
            if(!book.getKeywords().equals("")&& !book.getKeywords().equals("?")){
                b.setKeywords(book.getKeywords());
            }
            System.out.println(b.getAuthor());
            System.out.println(b.getTitle());
            bookDAO.updateBook(b);
            System.out.println("updated: "+b.getBookId());
        }

        return exception;
    }
}
