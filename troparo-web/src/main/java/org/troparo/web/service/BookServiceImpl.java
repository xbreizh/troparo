package org.troparo.web.service;

import org.troparo.consumer.contract.BookDAO;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessException;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@WebService(serviceName="BookService", endpointInterface="org.troparo.services.bookservice.IBookService",
        targetNamespace="http://troparo.org/services/BookService/", portName="BookServicePort", name="BookServiceImpl")
public class BookServiceImpl implements IBookService {

    @Inject
    private BookDAO bookDAO;


    @Override
    public AddBookResponseType addBook(AddBookRequestType parameters) throws BusinessException {
        AddBookResponseType ar = new AddBookResponseType();
        ar.setReturn(false);
            Book book = new Book();
            BookTypeIn bt = parameters.getBookTypeIn();
            book.setIsbn(bt.getISBN());
            book.setTitle(bt.getTitle());
            book.setAuthor(bt.getAuthor());
            book.setInsert_date(new Date());
            book.setPublication(new Date());
            book.setEdition(bt.getEdition());
            book.setNbPages(bt.getNbPages());
            book.setKeywords(bt.getKeywords());




        if(bt.getTitle().length() < 2 || bt.getTitle().length() > 200){
           throw new BusinessException("Title should have between 3 and 200 characters");
            }
        if(bt.getAuthor().length() < 2 || bt.getAuthor().length() > 200){
            throw new BusinessException("Author should have between 3 and 200 characters");
        }
        if (bookDAO.addBook(book) == true) {
            ar.setReturn(true);
        }

        return ar;
    }

    @Override
    public BookListResponseType getAllBooks(BookListRequestType parameters) throws BusinessException {

        List<Book> bookList= bookDAO.getBooks();
        System.out.println("size list: "+bookList.size());
        BookTypeOut bookTypeOut;
        BookListType bookListType = new BookListType();
        BookListResponseType bookListResponseType = new BookListResponseType();

        for(Book book : bookList){

            // set values retrieved from DAO class
            bookTypeOut = new BookTypeOut();
            bookTypeOut.setISBN(book.getIsbn());
            bookTypeOut.setTitle(book.getTitle());
            bookTypeOut.setAuthor(book.getAuthor());
            bookTypeOut.setEdition(book.getEdition());

            try {
                GregorianCalendar c = new GregorianCalendar();
                Date date = book.getPublication();
                XMLGregorianCalendar xmlDate;
                xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                bookTypeOut.setPublication(xmlDate);
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }

            /*bookTypeOut.se(book.getEdition());*/
            bookTypeOut.setNbPages(book.getNbPages());
            bookTypeOut.setKeywords(book.getKeywords());
            bookListType.getBookTypeOut().add(bookTypeOut); // add movieType to the movieListType
        }

        bookListResponseType.setBookListType(bookListType);
        return bookListResponseType;
    }
}
