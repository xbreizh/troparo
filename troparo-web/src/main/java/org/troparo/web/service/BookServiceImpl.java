package org.troparo.web.service;

import org.troparo.consumer.contract.BookDAO;
import org.troparo.entities.book.BookListRequestType;
import org.troparo.entities.book.BookListResponseType;
import org.troparo.entities.book.BookListType;
import org.troparo.entities.book.BookType;
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
