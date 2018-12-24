package org.troparo.web.service;

import org.troparo.consumer.contract.BookDAO;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessException;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;
import javax.jws.WebService;

import java.util.List;

@WebService(serviceName="BookService", endpointInterface="org.troparo.services.bookservice.IBookService",
        targetNamespace="http://troparo.org/services/BookService/", portName="BookServicePort", name="BookServiceImpl")
public class BookServiceImpl implements IBookService {

    @Inject
    private BookDAO bookDAO;


    @Override
    public AddBookResponseType addBook(AddBookRequestType parameters) throws BusinessException {
        Book book = new Book();
        BookType bt = parameters.getBookType();
        book.setName(bt.getName());
        book.setAuthor(bt.getAuthor());
        book.setEdition(bt.getEdition());
        book.setPublication(bt.getPublication());

        AddBookResponseType ar = new AddBookResponseType();
        ar.setReturn(false);
        if(bookDAO.addBook(book)==true){
            ar.setReturn(true);
        }
        return ar;
    }

    @Override
    public BookListResponseType getAllBooks(BookListRequestType parameters) throws BusinessException {

        List<Book> bookList= bookDAO.getBooks();
        System.out.println("size list: "+bookList.size());
        BookType bookType;
        BookListType bookListType = new BookListType();
        BookListResponseType bookListResponseType = new BookListResponseType();

        for(Book book : bookList){

            // set values retrieved from DAO class
            bookType = new BookType();
            bookType.setName(book.getName());
            bookType.setAuthor(book.getAuthor());
            bookType.setPublication(book.getPublication());
            bookType.setEdition(book.getEdition());
            bookListType.getBookType().add(bookType); // add movieType to the movieListType
        }

        bookListResponseType.setBookListType(bookListType);
        return bookListResponseType;
    }
}
